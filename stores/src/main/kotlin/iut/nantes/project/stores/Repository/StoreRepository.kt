package iut.nantes.project.stores.Repository


import iut.nantes.project.stores.DTO.StoreDTO
import iut.nantes.project.stores.Entity.StoreEntity
import org.springframework.data.jpa.repository.JpaRepository

interface StoreRepository : JpaRepository<StoreEntity, Long>{
    fun findTopByOrderByIdDesc(liste:  List<StoreDTO>): StoreDTO
}
