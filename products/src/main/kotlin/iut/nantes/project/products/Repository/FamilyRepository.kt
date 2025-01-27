package iut.nantes.project.products.Repository

import iut.nantes.project.products.Entity.FamilyEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface FamilyRepository : JpaRepository<FamilyEntity, UUID> {
    fun existsByName(name: String): Boolean
}

