package iut.nantes.project.products.Controller

import iut.nantes.project.products.DTO.FamilyDTO
import iut.nantes.project.products.FamilyException
import iut.nantes.project.products.Service.FamilyService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class FamilyController(private val familyService: FamilyService) {

    @PostMapping("/api/v1/families")
    fun createFamily(@RequestBody familyDto: FamilyDTO): ResponseEntity<FamilyDTO> {
        return try {
            val createdFamily = familyService.createFamily(familyDto)
            ResponseEntity.status(HttpStatus.CREATED).body(createdFamily)
        } catch (e: FamilyException.InvalidDataException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        } catch (e: FamilyException.NameConflictException) {
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
    }

    @GetMapping("/api/v1/families")
    fun getAllFamilies(): ResponseEntity<List<FamilyDTO>> {
        val families = familyService.getAllFamilies()
        return ResponseEntity.ok(families)
    }

    @GetMapping("/api/v1/families/{id}")
    fun getFamilyById(@PathVariable id: String): ResponseEntity<FamilyDTO> {
        return try {
            val family = familyService.getFamilyById(id)
            ResponseEntity.ok(family)
        } catch (e: FamilyException.InvalidIdFormatException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        } catch (e: FamilyException.FamilyNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @PutMapping("/api/v1/families/{id}")
    fun updateFamily(@PathVariable id: String, @RequestBody familyDto: FamilyDTO): ResponseEntity<FamilyDTO> {
        return try {
            val updatedFamily = familyService.updateFamily(id, familyDto)
            ResponseEntity.ok(updatedFamily)
        } catch (e: FamilyException.InvalidDataException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        } catch (e: FamilyException.NameConflictException) {
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
    }

    @DeleteMapping("/api/v1/families/{id}")
    fun deleteFamily(@PathVariable id: String): ResponseEntity<Void> {
        return try {
            familyService.deleteFamily(id)
            ResponseEntity.status(HttpStatus.NO_CONTENT).build()
        } catch (e: FamilyException.FamilyNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        } catch (e: FamilyException.FamilyHasProductsException) {
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
    }
}
