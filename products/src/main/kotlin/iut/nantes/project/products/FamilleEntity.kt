package iut.nantes.project.products

import iut.nantes.project.products.controler.FamilleDto
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface FamilleJpa: JpaRepository<FamilleDto, UUID> {
}

@Entity
@Table(name = "FAMILLE", uniqueConstraints = [UniqueConstraint(columnNames = ["name"])])
data class FamilleEntity(
    @Id
    val id: String = UUID.randomUUID().toString(),

    @Column(nullable = false, unique = true)
    @field:NotBlank(message = "Le nom ne peut pas être vide.")
    @field:Size(min = 3, max = 30, message = "Le nom doit contenir entre 3 et 30 caractères.")
    val name: String,

    @Column(nullable = false)
    @field:NotBlank(message = "La description ne peut pas être vide.")
    @field:Size(min = 5, max = 100, message = "La description doit contenir entre 5 et 100 caractères.")
    val description: String,
)

