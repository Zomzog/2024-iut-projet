package iut.nantes.project.products.DTO

import iut.nantes.project.products.Entity.PriceEntity
import iut.nantes.project.products.Entity.ProductEntity
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import java.util.*

data class ProductDTO(
    val id: UUID?,

    @field:Size(min = 2, max = 20, message = "Le nom doit faire entre 2 et 20 caractères.")
    val name: String,

    @field:Size(min = 5, max = 100, message = "La description doit faire entre 5 et 100 caractères.")
    val description: String?,

    @field:Valid
    val price: PriceDTO,

    @field:NotNull(message = "La famille est obligatoire.")
    val family: FamilyDTO
) {
    fun toEntity() = ProductEntity(id ?: UUID.randomUUID(), name, description, price.toEntity(), family.toEntity())

}

data class PriceDTO(
    @field:Positive(message = "Le montant doit être positif.")
    val amount: Double,
    @field:Pattern(
        regexp = "^[A-Z]{3}$",
        message = "La devise doit comporter 3 lettres majuscules (ex : EUR)."
    )
    val currency: String
) {
    fun toEntity() = PriceEntity(amount, currency)

}
