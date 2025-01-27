package iut.nantes.project.products.Config

import iut.nantes.project.products.Entity.FamilyEntity
import iut.nantes.project.products.ProductService
import iut.nantes.project.products.Repository.*
import iut.nantes.project.products.Service.FamilyService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

@Configuration
class AppConfig {

    @Bean
    @Profile("!dev")
    fun familyRepositoryJPA(jpaRepository: FamilyJpaRepository): FamilyRepositoryCustom {
        return FamilyRepositoryJPA(jpaRepository)
    }

    @Bean
    @Profile("dev")
    fun familyRepositoryInMemory(): FamilyRepositoryCustom {
        return FamilyRepositoryInMemory()
    }

    @Bean
    fun familyService(familyRepository: FamilyRepositoryCustom): FamilyService {
        return FamilyService(familyRepository)
    }

    @Bean
    fun productService(productRepository: ProductRepository, familyRepository: FamilyRepositoryCustom): ProductService {
        return ProductService(productRepository, familyRepository)
    }
}
