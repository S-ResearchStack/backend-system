package researchstack.backend.application.port.incoming.task

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.enums.TaskStatus
import researchstack.backend.enums.TaskType
import java.time.LocalDateTime

internal class CreateTaskSpecCommandTest {
    @Test
    @Tag(NEGATIVE_TEST)
    fun `init throws IllegalArgumentException when schedule was invalid expression`() {
        assertThrows<IllegalArgumentException> {
            CreateTaskSpecCommand(
                "",
                "",
                TaskStatus.CREATED,
                "x",
                LocalDateTime.now(),
                LocalDateTime.now(),
                0,
                "",
                null,
                true,
                TaskType.SURVEY,
                mapOf()
            )
        }
    }
}
