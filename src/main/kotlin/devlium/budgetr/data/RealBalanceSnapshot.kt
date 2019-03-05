package devlium.budgetr.data

import java.time.LocalDate
import javax.persistence.Entity

@Entity
data class RealBalanceSnapshot(
        val total:Double,
        val date: LocalDate
) : BaseEntity()