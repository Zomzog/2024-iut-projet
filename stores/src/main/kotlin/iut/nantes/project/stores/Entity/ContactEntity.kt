package iut.nantes.project.stores.Entity


import iut.nantes.project.stores.DTO.AddressDTO
import iut.nantes.project.stores.DTO.ContactDTO
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern

@Entity
data class ContactEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:Email(message = "Email invalide")
    val email: String,

    @field:Pattern(regexp = "\\d{10}", message = "Numéro de téléphone invalide")
    val phone: String,

    @Embedded
    val address: AddressEntity
) {
    fun toDto() = ContactDTO(id, email, phone, address.toDto())

}


@Embeddable
data class AddressEntity(
    val street: String,
    val city: String,
    val postalCode: String
){
    fun toDto() = AddressDTO(street, city, postalCode)

}