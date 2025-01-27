package iut.nantes.project.stores.Entity
import iut.nantes.project.products.Entity.ProductEntity
import iut.nantes.project.stores.DTO.StoreDTO
import jakarta.persistence.*

@Entity
data class StoreEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var name: String,

    @ManyToOne
    var contact: ContactEntity,

    @OneToMany
    val products: MutableList<ProductEntity> = mutableListOf()
) {
    fun toDto() = StoreDTO(id, name, contact.toDto(), products.map { it.toDto() })
}

