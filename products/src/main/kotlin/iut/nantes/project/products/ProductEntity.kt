package iut.nantes.project.products

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Pattern
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ProductRepository {
    fun save(product: ProductsEntity): ProductsEntity
    fun findById(id: UUID): ProductsEntity?
    fun findAll(): List<ProductsEntity>
    fun deleteById(id: UUID)
    fun findByFamilyId(id: UUID): List<ProductsEntity>
}

@Profile("!dev")
interface ProductJpa: JpaRepository<ProductsEntity, Int>, ProductRepository {
}


@Profile("dev")
class InMemoryProductRepository : ProductRepository {
    private val products = mutableMapOf<UUID, ProductsEntity>()

    override fun save(product: ProductsEntity): ProductsEntity {
        products[product.id] = product
        return product
    }

    override fun findById(id: UUID): ProductsEntity? {
        return products[id]
    }

    override fun findAll(): List<ProductsEntity> {
        return products.values.toList()
    }

    override fun deleteById(id: UUID) {
        products.remove(id)
    }

    override fun findByFamilyId(id: UUID): List<ProductsEntity> {
        return products.values.filter { it.family.id == id }
    }
}


@Entity
@Table(name = "PRODUCTS")
data class ProductsEntity(
    @Id
    @GeneratedValue
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    @field:NotBlank(message = "Le nom ne peut pas être vide.")
    @field:Size(min = 2, max = 20, message = "Le nom doit faire entre 2 et 20 caractères.")
    val name: String,

    @Column(nullable = true)
    @field:Size(min = 5, max = 100, message = "La description doit faire entre 5 et 100 caractères.")
    val description: String? = null,

    @Embedded
    val price: Price,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id", nullable = false)
    val family: FamilleEntity
)

@Embeddable
data class Price(

    @field:Positive(message = "Le montant doit être positif.")
    val amount: Double,

    @field:Pattern(regexp = "^[A-Z]{3}$", message = "La devise doit être en 3 lettres majuscules.")
    val currency: String
)