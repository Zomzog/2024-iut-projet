package iut.nantes.project.products.DTO

import iut.nantes.project.products.Entity.FamilyEntity
import java.util.*

data class FamilyDTO(
    val id: UUID?,
    val name: String,
    val description: String
) {
    fun toEntity() = FamilyEntity(id ?: UUID.randomUUID(), name, description)
}
