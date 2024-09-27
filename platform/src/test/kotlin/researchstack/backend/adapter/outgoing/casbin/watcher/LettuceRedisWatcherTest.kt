package researchstack.backend.adapter.outgoing.casbin.watcher

import io.lettuce.core.RedisConnectionException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import redis.embedded.RedisServer
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST

@ExperimentalCoroutinesApi
internal class LettuceRedisWatcherTest {
    private val channelName = "TEST_LETTUCE_REDIS_WATCHER"
    private val redisHost = "localhost"
    private val redisPort = 6389
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
    @Tag(NEGATIVE_TEST)
    fun `update should throw RedisConnectionException when it tried connect to wrong host`() {
        val watcher = LettuceRedisWatcher(
            "wronghost",
            redisPort,
            channelName,
            2000,
            "test-pw",
            false
        )

        assertThrows<RedisConnectionException> {
            watcher.update()
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `update should throw RedisConnectionException when it tried connect to wrong port`() {
        val watcher = LettuceRedisWatcher(
            redisHost,
            9999,
            channelName,
            2000,
            "test-pw",
            false
        )

        assertThrows<RedisConnectionException> {
            watcher.update()
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `update should throw RedisConnectionException when it failed to connect to redis server cause of password`() {
        val watcher = LettuceRedisWatcher(
            redisHost,
            redisPort,
            channelName,
            2000,
            "test-pw",
            false
        )

        assertThrows<RedisConnectionException> {
            watcher.update()
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `update should throw RedisConnectionException when it failed to connect to redis server cause of ssl hankshake timeout`() {
        val watcher = LettuceRedisWatcher(
            redisHost,
            redisPort,
            channelName,
            2000,
            null,
            true
        )

        assertThrows<RedisConnectionException> {
            watcher.update()
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `update should work properly`() {
        val watcher = LettuceRedisWatcher(
            redisHost,
            redisPort,
            channelName,
            2000,
            null,
            false
        )

        assertDoesNotThrow {
            watcher.update()
        }
    }
}
