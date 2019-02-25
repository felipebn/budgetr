package devlium.budgetr

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.time.LocalDate
import java.time.Month
import java.util.*

@SpringBootApplication
class BudgetrApplication{

	@Bean	
	fun init(seedLoader : Optional<SeedDataLoader>, budgetService :BudgetServiceBean) = CommandLineRunner{
		seedLoader.ifPresent {
			it.seedData()
		}
		
		val start = LocalDate.of(2019, Month.FEBRUARY, 24)
		val period = Period(start, start.plusDays(6))

		val forecast = budgetService.forecast(period, 8)
		
		forecast.budgets.forEach { budget ->
			println("Week budget (${budget.period}): ${budget.total}")
			budget.expenses.forEach{
				println("${it.amount}\t${it.description}")
			}	
		}
		
		println("Total Forecast: ${forecast.total}")
	}
	
}

//Bootstrap:
fun main(args: Array<String>) {
	runApplication<BudgetrApplication>(*args)
} 

		
		


