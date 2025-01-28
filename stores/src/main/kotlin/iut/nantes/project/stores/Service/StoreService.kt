package iut.nantes.project.stores.Service


import iut.nantes.project.products.Repository.ProductRepositoryCustom
import iut.nantes.project.stores.DTO.StoreDTO
import iut.nantes.project.stores.Entity.ProductStoreEntity
import iut.nantes.project.stores.Entity.StoreEntity
import iut.nantes.project.stores.Exception.ContactException
import iut.nantes.project.stores.Exception.StoreException
import iut.nantes.project.stores.Repository.ContactRepository
import iut.nantes.project.stores.Repository.StoreRepository
import org.springframework.stereotype.Service


@Service
class StoreService(
    private val storeRepository: StoreRepository,
    private val contactRepository: ContactRepository,
    private val productRepository : ProductRepositoryCustom
) {

    fun createStore(storeDTO: StoreDTO): StoreDTO {
        val contact = storeDTO.contact.id?.let { contactRepository.findById(it).orElseThrow { ContactException.ContactNotFoundException() } }
            ?: throw IllegalArgumentException("Le contact ne peut pas être nul avec l'id ${storeDTO.contact.id}.")

        val store = StoreEntity(
            name = storeDTO.name,
            contact = contact,
            products = mutableListOf()
        )

        storeDTO.products.forEach { productDTO ->
            val product = productRepository.findById(productDTO.id)
                .orElseThrow { IllegalArgumentException("Le produit avec l'ID ${productDTO.id} n'existe pas.") }
            if (product != null){
                val productStoreEntity = ProductStoreEntity(
                    id = productDTO.id,
                    name = productDTO.name,
                    quantity = productDTO.quantity,
                    store = store
                )

                store.products.add(productStoreEntity)
            }

        }
        val savedStore = storeRepository.save(store)
        return StoreDTO(savedStore.id, savedStore.name, contact.toDto(), emptyList())
    }


    fun getAllStores(): List<StoreDTO> {
        val stores = storeRepository.findAll().sortedBy { it.name }
        return stores.map { it.toDto() }
    }

    fun getStoreById(id: Int): StoreDTO {
        val store = storeRepository.findById(id)
            .orElseThrow { StoreException.StoreNotFoundException() }
        return store.toDto()
    }

    fun updateStore(id: Int, storeDto: StoreDTO): StoreDTO {
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

    fun deleteStore(id: Int) {
        val store = storeRepository.findById(id)
            .orElseThrow { StoreException.StoreNotFoundException() }

        if (store.products.isNotEmpty()) {
            throw StoreException.StoreHasProductsException()
        }

        storeRepository.delete(store)
    }


}
