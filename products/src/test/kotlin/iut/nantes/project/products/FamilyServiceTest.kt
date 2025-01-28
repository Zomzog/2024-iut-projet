package iut.nantes.project.products.Service

import iut.nantes.project.products.DTO.FamilyDTO
import iut.nantes.project.products.Entity.FamilyEntity
import iut.nantes.project.products.Exception.FamilyException
import iut.nantes.project.products.Repository.FamilyRepositoryCustom
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class FamilyServiceTest {

 private val familyRepository = MockFamilyRepository()
 private val familyService = FamilyService(familyRepository)

 @Test
 fun createFamilyShouldThrowNameConflictExceptionWhenFamilyNameExists() {
  val familyDto = FamilyDTO(UUID.randomUUID(), "Test Family", "Description")
  familyRepository.families.add(FamilyEntity(UUID.randomUUID(), familyDto.name, familyDto.description))

  assertThrows<FamilyException.NameConflictException> {
   familyService.createFamily(familyDto)
  }
 }

 @Test
 fun createFamilyShouldSaveAndReturnFamilyDTOWhenFamilyNameDoesNotExist() {
  val familyDto = FamilyDTO(UUID.randomUUID(), "Test Family", "Description")

  val createdFamily = familyService.createFamily(familyDto)

  assertNotNull(createdFamily)
  assertEquals(familyDto.name, createdFamily.name)
 }

 @Test
 fun getFamilyByIdShouldThrowFamilyNotFoundExceptionWhenFamilyDoesNotExist() {
  val id = UUID.randomUUID()

  assertThrows<FamilyException.FamilyNotFoundException> {
   familyService.getFamilyById(id)
  }
 }

 @Test
 fun getFamilyByIdShouldReturnFamilyDTOWhenFamilyExists() {
  val id = UUID.randomUUID()
  val familyEntity = FamilyEntity(id, "Test Family", "Description")
  familyRepository.families.add(familyEntity)

  val familyDto = familyService.getFamilyById(id)

  assertNotNull(familyDto)
  assertEquals(familyEntity.name, familyDto.name)
 }

 @Test
 fun deleteFamilyShouldThrowFamilyNotFoundExceptionWhenFamilyDoesNotExist() {
  val id = UUID.randomUUID()

  assertThrows<FamilyException.FamilyNotFoundException> {
   familyService.deleteFamily(id)
  }
 }
}

class MockFamilyRepository : FamilyRepositoryCustom {
 val families = mutableListOf<FamilyEntity>()

 override fun existsByName(name: String): Boolean {
  return families.any { it.name == name }
 }

 override fun save(family: FamilyEntity) {
   families.add(family)
 }

 override fun findAll(): List<FamilyEntity> {
  return families
 }

 override fun findById(id: UUID): Optional<FamilyEntity> {
  return families.find { it.id == id }?.let { Optional.of(it) } ?: Optional.empty()
 }

 override fun delete(family: FamilyEntity) {
  families.remove(family)
 }
}
