package researchstack.backend.adapter.outgoing.casbin

import com.mongodb.assertions.Assertions.assertFalse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.casbin.jcasbin.main.Enforcer
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.core.io.ClassPathResource
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.config.CasbinProperties
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
internal class CasbinAdapterTest {
    private val casbinProperties =
        CasbinProperties("casbin/model.conf", "casbin/predefined-policy.csv", "HRP_CASBIN_POLICY_TOPIC")
    private val enforcer = Enforcer(ClassPathResource("casbin/model.conf").uri.path)
    private val casbinAdapter = CasbinAdapter(casbinProperties, enforcer)

    @Test
    @Tag(POSITIVE_TEST)
    fun `init should update policies without any duplications`() = runTest {
        assertEquals(0, enforcer.policy.size)
        assertEquals(0, enforcer.groupingPolicy.size)
        assertEquals(0, enforcer.getNamedGroupingPolicy("g2").size)
        assertEquals(0, enforcer.getNamedGroupingPolicy("g3").size)
        CasbinAdapter(
            CasbinProperties(
                "casbin/model.conf",
                "casbin/additional-policy-1.csv",
                "HRP_CASBIN_POLICY_TOPIC"
            ),
            enforcer
        )
        assertEquals(4, enforcer.policy.size)
        assertEquals(2, enforcer.groupingPolicy.size)
        assertEquals(0, enforcer.getNamedGroupingPolicy("g2").size)
        assertEquals(0, enforcer.getNamedGroupingPolicy("g3").size)
        CasbinAdapter(
            CasbinProperties(
                "casbin/model.conf",
                "casbin/additional-policy-2.csv",
                "HRP_CASBIN_POLICY_TOPIC"
            ),
            enforcer
        )
        assertEquals(8, enforcer.policy.size)
        assertEquals(4, enforcer.groupingPolicy.size)
        assertEquals(0, enforcer.getNamedGroupingPolicy("g2").size)
        assertEquals(1, enforcer.getNamedGroupingPolicy("g3").size)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `createStudyPolicies should throw IllegalArgumentException when userId was blank`(userId: String) = runTest {
        assertThrows<IllegalArgumentException> {
            casbinAdapter.createStudyPolicies(userId, "test-study")
        }
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `createStudyPolicies should throw IllegalArgumentException when studyId was blank`(studyId: String) = runTest {
        assertThrows<IllegalArgumentException> {
            casbinAdapter.createStudyPolicies("test-user", studyId)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `createStudyPolicies should work properly`() = runTest {
        val userId = "test-user"
        val studyId = "test-study"
        casbinAdapter.createStudyPolicies(userId, studyId)
        assertNotNull(
            enforcer.policy.find {
                it.size == 4 &&
                    it[0] == RoleConstant.ADMIN_USER_GROUP &&
                    it[1] == studyId &&
                    it[2] == RoleConstant.ADMIN_READ_RESOURCE_GROUP &&
                    it[3] == RoleConstant.ACTION_READ
            }
        )
        assertNotNull(
            enforcer.policy.find {
                it.size == 4 &&
                    it[0] == RoleConstant.ADMIN_USER_GROUP &&
                    it[1] == studyId &&
                    it[2] == RoleConstant.ADMIN_WRITE_RESOURCE_GROUP &&
                    it[3] == RoleConstant.ACTION_WRITE
            }
        )
        assertNotNull(
            enforcer.groupingPolicy.find {
                it.size == 3 && it[0] == userId && it[1] == RoleConstant.ADMIN_USER_GROUP && it[2] == studyId
            }
        )
        assertTrue(enforcer.enforce(userId, studyId, RoleConstant.ADMIN_READ_RESOURCE_GROUP, RoleConstant.ACTION_READ))
        assertTrue(
            enforcer.enforce(
                userId,
                studyId,
                RoleConstant.ADMIN_WRITE_RESOURCE_GROUP,
                RoleConstant.ACTION_WRITE
            )
        )
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `deleteStudyPolicies should work properly`() = runTest {
        val userId = "test-user"
        val studyId = "test-study"
        casbinAdapter.createStudyPolicies(userId, studyId)
        assertFalse(enforcer.policy.isEmpty())
        casbinAdapter.deleteStudyPolicies(studyId)

        assertNull(
            enforcer.policy.find {
                it.size == 4 &&
                    it[0] == RoleConstant.ADMIN_USER_GROUP &&
                    it[1] == studyId &&
                    it[2] == RoleConstant.ADMIN_READ_RESOURCE_GROUP &&
                    it[3] == RoleConstant.ACTION_READ
            }
        )
        assertNull(
            enforcer.policy.find {
                it.size == 4 &&
                    it[0] == RoleConstant.ADMIN_USER_GROUP &&
                    it[1] == studyId &&
                    it[2] == RoleConstant.ADMIN_WRITE_RESOURCE_GROUP &&
                    it[3] == RoleConstant.ACTION_WRITE
            }
        )
        assertNull(
            enforcer.groupingPolicy.find {
                it.size == 3 && it[0] == userId && it[1] == RoleConstant.ADMIN_USER_GROUP && it[2] == studyId
            }
        )
        assertFalse(enforcer.enforce(userId, studyId, RoleConstant.ADMIN_READ_RESOURCE_GROUP, RoleConstant.ACTION_READ))
        assertFalse(
            enforcer.enforce(
                userId,
                studyId,
                RoleConstant.ADMIN_WRITE_RESOURCE_GROUP,
                RoleConstant.ACTION_WRITE
            )
        )
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `deleteStudyPolicies should throw IllegalArgumentException when studyId was blank`(studyId: String) = runTest {
        assertThrows<IllegalArgumentException> {
            casbinAdapter.deleteStudyPolicies(studyId)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `addRole should work properly`() = runTest {
        val userId = "test-user"
        val studyId = "test-study"
        val role = "test-role"

        assertNull(
            enforcer.groupingPolicy.find {
                it.size == 3 && it[0] == userId && it[1] == role && it[2] == studyId
            }
        )
        assertEquals(true, casbinAdapter.addRole(userId, studyId, role))
        assertNotNull(
            enforcer.groupingPolicy.find {
                it.size == 3 && it[0] == userId && it[1] == role && it[2] == studyId
            }
        )
        assertEquals(false, casbinAdapter.addRole(userId, studyId, role))
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getRoles should work properly`() = runTest {
        val userId = "test-user"
        val studyId = "test-study"
        val role1 = "test-role-1"
        val role2 = "test-role-2"

        casbinAdapter.addRole(userId, studyId, role1)
        assertEquals(listOf("test-study_test-role-1"), casbinAdapter.getRoles(userId))
        casbinAdapter.addRole(userId, studyId, role2)
        assertEquals(listOf("test-study_test-role-1", "test-study_test-role-2"), casbinAdapter.getRoles(userId))
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getRolesInStudy should work properly`() = runTest {
        val userId = "test-user"
        val study1 = "test-study-1"
        val study2 = "test-study-2"
        val role = "test-role"

        casbinAdapter.addRole(userId, study1, role)
        casbinAdapter.addRole(userId, study2, role)
        assertEquals(listOf(role), casbinAdapter.getRolesInStudy(userId, study1))
        assertEquals(listOf(role), casbinAdapter.getRolesInStudy(userId, study2))
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `updateRole should work properly`() = runTest {
        val userId = "test-user"
        val studyId = "test-study"
        val role = "test-role"

        casbinAdapter.addRole(userId, studyId, role)
        assertEquals(listOf(role), casbinAdapter.getRolesInStudy(userId, studyId))
        casbinAdapter.updateRole(userId, studyId, "updated-role")
        assertEquals(listOf("updated-role"), casbinAdapter.getRolesInStudy(userId, studyId))
    }
}
