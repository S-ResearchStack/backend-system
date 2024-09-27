package researchstack.backend.adapter.outgoing.casbin.watcher

import io.lettuce.core.RedisClient
import io.lettuce.core.pubsub.RedisPubSubListener
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

class LettuceSubThread(
    private val redisClient: RedisClient,
    private val channel: String
) : Thread("LettuceSubThread") {
    private val lettuceSubscriber = LettuceSubscriber()
    private lateinit var statefulRedisPubSubConnection: StatefulRedisPubSubConnection<String, String>

    fun setUpdateCallback(runnable: Runnable) {
        lettuceSubscriber.setUpdateCallback(runnable)
    }

    fun setUpdateCallback(consumer: Consumer<String>) {
        lettuceSubscriber.setUpdateCallback(consumer)
    }

    override fun run() {
        try {
            statefulRedisPubSubConnection = redisClient.connectPubSub()
            if (statefulRedisPubSubConnection.isOpen) {
                statefulRedisPubSubConnection.addListener(object : RedisPubSubListener<String, String> {
                    override fun unsubscribed(channel: String, count: Long) {
                        logger.info("[unsubscribed] $channel")
                    }

                    override fun subscribed(channel: String, count: Long) {
                        logger.info("[subscribed] $channel")
                    }

                    override fun punsubscribed(pattern: String, count: Long) {
                        logger.info("[punsubscribed] $pattern")
                    }

                    override fun psubscribed(pattern: String, count: Long) {
                        logger.info("[psubscribed] $pattern")
                    }

                    override fun message(pattern: String, channel: String, message: String) {
                        logger.info("[message] $pattern -> $channel -> $message")
                        lettuceSubscriber.onMessage(channel, message)
                    }

                    override fun message(channel: String, message: String) {
                        logger.info("[message] $channel -> $message")
                        lettuceSubscriber.onMessage(channel, message)
                    }
                })
                statefulRedisPubSubConnection.async().subscribe(channel)
                    .get(500, TimeUnit.MILLISECONDS)
            }
        } catch (e: Exception) {
            logger.error("error message ${e.message}")
            if (this::statefulRedisPubSubConnection.isInitialized) {
                close(statefulRedisPubSubConnection)
            }
        }
    }

    private fun close(statefulRedisPubSubConnection: StatefulRedisPubSubConnection<String, String>) {
        if (statefulRedisPubSubConnection.isOpen) {
            statefulRedisPubSubConnection.closeAsync()
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(LettuceSubThread::class.java)
    }
}
