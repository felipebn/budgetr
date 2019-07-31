package devlium.budgetr

import devlium.budgetr.data.RealBalanceSnapshotRepository
import devlium.budgetr.data.Expense
import devlium.budgetr.data.ExpensesRepository
import devlium.budgetr.data.IncomeRepository
import devlium.budgetr.system.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Component
class BudgetServiceBean {
    companion object{
        val log by logger()
    }

	@Autowired
	lateinit var expensesRepository:ExpensesRepository

    @Autowired
    lateinit var incomesRepository:IncomeRepository;

	@Autowired
	lateinit var realBalanceSnapshotRepository: RealBalanceSnapshotRepository
	
	fun project(period:Period) : Budget{
		val expenses = expensesRepository.findAll()

        log.info("Loaded expenses: {}", expenses)

		val applicableExpenses = expenses
				.filter{
					it.firstOccurrence <= period.end
				}
				.filter{
					it.applies(period)
				}.toList()

        log.info("Applicable expenses: {}", expenses)

		val totalExpense = applicableExpenses.map{ it.amount }.sum()

        val incomes = incomesRepository.findAll()

        val totalIncome = incomes.filter{
            it.applies(period)
        }.map{
            it.amount
        }.sum()
		
		return Budget(period, totalIncome, totalExpense, applicableExpenses)
	}
	
	fun forecast(startPeriod : Period, forecastLength : Int) : Forecast{
		val budgets = startPeriod.iterator(forecastLength).asSequence().map {
			project(it)
		}.toList()

        var previouslyEstimatedBalance = realBalanceSnapshotRepository.findFirstByDateLessThanEqualOrderByDateDesc(startPeriod.start)
            .map {snapshot -> snapshot.total }
            .orElse(0.0)

		val forecastSteps = budgets.map{
			val estimatedBalance = previouslyEstimatedBalance + it.totalIncome - it.totalExpense
            previouslyEstimatedBalance = estimatedBalance

			ForecastStep(it, estimatedBalance)
		}.toList()
		
		return Forecast(forecastLength, budgets.map{it.totalExpense}.sum() , forecastSteps)
	}
}

data class Forecast(val forecastLength: Int,
					val total: Double,
					val steps : List<ForecastStep>)

data class ForecastStep(val budget: Budget,
						val estimatedBalance: Double)

data class Budget(val period: Period,
                  val totalIncome:Double,
                  val totalExpense:Double,
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

