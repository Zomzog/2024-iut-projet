package iut.nantes.project.products.controler

import iut.nantes.project.products.DatabaseProxy
import iut.nantes.project.products.FamilleJpa
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody

class FamilleController(val db: DatabaseProxy){


    @PostMapping("/api/v1/families")
    fun createFamille(@RequestBody famille: FamilleDto): ResponseEntity<Any> {
        val violations = db.validateFamille(famille)
        if (violations.isNotEmpty()) {
            return ResponseEntity.badRequest().body(violations)
        }

        if (db.findFamilleByName(famille.name)) {
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

}