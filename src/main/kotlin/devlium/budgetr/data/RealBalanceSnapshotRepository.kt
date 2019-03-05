package devlium.budgetr.data

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate
import java.util.*
import javax.transaction.Transactional

@Transactional
interface RealBalanceSnapshotRepository : JpaRepository<RealBalanceSnapshot, UUID>{

    fun findFirstByDateLessThanEqual(date: LocalDate) : Optional<RealBalanceSnapshot>
}