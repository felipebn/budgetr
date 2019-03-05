package devlium.budgetr.data

import java.time.LocalDate

data class RealBalanceSnapshot(
        val total:Double,
        val date: LocalDate
) : BaseEntity()