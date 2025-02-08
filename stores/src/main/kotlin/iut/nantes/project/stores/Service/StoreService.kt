package iut.nantes.project.stores.Service


import iut.nantes.project.products.Exception.ProductException
import iut.nantes.project.stores.DTO.ProductStoreDTO
import iut.nantes.project.stores.DTO.StoreDTO
import iut.nantes.project.stores.Entity.ProductStoreEntity
import iut.nantes.project.stores.Entity.StoreEntity
import iut.nantes.project.stores.Exception.ContactException
import iut.nantes.project.stores.Exception.StoreException
import iut.nantes.project.stores.Repository.ContactRepository
import iut.nantes.project.stores.Repository.StoreRepository
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClientResponseException
import java.util.UUID


@Service
class StoreService(
    private val storeRepository: StoreRepository,
    private val contactRepository: ContactRepository,
    private val webClientServiceProduct : WebClientService
) {

    fun createStore(storeDTO: StoreDTO): StoreDTO {
        val contact = storeDTO.contact.id?.let { contactRepository.findById(it).orElseThrow { ContactException.ContactNotFoundException() } }
            ?: throw IllegalArgumentException("Le contact ne peut pas être nul avec l'id ${storeDTO.contact.id}.")

        val store = StoreEntity(
            name = storeDTO.name,
            contact = contact,
            products = mutableListOf()
        )


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


    fun removeProductFromStore(storeId: Int, productId: UUID, quantity: Int): ProductStoreDTO {
        val store = storeRepository.findById(storeId)
            .orElseThrow { StoreException.StoreNotFoundException() }

        val productInStore = store.products.find { it.id == productId }
            ?: throw ProductException.ProductNotFoundException()

        if (quantity <= 0) throw IllegalArgumentException("La quantité doit être positive.")

        if (productInStore.quantity < quantity) {
            throw IllegalStateException("Le stock final ne peut pas être inférieur à zéro.")
        }

        productInStore.quantity -= quantity
        storeRepository.save(store)
        return productInStore.toDTO()
    }


    fun addProductToStore(storeId: Int, productId: UUID, quantity: Int): ProductStoreDTO {
        val store = storeRepository.findById(storeId)
            .orElseThrow { StoreException.StoreNotFoundException() }

        val product = try {
            webClientServiceProduct.getProductInfo(productId.toString())
        } catch (e: ProductException.ProductNotFoundException) {
            throw StoreException.InvalidDataException()
        }


        if (quantity <= 0) throw IllegalArgumentException("La quantité doit être positive.")

        val productInStore = store.products.find { it.id == productId }

        if (productInStore != null) {
            productInStore.quantity += quantity
        } else {
            val newProductStoreEntity = ProductStoreEntity(
                id = product.id!!,
                name = product.name,
                quantity = quantity,
                store = store
            )
            store.products.add(newProductStoreEntity)
        }

        storeRepository.save(store)
        return store.products.find { it.id == productId }!!.toDTO()
    }

    fun deleteProductsFromStore(storeId: Int, productIds: List<UUID>) {
        val store = storeRepository.findById(storeId)
            .orElseThrow { StoreException.StoreNotFoundException() }

        if (productIds.distinct().size != productIds.size) {
            throw IllegalArgumentException("La liste contient des produits en double.")
        }

        productIds.forEach { productId ->
            val productInStore = store.products.find { it.id == productId }
            if (productInStore != null) {
                store.products.remove(productInStore)
            }
        }

        storeRepository.save(store)
    }


}
