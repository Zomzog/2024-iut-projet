package iut.nantes.project.products.Entity

import iut.nantes.project.products.DTO.PriceDTO
import jakarta.persistence.Embeddable

@Embeddable
data class PriceEntity(
    var amount: Double,
    var currency: String
) {
    fun toDto() = PriceDTO(amount, currency)
}
