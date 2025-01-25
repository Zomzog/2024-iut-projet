
package iut.nantes.project.products.Service

import iut.nantes.project.products.DTO.FamilyDTO
import iut.nantes.project.products.Entity.FamilyEntity
import iut.nantes.project.products.FamilyException
import iut.nantes.project.products.Repository.FamilyRepository
import org.springframework.stereotype.Service
import java.util.*


@Service
class FamilyService(private val familyRepository: FamilyRepository) {

    fun createFamily(familyDto: FamilyDTO): FamilyDTO {
        if (familyRepository.existsByName(familyDto.name)) {
            throw FamilyException.NameConflictException()
        }
        val family = FamilyEntity(UUID.randomUUID(), familyDto.name, familyDto.description)
        familyRepository.save(family)
        return family.toDto()
    }

    fun getAllFamilies(): List<FamilyDTO> {
        return familyRepository.findAll().map { it.toDto() }
    }

    fun getFamilyById(id: String): FamilyDTO {
        val family = familyRepository.findById(id).orElseThrow { FamilyException.FamilyNotFoundException() }
        return family.toDto()
    }

    fun updateFamily(id: String, familyDto: FamilyDTO): FamilyDTO {
        val family = familyRepository.findById(id).orElseThrow { FamilyException.FamilyNotFoundException() }
        if (familyRepository.existsByName(familyDto.name) && family.name != familyDto.name) {
            throw FamilyException.NameConflictException()
        }
        family.name = familyDto.name
        family.description = familyDto.description
        familyRepository.save(family)
        return family.toDto()
    }

    fun deleteFamily(id: String) {
        val family = familyRepository.findById(id).orElseThrow { FamilyException.FamilyNotFoundException() }
        familyRepository.delete(family)
    }
}
