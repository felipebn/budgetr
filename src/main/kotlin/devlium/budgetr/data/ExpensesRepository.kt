package devlium.budgetr.data

import javax.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID
import org.springframework.data.repository.NoRepositoryBean

@Transactional
interface ExpensesRepository : JpaRepository <PersistentExpense,UUID>
