package iut.nantes.project.products.Config

import iut.nantes.project.products.ProductService
import iut.nantes.project.products.Repository.FamilyRepository
import iut.nantes.project.products.Repository.ProductRepository
import iut.nantes.project.products.Service.FamilyService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {

    @Bean
    fun familyService(familyRepository: FamilyRepository): FamilyService {
        return FamilyService(familyRepository)
    }

    @Bean
    fun productService(productRepository: ProductRepository, familyRepository: FamilyRepository): ProductService {
        return ProductService(productRepository, familyRepository)
    }
}
