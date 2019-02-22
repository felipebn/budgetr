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
		val dueOnYear = LocalDate.of(period.start.year, dueMonth, dueDay)
		return period.contains(dueOnYear)
	}
};

@Entity
data class MonthlyExpense(@Embedded override val details:ExpenseDetails,
						  val dueDay:Int) : PersistentExpense(){
	override fun applies(period: Period) : Boolean{
		val dueOnStartMonth = LocalDate.of(period.start.year, period.start.month, dueDay)
		return period.contains(dueOnStartMonth)
	}
};

@Entity
data class WeeklyExpense(@Embedded override val details:ExpenseDetails,
						 @Enumerated val weekDay: DayOfWeek? = null) : PersistentExpense(){
	override fun applies(period: Period) : Boolean{
		return true
	}
};