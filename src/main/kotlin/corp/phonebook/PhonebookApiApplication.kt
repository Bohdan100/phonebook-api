package corp.phonebook

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PhonebookApiApplication

fun main(args: Array<String>) {
	runApplication<PhonebookApiApplication>(*args)
}
