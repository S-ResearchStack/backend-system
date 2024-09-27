package researchstack.backend.adapter.outgoing.trino.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import javax.sql.DataSource

@Configuration
class TrinoDataSourceConfiguration {

    @Bean
    @ConfigurationProperties("spring.datasource.secondary")
    fun trinoDataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean
    fun trinoDataSource(): DataSource {
        return trinoDataSourceProperties()
            .initializeDataSourceBuilder()
            .build()
    }

    @Bean
    fun trinoJdbcTemplate(@Qualifier("trinoDataSource") dataSource: DataSource): JdbcTemplate {
        return JdbcTemplate(dataSource)
    }

    @Bean
    fun trinoNamedParameterJdbcTemplate(@Qualifier("trinoDataSource") dataSource: DataSource): NamedParameterJdbcTemplate {
        return NamedParameterJdbcTemplate(dataSource)
    }
}
