package iut.nantes.project.stores.Repository


import iut.nantes.project.stores.Entity.ProductStoreEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductStoreRepository : JpaRepository<ProductStoreEntity, String>
