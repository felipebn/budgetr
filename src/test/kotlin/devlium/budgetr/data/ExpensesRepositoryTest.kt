package devlium.budgetr.data

import devlium.budgetr.data.ExpenseDetails
import devlium.budgetr.data.MonthlyExpense
import devlium.budgetr.data.OneTimeOnlyExpense
import devlium.budgetr.data.WeeklyExpense
import devlium.budgetr.data.YearlyExpense
import org.assertj.core.api.Assertions.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDate
import java.time.Month

@RunWith(SpringRunner::class)
@SpringBootTest
class ExpensesRepositoryTest {

	@Autowired
	lateinit var repository:ExpensesRepository
	
	@Test
	fun validatePolymorphicRepo() {
		val weekly = WeeklyExpense(ExpenseDetails("weekly", 80.0))
		val monthly = MonthlyExpense(ExpenseDetails("monthly", 424.0), 2)
		val yearly = YearlyExpense(ExpenseDetails("yearly", 200.0), 15, Month.AUGUST)
		val once = OneTimeOnlyExpense(ExpenseDetails("once", 200.0), LocalDate.of(2019, Month.AUGUST, 15))

		repository.save(weekly)
		repository.save(monthly)		
		repository.save(yearly) 		
		repository.save(once) 		
		
		repository.flush()
		
		val allExpenses = repository.findAll()
		assertThat(allExpenses).hasSize(4)
		assertThat(allExpenses).containsExactlyInAnyOrder(weekly, monthly, yearly, once)
	}

}