package iut.nantes.project.stores.DTO

import iut.nantes.project.products.DTO.ProductDTO
import iut.nantes.project.stores.Entity.StoreEntity
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size


data class StoreDTO(
    var id: Long? = null,

    @field:NotBlank(message = "Le nom du magasin ne peut pas être vide.")
    @field:Size(min = 3, max = 30, message = "Le nom du magasin doit faire entre 3 et 30 caractères.")
    var name: String,

    @field:NotNull(message = "Le contact ne peut pas être nul.")
    var contact: ContactDTO,

    var products: List<ProductDTO> = mutableListOf()
){
    fun toEntity() = StoreEntity(id, name, contact.toEntity(), products.map { it.toEntity() }.toMutableList())
}
