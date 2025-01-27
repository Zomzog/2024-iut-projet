package iut.nantes.project.stores.Repository

import iut.nantes.project.stores.Entity.ContactEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ContactRepository : JpaRepository<ContactEntity, Int> {
    fun findByAddressCity(city: String): List<ContactEntity>
}

