package researchstack.backend.adapter.outgoing.casbin.watcher

import java.util.function.Consumer

class LettuceSubscriber {
    private lateinit var runnable: Runnable
    private var consumer: Consumer<String>? = null

    fun setUpdateCallback(runnable: Runnable) {
        this.runnable = runnable
    }

    fun setUpdateCallback(consumer: Consumer<String>) {
        this.consumer = consumer
    }

    fun onMessage(channel: String, message: String) {
        runnable.run()
        consumer?.accept("Channel: $channel Message: $message")
    }
}
