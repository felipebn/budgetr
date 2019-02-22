package devlium.budgetr.data

import java.util.UUID
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
open class BaseEntity{
	@Id
	val id:UUID = UUID.randomUUID()
}