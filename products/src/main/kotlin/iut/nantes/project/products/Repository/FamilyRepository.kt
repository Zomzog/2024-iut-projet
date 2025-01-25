package iut.nantes.project.products.Repository

import iut.nantes.project.products.Entity.FamilyEntity
import org.springframework.data.jpa.repository.JpaRepository

interface FamilyRepository : JpaRepository<FamilyEntity, String> {
    fun existsByName(name: String): Boolean //A IMPLEMENTER
}
