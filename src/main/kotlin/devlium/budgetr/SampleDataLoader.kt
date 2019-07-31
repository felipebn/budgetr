package devlium.budgetr

import devlium.budgetr.data.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month

@Profile(value=["development"])
class SampleDataLoader : SeedDataLoader {

    @Autowired
    lateinit var expensesRepository: ExpensesRepository

    @Autowired
    lateinit var incomeRepository: IncomeRepository

    @Autowired
    lateinit var realBalanceSnapshotRepository: RealBalanceSnapshotRepository

    override fun seedData() {
        seedExpenses()
        seedIncomes()
        seedRealBalances()
    }

    fun seedExpenses(){
        var monthExpenses = listOf(
             MonthlyExpense(ExpenseDetails(description = "Energy", amount = 30.0, firstOccurrence = LocalDate.of(2019, 1, 1)),3)
            ,MonthlyExpense(ExpenseDetails(description = "Water", amount = 20.0, firstOccurrence = LocalDate.of(2019, 1, 1)),5)
            ,MonthlyExpense(ExpenseDetails(description = "Gas", amount = 20.0, firstOccurrence = LocalDate.of(2019, 1, 1)),5)
            ,MonthlyExpense(ExpenseDetails(description = "Rent", amount = 800.0, firstOccurrence = LocalDate.of(2019, 1, 1)),2)
            ,MonthlyExpense(ExpenseDetails(description = "Communications", amount = 70.0, firstOccurrence = LocalDate.of(2019, 1, 1)),10)
            ,MonthlyExpense(ExpenseDetails(description = "Car payment", amount = 200.0, firstOccurrence = LocalDate.of(2019, 5, 1)),8)
        )

        var weeklyExpenses = listOf(
             WeeklyExpense(ExpenseDetails(description = "Groceries", amount = 45.0, firstOccurrence = LocalDate.of(2019, 1, 1)), DayOfWeek.SUNDAY)
            ,WeeklyExpense(ExpenseDetails(description = "Fuel", amount = 25.0, firstOccurrence = LocalDate.of(2019, 1, 1)), DayOfWeek.SUNDAY)
            ,WeeklyExpense(ExpenseDetails(description = "Eating Out 1", amount = 30.0, firstOccurrence = LocalDate.of(2019, 1, 1)), DayOfWeek.SATURDAY)
            ,WeeklyExpense(ExpenseDetails(description = "Eating Out 2", amount = 30.0, firstOccurrence = LocalDate.of(2019, 1, 1)), DayOfWeek.FRIDAY)
        )

        var yearlyExpense= listOf(
            YearlyExpense(ExpenseDetails(description = "Car Insurance", amount = 230.0, firstOccurrence = LocalDate.of(2019, 5, 8)), 2, Month.AUGUST)
        )

        var allExpenses = monthExpenses + weeklyExpenses + yearlyExpense
        expensesRepository.saveAll(allExpenses)
    }

    fun seedIncomes(){
        var monthlyIncomes = listOf(
            MonthlyIncome(IncomeDetails("Salary", 3000.0), 27, LocalDate.of(2019, 1, 27))
        )

        incomeRepository.saveAll(monthlyIncomes)
    }

    fun seedRealBalances(){
        var realBalance = RealBalanceSnapshot(total = 1500.00, date = LocalDate.of(2019, 8, 1))
        realBalanceSnapshotRepository.save(realBalance)
    }
}


