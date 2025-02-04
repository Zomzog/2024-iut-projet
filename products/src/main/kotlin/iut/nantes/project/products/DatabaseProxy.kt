package iut.nantes.project.products

import iut.nantes.project.products.controller.FamilleDto
import iut.nantes.project.products.controller.ProductDto
import java.util.*

private fun FamilleDto.toEntity() = FamilleEntity(
    id = this.id ?: UUID.randomUUID(),
    name = this.name,
    description = this.description
)
private fun FamilleEntity.toDto() = FamilleDto(id,name,description)
private fun ProductDto.toEntity() = ProductsEntity(id = this.id ?: UUID.randomUUID(),name, description, price, family.toEntity())
private fun ProductsEntity.toDto() = ProductDto(id,name, description, price, family.toDto())

class DatabaseProxy(private val familleRepo: FamilleRepository, private val productRepo: ProductRepository){
fun saveFamille(famille: FamilleDto): FamilleDto? {
        val violations = validateFamille(famille)
        if (violations.isNotEmpty()) {
            return null
        }
        if (findFamilleByName(famille.name) != null) {
            return null
        }
        val familleEntity = famille.toEntity()
        val savedEntity = familleRepo.save(familleEntity)
        return savedEntity.toDto()
    }

    fun findFamilleByName(familyName: String): FamilleDto? {
        return familleRepo.findByName(familyName)?.toDto()
    }

    fun findFamilleById(id: UUID): FamilleDto? {
        if (!(isValidUUID(id))){
            return null
        }
        return familleRepo.findById(id)?.toDto()
    }

    private fun isValidUUID(uuid: UUID): Boolean {
        val uuidRegex = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$".toRegex()
        return uuidRegex.matches(uuid.toString())
    }

    fun validateFamille(famille: FamilleDto): List<String> {
        val errors = mutableListOf<String>()

        if (famille.name.isBlank() || famille.name.length !in 3..30) {
            errors.add("Le nom doit contenir entre 3 et 30 caractères.")
        }

        if (famille.description.isBlank() || famille.description.length !in 5..100) {
            errors.add("La description doit contenir entre 5 et 100 caractères.")
        }

        return errors
    }

    fun findAllFamilles(): List<FamilleDto> {
        return familleRepo.findAll().map { it.toDto() }
    }


    fun deleteFamilleById(familleId: UUID) {
        if (familleRepo.existsById(familleId)) {
            familleRepo.deleteById(familleId)
        }
    }

    fun updateFamille(id: UUID, familleDto: FamilleDto): FamilleDto? {
        val existingFamille = findFamilleById(id)
        if (existingFamille == null) {
            return null
        }

        if (findFamilleByName(familleDto.name) != null && findFamilleByName(familleDto.name)!!.id != id) {
            return null
        }

        existingFamille.name = familleDto.name
        existingFamille.description = familleDto.description

        val updatedFamille = familleRepo.save(existingFamille.toEntity())
        return updatedFamille.toDto()
    }


    private fun validateProduct(produit: ProductDto): List<String> {
        val errors = mutableListOf<String>()

        if (produit.name.isBlank() || produit.name.length !in 2..20) {
            errors.add("Le nom doit contenir entre 2 et 20 caractères.")
        }

        if (produit.description != null){
            if (produit.description.isNotBlank() && produit.description.length !in 5..100) {
                errors.add("La description doit contenir entre 5 et 100 caractères.")
            }
        }

        if (produit.price.amount <= 0) {
            errors.add("Le montant du prix doit être positif.")
        }

        if (!produit.price.currency.matches(Regex("^[A-Z]{3}$"))) {
            errors.add("La devise doit être composée de 3 caractères alphabétiques majuscules (ex: EUR).")
        }

        return errors
    }

    fun findProductByFamilleId(id: UUID): List<ProductDto> {
        if (!isValidUUID(id)) {
            return emptyList()
        }
        return productRepo.findByFamilyId(id).map { it.toDto() }
    }

    fun saveProduct(product: ProductDto): ProductDto? {
        if (findFamilleByName(product.family.name)==null){
            return null
        }
        val violationsProduct = validateProduct(product)
        val violationsFamille = validateFamille(product.family)
        if (violationsFamille.isNotEmpty() || violationsProduct.isNotEmpty()) {
            return null
        }
        val productEntity = product.toEntity()
        val savedEntity = productRepo.save(productEntity)
        return savedEntity.toDto()
    }

    fun findProductsByFamilyNameAndPriceRange(familyName : String?, minPrice: Int?, maxPrice: Int?): List<ProductDto> {
        if ((minPrice != null && minPrice < 0) || (minPrice != null && maxPrice != null && maxPrice < minPrice)){
            listOf<ProductDto>()
        }
        return productRepo.findByCriteria(familyName, minPrice, maxPrice).map { it.toDto() }
    }
}