package iut.nantes.project.stores.Controller

import iut.nantes.project.stores.DTO.StoreDTO
import iut.nantes.project.stores.Service.StoreService
import jakarta.persistence.EntityNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/stores")
class StoreController(private val storeService: StoreService) {

    @PostMapping
    fun createStore(@RequestBody store: StoreDTO): ResponseEntity<StoreDTO> {
        return try {
            val createdStore = storeService.createStore(store)
            ResponseEntity(createdStore, HttpStatus.CREATED)
        } catch (e: Exception) {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping
    fun getAllStores(): ResponseEntity<List<StoreDTO>> {
        val stores = storeService.getAllStores()
        return ResponseEntity(stores, HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun getStoreById(@PathVariable id: Long): ResponseEntity<StoreDTO> {
        return try {
            val store = storeService.getStoreById(id)
            ResponseEntity(store, HttpStatus.OK)
        } catch (e: EntityNotFoundException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @PutMapping("/{id}")
    fun updateStore(@PathVariable id: Long, @RequestBody store: StoreDTO): ResponseEntity<StoreDTO> {
        return try {
            val updatedStore = storeService.updateStore(id, store)
            ResponseEntity(updatedStore, HttpStatus.OK)
        } catch (e: EntityNotFoundException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @DeleteMapping("/{id}")
    fun deleteStore(@PathVariable id: Long): ResponseEntity<Void> {
        return try {
            storeService.deleteStore(id)
            ResponseEntity(HttpStatus.NO_CONTENT)
        } catch (e: EntityNotFoundException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
}
