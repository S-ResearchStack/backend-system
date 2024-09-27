package researchstack.backend.adapter.outgoing.postgres

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.testcontainers.containers.PostgreSQLContainer
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.application.exception.AlreadyExistsException
import researchstack.backend.application.exception.NotFoundException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@TestInstance(Lifecycle.PER_CLASS)
@ExperimentalCoroutinesApi
@EnabledIfEnvironmentVariable(named = "ENABLE_TESTCONTAINERS", matches = "true")
internal class CreateSubjectNumberPostgresAdapterTest {
    private val testDB = "testDB"
    private val dbUser = "test"
    private val dbPassword = "secure"

    private val postgreSQL = PostgreSQLContainer("postgres:9.6.12")
        .withDatabaseName(testDB)
        .withUsername(dbUser)
        .withPassword(dbPassword)

    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    private val createSubjectNumberPostgresAdapter by lazy {
        CreateSubjectNumberPostgresAdapter(namedParameterJdbcTemplate)
    }

    private val studyId = "a" + UUID.randomUUID().toString().replace('-', '_')

    @BeforeAll
    fun setUpFixture() {
        postgreSQL.start()
        namedParameterJdbcTemplate = NamedParameterJdbcTemplate(
            DataSourceBuilder.create()
                .url(postgreSQL.getJdbcUrl())
                .username(dbUser)
                .password(dbPassword)
                .build()
        )
        val sqlFile = Paths.get("src", "main", "resources", "db", "migration", "V0.0.1__create_study_join_table.sql")
        namedParameterJdbcTemplate.jdbcTemplate.execute(
            Files.readAllLines(sqlFile, Charsets.UTF_8).joinToString(" ")
        )
        namedParameterJdbcTemplate.jdbcTemplate.execute("CREATE SEQUENCE $studyId")
    }

    @AfterAll
    fun tearDown() {
        postgreSQL.stop()
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `createSubjectNumber should return subjectNumber`() = runTest {
        val subjectNumber =
            createSubjectNumberPostgresAdapter.createSubjectNumber(studyId, UUID.randomUUID().toString())
        assertNotNull(subjectNumber)
        assertDoesNotThrow {
            subjectNumber.toLong()
        }
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `createSubjectNumber should throw NotFoundException when not existed study is passed`() = runTest {
        assertThrows<NotFoundException> {
            createSubjectNumberPostgresAdapter.createSubjectNumber("unknown", UUID.randomUUID().toString())
        }
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `createSubjectNumber should assure sequence`() = runTest {
        val subjectNumber1 =
            createSubjectNumberPostgresAdapter.createSubjectNumber(studyId, UUID.randomUUID().toString())
        val subjectNumber2 =
            createSubjectNumberPostgresAdapter.createSubjectNumber(studyId, UUID.randomUUID().toString())

        assertEquals(subjectNumber1.toLong(), subjectNumber2.toLong() - 1)
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `createSubjectNumber should throw AlreadyExistsException when existed (user-id and study-id) are passed`() =
        runTest {
            val userId = UUID.randomUUID().toString()

            createSubjectNumberPostgresAdapter.createSubjectNumber(studyId, userId)
            assertThrows<AlreadyExistsException> {
                createSubjectNumberPostgresAdapter.createSubjectNumber(studyId, userId)
            }
        }
}
