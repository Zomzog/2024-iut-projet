package iut.nantes.project.products

import iut.nantes.project.products.controler.FamilleDto


private fun FamilleDto.toEntity() = FamilleEntity(id,name, description)
private fun FamilleEntity.toDto(): FamilleDto = FamilleDto(id,name,description)


class DatabaseProxy(private val familleJpa: FamilleJpa) {
    fun saveFamille(famille: FamilleDto): FamilleDto {
        val familleEntity = famille.toEntity()
        val savedEntity = familleJpa.save(familleEntity)
        return savedEntity.toDto()
    }

    fun findFamilleByName(familyName: String): FamilleDto? {
        return familleJpa.findByName(familyName)?.toDto()
    }

    fun findFamilleById(id: String): FamilleDto? {
        return familleJpa.findById(id)?.toDto()
    }

    fun isValidUUID(uuid: String): Boolean {
        val uuidRegex = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$".toRegex()
        return uuidRegex.matches(uuid)
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
        return familleJpa.findAll().map { it.toDto() }
    }

    fun deleteFamilleById(familleId: String) {
        if (familleJpa.existsById(familleId)) {
            familleJpa.deleteById(familleId)
        }
    }

    fun findProductByFamilleId(id: String): FamilleDto? {
        TODO()
    }

}