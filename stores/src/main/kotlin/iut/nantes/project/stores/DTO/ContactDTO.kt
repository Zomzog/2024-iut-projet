package iut.nantes.project.stores.DTO


import iut.nantes.project.stores.Entity.AddressEntity
import iut.nantes.project.stores.Entity.ContactEntity
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class ContactDTO(
    var id: Long? = null,

    @field:Email(message = "L'email doit être valide.")
    var email: String,

    @field:Pattern(regexp = "^[0-9]{10}$", message = "Le téléphone doit être un numéro valide à 10 chiffres.")
    var phone: String,

    @field:Size(min = 5, max = 50, message = "La rue doit faire entre 5 et 50 caractères.")
    var address: AddressDTO
) {
    fun toEntity() = ContactEntity(id, email, phone, address.toEntity())

}

data class AddressDTO(
    @field:Size(min = 1, max = 30, message = "La ville doit faire entre 1 et 30 caractères.")
    var city: String,

    @field:Pattern(regexp = "^[0-9]{5}$", message = "Le code postal doit être valide avec 5 chiffres.")
    var postalCode: String,

    @field:Size(min = 5, max = 50, message = "La rue doit faire entre 5 et 50 caractères.")
    var street: String
) {
    fun toEntity() = AddressEntity(city, postalCode, street)

}
