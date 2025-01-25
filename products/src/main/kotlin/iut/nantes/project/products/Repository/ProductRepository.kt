package iut.nantes.project.products.Repository

import iut.nantes.project.products.Entity.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository





interface ProductRepository : JpaRepository<ProductEntity, String>
