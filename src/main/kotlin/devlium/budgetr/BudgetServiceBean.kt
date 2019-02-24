package devlium.budgetr

import devlium.budgetr.data.Expense
import java.time.LocalDate
import org.springframework.stereotype.Component
import devlium.budgetr.data.ExpensesRepository
import org.springframework.beans.factory.annotation.Autowired
import java.time.temporal.ChronoUnit

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
	
	fun forecast(startPeriod : Period, forecastLength : Int) : Forecast{
		val budgets = startPeriod.iterator(forecastLength).asSequence().map {
			project(it)
		}.toList()
		
		return Forecast(forecastLength, budgets.map{it.total}.sum() , budgets)
	}
}

data class Forecast(val forecastLength: Int,
					val total: Double,
					val budgets : List<Budget>)

data class Budget(	val period: Period,
				  	val total:Double,
				  	val expenses:List<Expense>)

data class Period(val start:LocalDate, val end:LocalDate){
	fun contains(date:LocalDate) : Boolean{
		return date >= start && date < end 
	}
	fun next() : Period{
		val s = end.plusDays(1)
		return Period(s, s.plusDays(ChronoUnit.DAYS.between(start, end)))
	}
	
	fun iterator(length:Int) : Iterator<Period>{
		return object:Iterator<Period>{
			var current = this@Period
			var remaining = length
			
			override fun hasNext() = remaining > 0

			override fun next(): Period {
				var next = current
				current = current.next()
				remaining-- 
				return next
			}
		}
	}
}

