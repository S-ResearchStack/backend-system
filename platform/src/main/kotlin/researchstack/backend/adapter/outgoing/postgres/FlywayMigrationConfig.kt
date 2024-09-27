package researchstack.backend.adapter.outgoing.postgres

import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class FlywayMigrationConfig {
    @Bean(initMethod = "migrate")
    fun flyway(@Qualifier("postgresDataSource") dataSource: DataSource): Flyway = Flyway(
        Flyway.configure()
            .baselineOnMigrate(true)
            .dataSource(dataSource)
    )
}
