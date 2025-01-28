package iut.nantes.project.stores.DTO

import iut.nantes.project.stores.Entity.ProductStoreEntity

data class ProductStoreDTO(
    val id: String,
    val name: String,
    val quantity: Int,
) {
    fun toEntity() = ProductStoreEntity(id, name, quantity, null)
}
