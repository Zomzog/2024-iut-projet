package iut.nantes.project.products.controller

data class ProductDto(val id : String, val name: String, val description: String,val price: Price,val family : FamilleDto) {
}

data class Price(val amount: Double, val currency: String)