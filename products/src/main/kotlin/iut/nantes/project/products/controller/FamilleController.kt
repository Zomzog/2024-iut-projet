package iut.nantes.project.products.controller

import iut.nantes.project.products.DatabaseProxy
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class FamilleController(val db: DatabaseProxy){
    @PostMapping("/api/v1/families")
    fun createFamille(@RequestBody famille: FamilleDto): ResponseEntity<Any> {
        val result = db.saveFamille(famille)
        return if (result != null) {
            ResponseEntity.status(HttpStatus.CREATED).body(result)
        } else {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Données de produit invalides")
        }
    }

    @GetMapping("/api/v1/families")
    fun getFamilles(): ResponseEntity<List<FamilleDto>> {
        val result = db.findAllFamilles()
        return ResponseEntity.ok(result)
    }

    @GetMapping("/api/v1/families/{id}")
    fun getFamilleById(@PathVariable id: UUID): ResponseEntity<Any> {
        val result = db.findFamilleById(id)
        return if (result != null){
            ResponseEntity.status(HttpStatus.OK).body(result)
        }else{
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Données de famille invalides")
        }
    }

    @PutMapping("/api/v1/families/{id}")
    fun putFamille(@PathVariable id: UUID, @RequestBody famille: FamilleDto): ResponseEntity<Any> {
        val updatedFamille = db.updateFamille(id, famille)

        return if (updatedFamille != null) {
            ResponseEntity.status(HttpStatus.OK).body(updatedFamille)
        } else {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La mise à jour a échoué. Vérifiez les données.")
        }
    }


    @DeleteMapping("/api/v1/families/{id}")
    fun deleteFamille(@PathVariable id: UUID): ResponseEntity<Any> {
        val result = db.findFamilleById(id)
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La famille n'existe pas")
        }

        if (db.findProductByFamilleId(id).isNotEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Au moins 1 produit de cette famille existe encore")
        }

        db.deleteFamilleById(id)

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("La famille a été supprimée avec succès")
    }
}