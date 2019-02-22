package devlium.budgetr

import devlium.budgetr.data.Expense
import java.time.LocalDate
import org.springframework.stereotype.Component
import devlium.budgetr.data.ExpensesRepository
import org.springframework.beans.factory.annotation.Autowired

@Component
class BudgetServiceBean {
	
	@Autowired
	lateinit var expensesRepository:ExpensesRepository
	
	fun project(period:Period) : Budget{
		val expenses = expensesRepository.findAll()
		
		val applicableExpenses = expenses.filter{
			it.applies(period)
		}.toList()
		
		val total = applicableExpenses.map{ it.amount }.sum()
		
		return Budget(period, total, applicableExpenses)
	}
}

data class Budget(	val period: Period,
				  	val totalBudget:Double,
				  	val expenses:List<Expense>)

data class Period(val start:LocalDate, val end:LocalDate){
	fun contains(date:LocalDate) : Boolean{
		return date >= start && date < end 
	}
}

