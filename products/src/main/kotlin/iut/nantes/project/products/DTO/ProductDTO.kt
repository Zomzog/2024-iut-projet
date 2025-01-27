package iut.nantes.project.products.DTO

import iut.nantes.project.products.Entity.PriceEntity
import iut.nantes.project.products.Entity.ProductEntity
import java.util.*

data class ProductDTO(
    val id: UUID?,
    val name: String,
    val description: String?,
    val price: PriceDTO,
    val family: FamilyDTO
) {
    fun toEntity() = ProductEntity(id ?: UUID.randomUUID(), name, description, price.toEntity(), family.toEntity())

}

data class PriceDTO(
    val amount: Double,
    val currency: String
) {
    fun toEntity() = PriceEntity(amount, currency)

}
