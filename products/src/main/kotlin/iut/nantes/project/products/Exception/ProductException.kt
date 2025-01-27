package iut.nantes.project.products.Exception

sealed class ProductException(message: String) : RuntimeException(message) {
    class InvalidDataException(message: String = "Les données fournies sont invalides.") : ProductException(message)
    class ProductNotFoundException(message: String = "Le produit n'a pas été trouvé.") : ProductException(message)
    class InvalidIdFormatException(message: String = "Le format de l'ID est invalide.") : ProductException(message)
    class ProductHasStockException(message: String = "Le produit a encore du stock.") : ProductException(message)
}
