package iut.nantes.project.products.Controller

import iut.nantes.project.products.DTO.FamilyDTO
import iut.nantes.project.products.Exception.FamilyException
import iut.nantes.project.products.Service.FamilyService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1")

class FamilyController(private val familyService: FamilyService) {

    @PostMapping("/families")
    fun createFamily(@RequestBody @Valid familyDto: FamilyDTO): ResponseEntity<FamilyDTO> {
        return try {
            val createdFamily = familyService.createFamily(familyDto)
            ResponseEntity.status(HttpStatus.CREATED).body(createdFamily)
        } catch (e: FamilyException.InvalidDataException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        } catch (e: FamilyException.NameConflictException) {
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
    }

    @GetMapping("/families")
    fun getAllFamilies(): ResponseEntity<List<FamilyDTO>> {
        val families = familyService.getAllFamilies()
        return ResponseEntity.ok(families)
    }

    @GetMapping("/families/{id}")
    fun getFamilyById(@PathVariable id: UUID): ResponseEntity<FamilyDTO> {
        return try {
            val family = familyService.getFamilyById(id)
            ResponseEntity.ok(family)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        } catch (e: FamilyException.FamilyNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }


    @PutMapping("/families/{id}")
    fun updateFamily(@PathVariable id: UUID, @RequestBody familyDto: FamilyDTO): ResponseEntity<FamilyDTO> {
        return try {
            val updatedFamily = familyService.updateFamily(id, familyDto)
            ResponseEntity.ok(updatedFamily)
        } catch (e: FamilyException.InvalidDataException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        } catch (e: FamilyException.NameConflictException) {
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
    }

    @DeleteMapping("/families/{id}")
    fun deleteFamily(@PathVariable id: UUID): ResponseEntity<Void> {
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
