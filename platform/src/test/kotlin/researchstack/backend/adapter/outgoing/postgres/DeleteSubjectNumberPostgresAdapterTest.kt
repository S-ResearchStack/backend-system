package researchstack.backend.adapter.outgoing.postgres

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.testcontainers.containers.PostgreSQLContainer
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.application.exception.NotFoundException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExperimentalCoroutinesApi
@EnabledIfEnvironmentVariable(named = "ENABLE_TESTCONTAINERS", matches = "true")
class DeleteSubjectNumberPostgresAdapterTest {
    private val testDB = "testDB"
    private val dbUser = "test"
    private val dbPassword = "secure"

    private val postgreSQL = PostgreSQLContainer("postgres:9.6.12")
        .withDatabaseName(testDB)
        .withUsername(dbUser)
        .withPassword(dbPassword)

    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    private val deleteSubjectNumberPostgresAdapter by lazy {
        DeleteSubjectNumberPostgresAdapter(namedParameterJdbcTemplate)
    }

    private val studyId = "a" + UUID.randomUUID().toString().replace('-', '_')
    private val userId = "test-subject-id"

    private val INSERT_QUERY = """
                INSERT INTO study_joins(user_id, study_id, subject_id)
                VALUES (:userId,:studyId, nextval('%s'))
                RETURNING subject_id
            """

    private val DELETE_ALL_QUERY = """
        DELETE FROM study_joins
    """

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

    @BeforeEach
    fun beforeEach() {
        namedParameterJdbcTemplate.queryForObject(
            INSERT_QUERY.format(studyId),
            CommonSubjectNumberPostgresQuery.toNamedParameter(studyId, userId),
            String::class.java
        )
    }

    @AfterEach
    fun afterEach() {
        val emptyMap: Map<String, Any> = mapOf()
        namedParameterJdbcTemplate.update(
            DELETE_ALL_QUERY,
            emptyMap
        )
    }

    @AfterAll
    fun tearDown() {
        postgreSQL.stop()
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `deleteSubjectNumber should work properly`() = runTest {
        assertDoesNotThrow {
            deleteSubjectNumberPostgresAdapter.deleteSubjectNumber(studyId, userId)
        }
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `deleteSubjectNumber should throw NotFoundException when not existed study is passed`() = runTest {
        val wrongStudyId = "wrong-study-id"

        assertThrows<NotFoundException> {
            deleteSubjectNumberPostgresAdapter.deleteSubjectNumber(wrongStudyId, userId)
        }
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `deleteSubjectNumber should throw NotFoundException when not existed userId is passed`() = runTest {
        val wrongUserId = "wrong-subject-id"

        assertThrows<NotFoundException> {
            deleteSubjectNumberPostgresAdapter.deleteSubjectNumber(studyId, wrongUserId)
        }
    }
}
