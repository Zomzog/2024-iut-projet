package iut.nantes.project.products.DTO

import iut.nantes.project.products.Entity.FamilyEntity
import jakarta.validation.constraints.Size
import java.util.*

data class FamilyDTO(
    val id: UUID?,

    @field:Size(min = 3, max = 30, message = "Le nom de la famille doit faire entre 3 et 30 caractères.")
    val name: String,

    @field:Size(min = 5, max = 100, message = "La description doit faire entre 5 et 100 caractères.")
    val description: String
) {
    fun toEntity() = FamilyEntity(id ?: UUID.randomUUID(), name, description)
}
