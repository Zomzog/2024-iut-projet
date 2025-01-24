package iut.nantes.project.products

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.CascadeType
import jakarta.persistence.JoinColumn
import java.util.*

@Entity
data class Famille(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID,
    val name: String,
    val description: String,

    @OneToMany(mappedBy = "family", cascade = [CascadeType.ALL])
    val products: List<ProductEntity> = mutableListOf()
)

@Entity
data class Prix(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID,
    val amount: Double,
    val currency: String
)

@Entity
data class ProductEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID,
    val name: String,
    val description: String,

    @ManyToOne
    @JoinColumn(name = "price_id")
    val price: Prix,

    @ManyToOne
    @JoinColumn(name = "family_id")
    val family: Famille
)
