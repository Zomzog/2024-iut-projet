package iut.nantes.project.products

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID


@Profile("!dev")
interface FamilleJpa: JpaRepository<FamilleEntity, Int>, FamilleRepository {
}

@Profile("dev")
class InMemoryFamilleRepository : FamilleRepository {
    private val familles = mutableMapOf<UUID, FamilleEntity>()

    override fun save(famille: FamilleEntity): FamilleEntity {
        familles[famille.id] = famille
        return famille
    }

    override fun findById(id: UUID): FamilleEntity? {
        return familles[id]
    }

    override fun findAll(): List<FamilleEntity> {
        return familles.values.toList()
    }

    override fun findByName(name: String): FamilleEntity? {
        return familles.values.find { it.name == name }
    }

    override fun deleteById(id: UUID) {
        familles.remove(id)
    }

    override fun existsById(id: UUID): Boolean {
        return familles.containsKey(id)
    }
}

interface FamilleRepository {
    fun save(famille: FamilleEntity): FamilleEntity
    fun findById(id: UUID): FamilleEntity?
    fun findAll(): List<FamilleEntity>
    fun findByName(name: String): FamilleEntity?
    fun deleteById(id: UUID)
    fun existsById(id: UUID): Boolean
}

@Entity
@Table(name = "FAMILLES", uniqueConstraints = [UniqueConstraint(columnNames = ["name"])])
data class FamilleEntity(
    @Id
    @GeneratedValue
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false, unique = true)
    @field:NotBlank(message = "Le nom ne peut pas être vide.")
    @field:Size(min = 3, max = 30, message = "Le nom doit contenir entre 3 et 30 caractères.")
    val name: String,

    @Column(nullable = false)
    @field:NotBlank(message = "La description ne peut pas être vide.")
    @field:Size(min = 5, max = 100, message = "La description doit contenir entre 5 et 100 caractères.")
    val description: String,
)

