package researchstack.backend.adapter.outgoing.casbin.config

import org.casbin.jcasbin.main.Enforcer
import org.casbin.jcasbin.persist.Watcher
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import researchstack.backend.adapter.outgoing.casbin.watcher.LettuceRedisWatcher
import researchstack.backend.config.CasbinProperties

@Configuration
class CasbinRedisWatcherConfiguration {
    @Bean
    fun lettuceRedisWatcher(
        redisProperties: RedisProperties,
        casbinProperties: CasbinProperties,
        enforcer: Enforcer
    ): Watcher {
        val lettuceRedisWatcher = LettuceRedisWatcher(
            redisProperties.host,
            redisProperties.port,
            casbinProperties.policyTopic,
            redisProperties.timeout?.toMillis()?.toInt() ?: 2000,
            redisProperties.password,
            redisProperties.ssl.isEnabled
        )
        enforcer.setWatcher(lettuceRedisWatcher)

        return lettuceRedisWatcher
    }
}
