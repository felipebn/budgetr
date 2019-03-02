package devlium.budgetr

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.util.*

@SpringBootApplication
class BudgetrApplication{

	@Bean
	fun init(seedLoader : Optional<SeedDataLoader>, budgetService :BudgetServiceBean) = CommandLineRunner{
		seedLoader.ifPresent {
			it.seedData()
		}
	}
	
}

//Bootstrap:
fun main(args: Array<String>) {
	runApplication<BudgetrApplication>(*args)
} 

		
		


