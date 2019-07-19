package devlium.budgetr.data

import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name="real_balance_snapshot")
data class RealBalanceSnapshot(
        val total:Double,
        val date: LocalDate
) : BaseEntity()
