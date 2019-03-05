package devlium.budgetr.data

import devlium.budgetr.BaseTest
import org.assertj.core.api.Assertions.*
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import java.time.Month

class RealBalanceSnapshotRepositoryTest : BaseTest() {

    @Autowired
    lateinit var repository: RealBalanceSnapshotRepository

    @Test
    fun validateLatestBalanceRetrieval(){
        val day9 = LocalDate.of(2019, Month.JANUARY, 9)

        repository.save(RealBalanceSnapshot(date = LocalDate.of(2019, Month.JANUARY, 10), total = 10.0))
        repository.save(RealBalanceSnapshot(date = LocalDate.of(2019, Month.JANUARY, 8), total = 20.0))
        repository.save(RealBalanceSnapshot(date = LocalDate.of(2019, Month.JANUARY, 15), total = 30.0))
        repository.save(RealBalanceSnapshot(date = day9, total = 40.0))

        repository.flush()

        assertThat(repository.findFirstByDateLessThanEqual(day9)).isNotEmpty
        assertThat(repository.findFirstByDateLessThanEqual(day9).get().total).isEqualTo(40.0)

        assertThat(repository.findFirstByDateLessThanEqual(LocalDate.now())).isNotEmpty
        assertThat(repository.findFirstByDateLessThanEqual(LocalDate.now()).get().total).isEqualTo(30.0)
    }

}