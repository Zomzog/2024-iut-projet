package iut.nantes.project.stores.Controller

import iut.nantes.project.stores.DTO.StoreDTO
import iut.nantes.project.stores.Service.StoreService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/stores")
class StoreController(private val storeService: StoreService) {

    @PostMapping
    fun createStore(@RequestBody storeDTO: StoreDTO): StoreDTO {
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
    fun updateStore(@PathVariable id: Int, @RequestBody storeDTO: StoreDTO): StoreDTO {
        return storeService.updateStore(id, storeDTO)
    }

    @DeleteMapping("/{id}")
    fun deleteStore(@PathVariable id: Int) {
        storeService.deleteStore(id)
    }
}
