package iut.nantes.project.stores.Service

import iut.nantes.project.products.Repository.ProductRepository
import iut.nantes.project.products.DTO.ProductDTO
import iut.nantes.project.products.Exception.ProductException
import iut.nantes.project.stores.DTO.StoreDTO
import iut.nantes.project.stores.Entity.StoreEntity
import iut.nantes.project.stores.Exception.ContactException
import iut.nantes.project.stores.Exception.StoreException
import iut.nantes.project.stores.Repository.ContactRepository
import iut.nantes.project.stores.Repository.StoreRepository
import org.springframework.stereotype.Service
import java.util.*


@Service
class StoreService(
    private val storeRepository: StoreRepository,
    private val contactRepository: ContactRepository,
    private val productRepository: ProductRepository
) {

    fun createStore(storeDto: StoreDTO): StoreDTO {
        val contactId = storeDto.contact.id ?: throw ContactException.InvalidIdFormatException()
        val contact = contactRepository.findById(contactId)
            .orElseThrow { ContactException.ContactNotFoundException() }
        val lastStore = storeRepository.findTopByOrderByIdDesc()
        val newId = lastStore.id?.plus(1) ?: 1L

        val store = StoreEntity(
            id = newId,
            name = storeDto.name,
            contact = contact,
            products = mutableListOf()
        )

        storeRepository.save(store)

        return store.toDto()
    }


    fun getAllStores(): List<StoreDTO> {
        val stores = storeRepository.findAll().sortedBy { it.name }
        return stores.map { it.toDto() }
    }

    fun getStoreById(id: Long): StoreDTO {
        val store = storeRepository.findById(id)
            .orElseThrow { StoreException.StoreNotFoundException() }
        return store.toDto()
    }

    fun updateStore(id: Long, storeDto: StoreDTO): StoreDTO {
        val store = storeRepository.findById(id)
            .orElseThrow { StoreException.StoreNotFoundException() }

        val contactId = storeDto.contact.id ?: throw ContactException.InvalidIdFormatException()
        val contact = contactRepository.findById(contactId)
            .orElseThrow { ContactException.ContactNotFoundException() }

        store.name = storeDto.name
        store.contact = contact

        storeRepository.save(store)

        return store.toDto()
    }

    fun deleteStore(id: Long) {
        val store = storeRepository.findById(id)
            .orElseThrow { StoreException.StoreNotFoundException() }

        // Si le magasin a des produits, on ne peut pas le supprimer
        if (store.products.isNotEmpty()) {
            throw StoreException.StoreHasProductsException()
        }

        storeRepository.delete(store)
    }

    fun addProductsToStore(storeId: Long, products: List<ProductDTO>) {
        val store = storeRepository.findById(storeId)
            .orElseThrow { StoreException.StoreNotFoundException() }

        val productEntities = products.map {
            val product = productRepository.findById(it.id.toString())
                .orElseThrow { ProductException.ProductNotFoundException() }
            product
        }

        // Ajoute les produits au magasin
        store.products.addAll(productEntities)
        storeRepository.save(store)
    }

    fun removeProductsFromStore(storeId: Long, productIds: List<UUID>) {
        val store = storeRepository.findById(storeId)
            .orElseThrow { StoreException.StoreNotFoundException() }

        store.products.removeIf { product -> productIds.contains(product.id) }
        storeRepository.save(store)
    }
}
