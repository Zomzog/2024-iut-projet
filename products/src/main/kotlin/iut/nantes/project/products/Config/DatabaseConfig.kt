package iut.nantes.project.products.Config

import iut.nantes.project.products.Entity.ProductEntity
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import javax.sql.DataSource

@Configuration
class DatabaseConfig {

    @Bean
    @Profile("default")
    fun dataSource(): DataSource {
        return EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .build()
    }

    @Bean
    @Profile("dev")
    fun productMap(): MutableMap<String, ProductEntity> {
        return HashMap()
    }
}
