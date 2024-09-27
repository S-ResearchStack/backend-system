package researchstack.backend.adapter.outgoing.mongo.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import researchstack.backend.adapter.outgoing.mongo.entity.studydata.InvestigatorEntity
import researchstack.backend.domain.common.Email
import researchstack.backend.domain.investigator.Investigator

@Mapper
abstract class InvestigatorMapper {
    abstract fun toEntity(investigator: Investigator): InvestigatorEntity

    fun mapEmail(email: Email): String = email.value

    @Mapping(target = "roles", ignore = true)
    abstract fun toDomain(investigatorEntity: InvestigatorEntity): Investigator

    fun mapEmail(email: String): Email = Email(email)
}

private val converter = Mappers.getMapper(InvestigatorMapper::class.java)

fun Investigator.toEntity(): InvestigatorEntity =
    converter.toEntity(this)

fun InvestigatorEntity.toDomain(): Investigator =
    converter.toDomain(this)
