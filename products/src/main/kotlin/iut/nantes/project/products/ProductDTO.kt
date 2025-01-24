package iut.nantes.project.products

import java.util.UUID


data class ProductDTO(val id: UUID, val name: String, val description: String, val price: Prix, val family : Famille )


