package iut.nantes.project.products.controler

import iut.nantes.project.products.DatabaseProxy
import iut.nantes.project.products.FamilleJpa
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody

class FamilleController(val db: DatabaseProxy){


    @PostMapping("/api/v1/families")
    fun createFamille(@RequestBody famille: FamilleDto): ResponseEntity<Any> {
        val violations = db.validateFamille(famille)
        if (violations.isNotEmpty()) {
            return ResponseEntity.badRequest().body(violations)
        }

        if (db.findFamilleByName(famille.name) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Une famille avec le nom '${famille.name}' existe déjà.")
        }

        db.saveFamille(famille).let { return ResponseEntity.status(HttpStatus.CREATED).body(it) }
    }

    @GetMapping("/api/v1/families")
    fun getFamilles(): ResponseEntity<List<FamilleDto>> {
        val result = db.findAllFamilles()
        return ResponseEntity.ok(result)
    }

    @GetMapping("/api/v1/families/{id}")
    fun getFamilleById(@PathVariable id: String): ResponseEntity<Any> {
        if (!(db.isValidUUID(id))){
            return ResponseEntity.badRequest().body("Format d'id incorrect")
        }
        val result = db.findFamilleById(id)
        return if (result != null){
            ResponseEntity.status(HttpStatus.OK).body(result)
        }else{
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("La famille n'existe pas")
        }
    }

    @PutMapping("/api/v1/families/{id}")
    fun putFamille(@PathVariable id: String, @RequestBody famille: FamilleDto): ResponseEntity<Any> {
        if (id != famille.id){
            return ResponseEntity.badRequest().body("Les IDs de famille ne correspondent pas")
        }
        val previous = db.findFamilleById(id) ?: return ResponseEntity.badRequest().body("La famille n'existe pas")
        val violations = db.validateFamille(famille)
        if (violations.isNotEmpty()) {
            return ResponseEntity.badRequest().body(violations)
        }
        if (db.findFamilleByName(famille.name) != null){
             return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Une famille avec le nom '${famille.name}' existe déjà.")
        }
        db.saveFamille(famille).let { return ResponseEntity.status(HttpStatus.CREATED).body(it) }
    }

    @DeleteMapping("/api/v1/families/{id}")
    fun deleteFamille(@PathVariable id: String): ResponseEntity<Any> {
        val result = db.findFamilleById(id)
        if (result == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La famille n'existe pas")
        }
        if (db.findProductByFamilleId(id) != null){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Au moins 1 produit de cette famille existe encore")
        }
        db.deleteFamilleById(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("La famille a été supprimée avec succès")
    }
}