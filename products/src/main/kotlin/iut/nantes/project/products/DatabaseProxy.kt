package iut.nantes.project.products

import iut.nantes.project.products.controler.FamilleDto
import org.springframework.http.ResponseEntity

class DatabaseProxy {
    fun saveFamille(famille: FamilleDto): FamilleDto {
        TODO()
    }

    fun findFamilleByName(familyName: String): Boolean {
        TODO()
    }

    fun validateFamille(famille: FamilleDto): List<String> {
        val errors = mutableListOf<String>()

        if (famille.name.isBlank() || famille.name.length !in 3..30) {
            errors.add("Le nom doit contenir entre 3 et 30 caractères.")
        }

        if (famille.description.isBlank() || famille.description.length !in 5..100) {
            errors.add("La description doit contenir entre 5 et 100 caractères.")
        }

        return errors
    }

    fun findAllFamilles(): List<FamilleDto> {
        TODO()
    }
}