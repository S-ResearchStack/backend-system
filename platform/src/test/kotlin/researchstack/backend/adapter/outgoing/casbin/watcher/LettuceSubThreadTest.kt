package researchstack.backend.adapter.outgoing.casbin.watcher

import io.lettuce.core.ClientOptions
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import io.lettuce.core.resource.ClientResources
import io.lettuce.core.resource.DefaultClientResources
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import redis.embedded.RedisServer
import researchstack.backend.POSITIVE_TEST
import java.time.Duration
import java.time.temporal.ChronoUnit

internal class LettuceSubThreadTest {
    private val channelName = "TEST_LETTUCE_SUB_THREAD"
    private val redisHost = "localhost"
    private val redisPort = 6391
    private val redisServer = RedisServer(redisPort)

    @BeforeEach
    fun init() {
        redisServer.start()
    }

    @AfterEach
    fun finish() {
        redisServer.stop()
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `redis listener should work properly`() {
        val pattern = "test-p"

        val clientResources: ClientResources = DefaultClientResources.builder()
            .ioThreadPoolSize(4)
            .computationThreadPoolSize(4)
            .build()

        val redisUri = RedisURI.builder()
            .withHost(redisHost)
            .withPort(redisPort)
            .withTimeout(Duration.of(2000, ChronoUnit.MILLIS))
            .build()

        val redisClient = RedisClient.create(clientResources, redisUri)
        redisClient.options = ClientOptions
            .builder()
            .autoReconnect(true)
            .pingBeforeActivateConnection(true)
            .build()
        val conn = redisClient.connectPubSub()

        val mockRedisClient = mockk<RedisClient>()

        every {
            mockRedisClient.connectPubSub()
        } returns conn

        val subThread = LettuceSubThread(mockRedisClient, channelName)
        subThread.setUpdateCallback(
            mockk<Runnable>() {
                every {
                    run()
                } returns Unit
            }
        )
        subThread.start()

        assertDoesNotThrow {
            conn.sync().punsubscribe(pattern)
            conn.sync().psubscribe(pattern)
            conn.sync().unsubscribe(channelName)
            redisClient.connectPubSub().sync().publish(pattern, "test")
        }
    }
}
