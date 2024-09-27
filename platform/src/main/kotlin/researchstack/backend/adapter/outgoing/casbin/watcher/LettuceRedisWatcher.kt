package researchstack.backend.adapter.outgoing.casbin.watcher

import io.lettuce.core.ClientOptions
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import io.lettuce.core.resource.ClientResources
import io.lettuce.core.resource.DefaultClientResources
import org.casbin.jcasbin.persist.Watcher
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.UUID
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

class LettuceRedisWatcher(
    redisIp: String,
    redisPort: Int,
    private val redisChannelName: String,
    timeout: Int,
    password: String?,
    isSsl: Boolean
) : Watcher {
    private val localId = UUID.randomUUID().toString()
    private val redisClient = getLettuceRedisClient(redisIp, redisPort, password, timeout, isSsl)
    private val lettuceSubThread = LettuceSubThread(redisClient, redisChannelName)

    init {
        lettuceSubThread.start()
    }

    override fun setUpdateCallback(runnable: Runnable) {
        lettuceSubThread.setUpdateCallback(runnable)
    }

    override fun setUpdateCallback(consumer: Consumer<String>) {
        lettuceSubThread.setUpdateCallback(consumer)
    }

    override fun update() {
        try {
            redisClient.connectPubSub().use { statefulRedisPubSubConnection ->
                if (statefulRedisPubSubConnection.isOpen) {
                    val msg = "Casbin policy has a new version from redis watcher: $localId"
                    statefulRedisPubSubConnection.async().publish(redisChannelName, msg)
                        .get(100, TimeUnit.MILLISECONDS)
                }
            }
        } catch (e: InterruptedException) {
            throw RuntimeException("Publish error! The localId: $localId", e)
        }
    }

    private fun getLettuceRedisClient(
        host: String,
        port: Int,
        password: String?,
        timeout: Int,
        isSsl: Boolean
    ): RedisClient {
        val clientResources: ClientResources = DefaultClientResources.builder()
            .ioThreadPoolSize(4)
            .computationThreadPoolSize(4)
            .build()

        val redisUri = RedisURI.builder()
            .withHost(host)
            .withPort(port)
            .withPassword(password?.toCharArray())
            .withSsl(isSsl)
            .withTimeout(Duration.of(timeout.toLong(), ChronoUnit.MILLIS))
            .build()

        val clientOptions = ClientOptions.builder()
            .autoReconnect(true)
            .pingBeforeActivateConnection(true)
            .build()

        val redisClient = RedisClient.create(clientResources, redisUri)
        redisClient.options = clientOptions

        return redisClient
    }
}
