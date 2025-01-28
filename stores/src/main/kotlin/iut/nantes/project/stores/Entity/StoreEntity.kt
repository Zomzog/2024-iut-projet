package iut.nantes.project.stores.Entity

import iut.nantes.project.stores.DTO.StoreDTO
import jakarta.persistence.*
import jakarta.validation.constraints.Size

@Entity
data class StoreEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @field:Size(min = 3, max = 30, message = "Le nom du magasin doit être entre 3 et 30 caractères.")
    var name: String,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "contact_id", referencedColumnName = "id")
    var contact: ContactEntity,

    @OneToMany(mappedBy = "store", cascade = [CascadeType.ALL], orphanRemoval = true)
    val products: MutableList<ProductStoreEntity> = mutableListOf()
) {
    constructor() : this(name = "", contact = ContactEntity(), products = mutableListOf())

    fun toDto() = StoreDTO(id, name, contact.toDto(), products.map { it.toDTO() }.toList())
}
