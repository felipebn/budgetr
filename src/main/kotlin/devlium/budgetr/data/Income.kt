package devlium.budgetr.data

import devlium.budgetr.Period
import java.time.LocalDate
import javax.persistence.Embeddable
import javax.persistence.Embedded
import javax.persistence.Entity

interface Income{
    fun applies(period: Period) : Boolean
    fun applicableDateInPeriod(period: Period) : LocalDate?
    val description : String
    val amount : Double
}

@Embeddable
data class IncomeDetails(
    val description : String,
    val amount : Double
)

@Entity
abstract class PersistentIncome : BaseEntity() , Income{
    abstract val details : IncomeDetails

    override val amount: Double
        get() = details.amount

    override val description: String
        get() = details.description
}

@Entity
data class MonthlyIncome(
    @Embedded override val details: IncomeDetails,
    val paymentDay: Int,
    val starDate: LocalDate
) : PersistentIncome(){
    override fun applicableDateInPeriod(period: Period): LocalDate {
        var realDueDate = LocalDate.of(period.start.year, period.start.month, paymentDay)

        if(realDueDate < period.start){
            realDueDate = LocalDate.of(period.end.year, period.end.month, paymentDay)
        }

        return nextWorkDay(realDueDate)
    }

    override fun applies(period: Period): Boolean {
        return period.contains(applicableDateInPeriod(period))
    }
}

@Entity
data class OneTimeIncome(
    @Embedded override val details: IncomeDetails,
    val paymentDate: LocalDate
) : PersistentIncome(){
    override fun applicableDateInPeriod(period: Period): LocalDate {
        return paymentDate
    }

    override fun applies(period: Period): Boolean {
        return period.contains(applicableDateInPeriod(period))
    }
}
