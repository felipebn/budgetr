package devlium.budgetr.data

import devlium.budgetr.Period
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import javax.persistence.Embeddable
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Enumerated
import javax.persistence.Transient

interface Expense{
	fun applies(period:Period) : Boolean
	val amount:Double
	val description:String
}

@Embeddable
data class ExpenseDetails(
	val description:String,
	val amount: Double
);

//Mapped as entity to be able to create a Repository for it
@Entity
abstract class PersistentExpense : BaseEntity() , Expense{
	abstract val details:ExpenseDetails
	
	override val amount: Double
		get() = details.amount
	
	override val description: String
		get() = details.description
}

@Entity
data class OneTimeOnlyExpense(@Embedded override val details:ExpenseDetails,
							  val dueDate:LocalDate) : PersistentExpense(){
	override fun applies(period: Period) : Boolean{
		return period.contains(dueDate)
	}
};

@Entity
data class YearlyExpense(@Embedded override val details:ExpenseDetails,
						 val dueDay:Int,
						 val dueMonth:Month) : PersistentExpense(){
	
	override fun applies(period: Period) : Boolean{
		var realDueDate = LocalDate.of(period.start.year, dueMonth, dueDay)
		
		if(realDueDate < period.start){
			realDueDate = LocalDate.of(period.end.year, dueMonth, dueDay)
		}
		
		realDueDate = nextWorkDay(realDueDate)
		
		return period.contains(realDueDate)
	}
};

@Entity
data class MonthlyExpense(@Embedded override val details:ExpenseDetails,
						  val dueDay:Int) : PersistentExpense(){
	override fun applies(period: Period) : Boolean{
		var realDueDate = LocalDate.of(period.start.year, period.start.month, dueDay)
		
		if(realDueDate < period.start){
			realDueDate = LocalDate.of(period.end.year, period.end.month, dueDay)
		}
		
		realDueDate = nextWorkDay(realDueDate)
		
		return period.contains(realDueDate)
	}
};

@Entity
data class WeeklyExpense(@Embedded override val details:ExpenseDetails,
						 @Enumerated val weekDay: DayOfWeek? = null) : PersistentExpense(){
	override fun applies(period: Period) : Boolean{
		//TODO check day of week
		return true
	}
};


fun nextWorkDay(date : LocalDate) = when(date.dayOfWeek){
	DayOfWeek.SATURDAY -> date.plusDays(2)
	DayOfWeek.SUNDAY -> date.plusDays(1)
	else -> date
}
