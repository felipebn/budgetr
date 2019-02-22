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
		
		val start = LocalDate.of(2019, Month.FEBRUARY, 25);
		val budget = budgetService.project(Period(start, start.plusDays(7)))
		
		println("Week budget: ${budget.totalBudget}")
		budget.expenses.forEach{
			println("${it.amount}\t${it.description}")
		}
		
	}
	
}

//Bootstrap:
fun main(args: Array<String>) {
	runApplication<BudgetrApplication>(*args)
} 

		
		


