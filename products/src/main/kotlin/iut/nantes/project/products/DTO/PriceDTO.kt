package iut.nantes.project.products.DTO

import iut.nantes.project.products.Entity.PriceEntity

data class PriceDTO(
    val amount: Double,
    val currency: String
) {
    fun toEntity() = PriceEntity(amount, currency)

}
