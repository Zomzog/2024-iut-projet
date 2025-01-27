package iut.nantes.project.stores.Config

import iut.nantes.project.products.Repository.ProductRepository
import iut.nantes.project.stores.Repository.ContactRepository
import iut.nantes.project.stores.Repository.StoreRepository
import iut.nantes.project.stores.Service.StoreService
import org.springframework.context.annotation.Bean

class AppConfig {

    @Bean
    fun storeService(storeRepository: StoreRepository, contactRepository: ContactRepository, productRepository: ProductRepository): StoreService {
        return StoreService(storeRepository, contactRepository, productRepository )
    }
}

