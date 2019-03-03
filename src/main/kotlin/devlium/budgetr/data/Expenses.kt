package devlium.budgetr.data

import devlium.budgetr.Period
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import javax.persistence.Embeddable
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Enumerated

interface Expense{
	fun applies(period:Period) : Boolean
	fun applicableDateInPeriod(period: Period) : LocalDate?
	val amount:Double
	val description:String
	val firstOccurrence:LocalDate
}

@Embeddable
data class ExpenseDetails(
	val description:String,
	val amount: Double,
	val firstOccurrence : LocalDate = LocalDate.EPOCH
)

//Mapped as entity to be able to create a Repository for it
@Entity
abstract class PersistentExpense : BaseEntity() , Expense{
	abstract val details:ExpenseDetails
	
	override val amount: Double
		get() = details.amount
	
	override val description: String
		get() = details.description

	override val firstOccurrence: LocalDate
		get() = details.firstOccurrence
}

@Entity
data class OneTimeOnlyExpense(@Embedded override val details:ExpenseDetails,
							  val dueDate:LocalDate) : PersistentExpense(){
	override fun applies(period: Period) : Boolean{
		return period.contains(dueDate)
	}

	override fun applicableDateInPeriod(period: Period): LocalDate = dueDate
}

@Entity
data class YearlyExpense(@Embedded override val details:ExpenseDetails,
						 val dueDay:Int,
						 val dueMonth:Month) : PersistentExpense(){
	
	override fun applies(period: Period) : Boolean{
		return period.contains(applicableDateInPeriod(period))
	}

	override fun applicableDateInPeriod(period: Period): LocalDate {
		var realDueDate = LocalDate.of(period.start.year, dueMonth, dueDay)

		if(realDueDate < period.start){
			realDueDate = LocalDate.of(period.end.year, dueMonth, dueDay)
		}

		return nextWorkDay(realDueDate)
	}
}

@Entity
data class MonthlyExpense(@Embedded override val details:ExpenseDetails,
						  val dueDay:Int) : PersistentExpense(){
	override fun applies(period: Period) : Boolean{
		return period.contains(applicableDateInPeriod(period))
	}

	override fun applicableDateInPeriod(period: Period): LocalDate {
		var realDueDate = LocalDate.of(period.start.year, period.start.month, dueDay)

		if(realDueDate < period.start){
			realDueDate = LocalDate.of(period.end.year, period.end.month, dueDay)
		}

		return nextWorkDay(realDueDate)
	}
}

@Entity
data class WeeklyExpense(@Embedded override val details:ExpenseDetails,
						 @Enumerated val weekDay: DayOfWeek? = null) : PersistentExpense(){
	override fun applies(period: Period) : Boolean{
		//TODO check day of week
		return true
	}

	override fun applicableDateInPeriod(period: Period): LocalDate? {
		return when(weekDay){
			null -> null
			DayOfWeek.SUNDAY -> period.start
			DayOfWeek.SATURDAY -> period.end
			else -> period.start.plusDays(weekDay.value.toLong())
		}
	}
}


fun nextWorkDay(date : LocalDate) : LocalDate = when(date.dayOfWeek){
	DayOfWeek.SATURDAY -> date.plusDays(2)
	DayOfWeek.SUNDAY -> date.plusDays(1)
	else -> date
}
