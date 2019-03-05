package devlium.budgetr.api

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import devlium.budgetr.Budget
import devlium.budgetr.BudgetServiceBean
import devlium.budgetr.ForecastStep
import devlium.budgetr.Period
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class BudgetResolver(
        val budgetServiceBean: BudgetServiceBean
    ) : GraphQLQueryResolver {

    fun currentBudget() : ResolvedBudget{
        val period = Period.fromWeekStart(LocalDate.now())
        val budget = budgetServiceBean.project(period)
        return ResolvedBudget(budget)
    }

    fun nextBudget() : ResolvedBudget{
        val period = Period.fromWeekStart(LocalDate.now().plusDays(7))
        val budget = budgetServiceBean.project(period)
        return ResolvedBudget(budget)
    }

    fun forecast(length:Int): ResolvedForecast{
        val period = Period.fromWeekStart(LocalDate.now())
        val forecast = budgetServiceBean.forecast(period, length)
        return ResolvedForecast(
                start = period.start,
                end = forecast.steps.last().budget.period.end,
                forecastItems = forecast.steps.map { ResolvedForecastItem(it) }
        )
    }
}

data class ResolvedBudget(
        val start:LocalDate,
        val end:LocalDate,
        val total:Double,
        val expenses:List<ResolvedExpense>
){
    constructor(budget:Budget) : this(
        start = budget.period.start,
        end = budget.period.end,
        total = budget.total,
        expenses = budget.expenses.map{
            ResolvedExpense(
                description = it.description,
                plannedDate = it.applicableDateInPeriod(budget.period),
                amount = it.amount
            )
        }
    )
}

data class ResolvedExpense(
        val description: String,
        val plannedDate: LocalDate?,
        val amount: Double
)

data class ResolvedForecast(
        val start:LocalDate,
        val end:LocalDate,
        val forecastItems : List<ResolvedForecastItem>
)

data class ResolvedForecastItem(
        val description:String,
        val budgeted: Double,
        val balance: Double
){
    constructor(forecastStep : ForecastStep) : this(
            description = forecastStep.budget.period.toString(),
            budgeted = forecastStep.budget.total,
            balance = forecastStep.estimatedBalance
    )
}