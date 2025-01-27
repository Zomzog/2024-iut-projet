package iut.nantes.project.stores

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["iut.nantes.project.stores", "iut.nantes.project.products"])

class StoresApplication

fun main(args: Array<String>) {
    runApplication<StoresApplication>(*args)
}
