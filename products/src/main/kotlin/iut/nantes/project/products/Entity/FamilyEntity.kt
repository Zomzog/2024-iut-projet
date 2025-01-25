package iut.nantes.project.products.Entity

import iut.nantes.project.products.DTO.FamilyDTO
import jakarta.persistence.*
import java.util.*

@Entity
data class FamilyEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID,
    var name: String,
    var description: String,

    @OneToMany(mappedBy = "family", cascade = [CascadeType.ALL])
    val products: List<ProductEntity> = mutableListOf()
) {
    constructor() : this(UUID.randomUUID(), "", "", mutableListOf())

    fun toDto() = FamilyDTO(id, name, description)
}