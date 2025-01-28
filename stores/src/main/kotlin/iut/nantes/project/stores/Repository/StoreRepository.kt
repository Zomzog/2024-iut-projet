package iut.nantes.project.stores.Repository


import iut.nantes.project.stores.DTO.StoreDTO
import iut.nantes.project.stores.Entity.StoreEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StoreRepository : JpaRepository<StoreEntity, Int> {
    fun findTopByOrderByIdDesc(): StoreDTO
}
