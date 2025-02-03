package iut.nantes.project.products.controller

import iut.nantes.project.products.Price
import java.util.*


data class ProductDto(val id: UUID?, val name: String, val description: String?, val price: Price, val family: FamilleDto) {
}

