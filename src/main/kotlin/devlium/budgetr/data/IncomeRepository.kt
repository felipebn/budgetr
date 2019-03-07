package devlium.budgetr.data

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import javax.transaction.Transactional

@Transactional
interface IncomeRepository : JpaRepository<PersistentIncome, UUID>
