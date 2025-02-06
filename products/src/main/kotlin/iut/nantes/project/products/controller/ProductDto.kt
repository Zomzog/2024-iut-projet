package iut.nantes.project.products.controller

import iut.nantes.project.products.Price
import java.util.*


data class ProductDto(val id: UUID?, var name: String, var description: String?, var price: Price, var family: FamilleDto) {
}

