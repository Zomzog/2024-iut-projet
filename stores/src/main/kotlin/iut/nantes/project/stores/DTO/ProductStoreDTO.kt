package iut.nantes.project.stores.DTO

import iut.nantes.project.stores.Entity.ProductStoreEntity
import java.util.*


data class ProductStoreDTO(
    val id: UUID,
    val name: String,
    val quantity: Int,
) {
    fun toEntity() = ProductStoreEntity(id, name, quantity, null)
}
