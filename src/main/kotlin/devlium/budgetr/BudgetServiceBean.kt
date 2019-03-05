package devlium.budgetr

import devlium.budgetr.data.RealBalanceSnapshotRepository
import devlium.budgetr.data.Expense
import devlium.budgetr.data.ExpensesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Component
class BudgetServiceBean {
	
	@Autowired
	lateinit var expensesRepository:ExpensesRepository

	@Autowired
	lateinit var realBalanceSnapshotRepository: RealBalanceSnapshotRepository
	
	fun project(period:Period) : Budget{
		val expenses = expensesRepository.findAll()
		
		val applicableExpenses = expenses
				.filter{
					it.firstOccurrence <= period.end
				}
				.filter{
					it.applies(period)
				}.toList()
		
		val total = applicableExpenses.map{ it.amount }.sum()
		
		return Budget(period, total, applicableExpenses)
	}
	
	fun forecast(startPeriod : Period, forecastLength : Int) : Forecast{
		val budgets = startPeriod.iterator(forecastLength).asSequence().map {
			project(it)
		}

		var balance = realBalanceSnapshotRepository.findFirstByDateLessThanEqual(startPeriod.start).map { it.total }.orElse(0.0)

		val forecastSteps = budgets.map{
			val estimatedBalance = balance - it.total
			balance = estimatedBalance

			ForecastStep(it, estimatedBalance)
		}.toList()
		
		return Forecast(forecastLength, budgets.map{it.total}.sum() , forecastSteps)
	}
}

data class Forecast(val forecastLength: Int,
					val total: Double,
					val steps : List<ForecastStep>)

data class ForecastStep(val budget: Budget,
						val estimatedBalance: Double)

data class Budget(	val period: Period,
				  	val total:Double,
				  	val expenses:List<Expense>)

data class Period(val start:LocalDate, val end:LocalDate){
	companion object {
	    fun fromWeekStart(referenceDate:LocalDate) : Period{
			val from = when(referenceDate.dayOfWeek){
                DayOfWeek.SUNDAY -> referenceDate
                else -> referenceDate.minusDays(referenceDate.dayOfWeek.value.toLong())
            }

			val to = from.plusDays(6)

            return Period(from, to)
		}
	}

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
				val next = current
				current = current.next()
				remaining-- 
				return next
			}
		}
	}

	override fun toString(): String {
		return "$start to $end"
	}
}

