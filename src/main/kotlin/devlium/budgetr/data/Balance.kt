package devlium.budgetr.data

import java.time.LocalDate

data class Balance(
        val total:Double,
        val date: LocalDate
) : BaseEntity()