package iut.nantes.project.products

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.env.Environment
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
class AppConfig(
    private val familleJpa: FamilleJpa,
    private val productJpa: ProductJpa,
    private val env: Environment
) {

    @Bean
    fun productRepository(): ProductRepository {
        return if (env.activeProfiles.contains("dev")) {
            InMemoryProductRepository()
        } else {
            productJpa
        }
    }

    @Bean
    fun familleRepository(): FamilleRepository {
        return if (env.activeProfiles.contains("dev")) {
            InMemoryFamilleRepository()
        } else {
            familleJpa
        }
    }

    @Bean
    fun databaseProxy(): DatabaseProxy {
        return DatabaseProxy(familleRepository(), productRepository())
    }

    @Bean
    @Profile("dev")
    fun inMemoryProductRepository(): ProductRepository {
        return InMemoryProductRepository()
    }

    @Bean
    @Profile("!dev")
    fun jpaProductRepository(jpaRepo: ProductJpa): ProductRepository {
        return jpaRepo
    }

    @Bean
    @Profile("dev")
    fun inMemoryFamilleRepository(): FamilleRepository {
        return InMemoryFamilleRepository()
    }

    @Bean
    @Profile("!dev")
    fun jpaFamilleRepository(jpaRepo: FamilleJpa): FamilleRepository {
        return jpaRepo
    }
}

