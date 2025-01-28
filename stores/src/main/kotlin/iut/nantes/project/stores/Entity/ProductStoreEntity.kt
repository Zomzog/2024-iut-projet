package iut.nantes.project.stores.Entity

import iut.nantes.project.stores.DTO.ProductStoreDTO
import jakarta.persistence.*
import java.util.*

@Entity
data class ProductStoreEntity(
    @Id
    val id: UUID,

    val name: String,

    var quantity: Int,

    @ManyToOne
    @JoinColumn(name = "store_id")
    val store: StoreEntity?
) {
    fun toDTO() = ProductStoreDTO(id, name, quantity)
}
