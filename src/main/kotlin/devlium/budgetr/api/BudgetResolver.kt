package devlium.budgetr.api

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import devlium.budgetr.BudgetServiceBean
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

        return ResolvedBudget(
                start = budget.period.start,
                end = budget.period.end,
                total = budget.total,
                expenses = budget.expenses.map{
                    ResolvedExpense(
                            description = it.description,
                            plannedDate = it.applicableDateInPeriod(period),
                            amount = it.amount
                    )
                }

        )
    }

}

data class ResolvedBudget(
        val start:LocalDate,
        val end:LocalDate,
        val total:Double,
        val expenses:List<ResolvedExpense>
)

data class ResolvedExpense(
        val description: String,
        val plannedDate: LocalDate?,
        val amount: Double
)
