package researchstack.backend.application.service.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.Named
import org.mapstruct.factory.Mappers
import researchstack.backend.application.port.incoming.applog.SendAppLogCommand
import researchstack.backend.domain.applog.AppLog
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

@Mapper(componentModel = "spring")
abstract class AppLogMapper {
    companion object {
        val INSTANCE: AppLogMapper = Mappers.getMapper(AppLogMapper::class.java)
        private val zoneId = TimeZone.getDefault().toZoneId()
    }

    @Mappings(
        Mapping(target = "timestamp", source = "command", qualifiedByName = ["toLocalDateTime"])
    )
    abstract fun toDomain(
        command: SendAppLogCommand
    ): AppLog

    @Named("toLocalDateTime")
    fun toLocalDateTime(command: SendAppLogCommand): LocalDateTime =
        LocalDateTime.ofInstant(
            Instant.ofEpochSecond(command.timestampSeconds, command.timestampNanos),
            zoneId
        )
}
