package devlium.budgetr

import devlium.budgetr.data.Expense
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.time.LocalDate
import java.time.Month
import java.util.Optional
import org.springframework.context.annotation.Bean

@SpringBootApplication
class BudgetrApplication{

	@Bean	
	fun init(seedLoader : Optional<SeedDataLoader>, budgetService :BudgetServiceBean) = CommandLineRunner{
		seedLoader.ifPresent {
			it.seedData()
		}
		
		val start = LocalDate.of(2019, Month.FEBRUARY, 24)
		var period = Period(start, start.plusDays(6))

		var forecastTotal = 0.0
		0.rangeTo(8).forEach {
			val budget = budgetService.project(period)
			
			forecastTotal += budget.total
			 
			println("Week budget ($period): ${budget.total}")
			budget.expenses.forEach{
				println("${it.amount}\t${it.description}")
			}	
			period = period.next()
		}
		
		println("Total Forecast: $forecastTotal")
	}
	
}

//Bootstrap:
fun main(args: Array<String>) {
	runApplication<BudgetrApplication>(*args)
} 

		
		


