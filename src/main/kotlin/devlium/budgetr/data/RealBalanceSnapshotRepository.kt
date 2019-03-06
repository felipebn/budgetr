package devlium.budgetr.data

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*
import javax.transaction.Transactional

@Repository
@Transactional
interface RealBalanceSnapshotRepository : JpaRepository<RealBalanceSnapshot, UUID>{

    fun findFirstByDateLessThanEqualOrderByDateDesc(date: LocalDate) : Optional<RealBalanceSnapshot>
}