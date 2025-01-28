package iut.nantes.project.stores.Entity

import iut.nantes.project.stores.DTO.ProductStoreDTO
import jakarta.persistence.*

@Entity
data class ProductStoreEntity(
    @Id
    val id: String,

    val name: String,

    val quantity: Int,

    @ManyToOne
    @JoinColumn(name = "store_id")
    val store: StoreEntity?
) {
    fun toDTO() = ProductStoreDTO(id, name, quantity)
}
