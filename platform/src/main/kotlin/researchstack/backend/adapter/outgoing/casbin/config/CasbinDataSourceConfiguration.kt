package researchstack.backend.adapter.outgoing.casbin.config

import org.casbin.annotation.CasbinDataSource
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import javax.sql.DataSource

@Configuration
class CasbinDataSourceConfiguration {
    @Bean
    @ConfigurationProperties("casbin.datasource")
    @Primary
    fun casbinDataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean
    @CasbinDataSource
    fun casbinDataSource(): DataSource {
        return casbinDataSourceProperties()
            .initializeDataSourceBuilder()
            .build()
    }
}
