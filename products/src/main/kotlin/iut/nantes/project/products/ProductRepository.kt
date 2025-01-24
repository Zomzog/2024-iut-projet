package iut.nantes.project.products

import org.springframework.data.jpa.repository.JpaRepository


interface ProductRepository : JpaRepository<ProductEntity, String>
