package iut.nantes.project.stores.Repository


import iut.nantes.project.stores.Entity.ProductStoreEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ProductStoreRepository : JpaRepository<ProductStoreEntity, String>
