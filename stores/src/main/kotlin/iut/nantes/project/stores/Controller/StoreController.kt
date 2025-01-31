package iut.nantes.project.stores.Controller

import iut.nantes.project.stores.DTO.ProductStoreDTO
import iut.nantes.project.stores.DTO.StoreDTO
import iut.nantes.project.stores.Service.StoreService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/v1/stores")
class StoreController(private val storeService: StoreService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createStore(@RequestBody @Valid storeDTO: StoreDTO): StoreDTO {
        return storeService.createStore(storeDTO)
    }

    @GetMapping
    fun getAllStores(): List<StoreDTO> {
        return storeService.getAllStores()
    }

    @GetMapping("/{id}")
    fun getStoreById(@PathVariable id: Int): StoreDTO {
        return storeService.getStoreById(id)
    }

    @PutMapping("/{id}")
    fun updateStore(@PathVariable id: Int, @RequestBody @Valid storeDTO: StoreDTO): StoreDTO {
        return storeService.updateStore(id, storeDTO)
    }

    @DeleteMapping("/{id}")
    fun deleteStore(@PathVariable id: Int) {
        storeService.deleteStore(id)
    }

    @PostMapping("/{storeId}/products/{productId}/add")
    fun addProductToStore(
        @PathVariable storeId: Int,
        @PathVariable productId: UUID,
        @RequestParam(required = false) quantity: Int?
    ): ProductStoreDTO {
        return storeService.addProductToStore(storeId, productId, quantity)
    }

    @PostMapping("/{storeId}/products/{productId}/remove")
    fun removeProductFromStore(
        @PathVariable storeId: Int,
        @PathVariable productId: UUID,
        @RequestParam(required = false) quantity: Int?
    ): ProductStoreDTO {
        return storeService.removeProductFromStore(storeId, productId, quantity)
    }

    @DeleteMapping("/{storeId}/products")
    fun deleteProductsFromStore(
        @PathVariable storeId: Int,
        @RequestBody productIds: List<String>
    ) {
        storeService.deleteProductsFromStore(storeId, productIds)
    }
}
