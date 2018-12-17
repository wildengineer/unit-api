package motiion.unitapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
class UnitApiApplication

fun main(args: Array<String>) {
	runApplication<UnitApiApplication>(*args)
}

