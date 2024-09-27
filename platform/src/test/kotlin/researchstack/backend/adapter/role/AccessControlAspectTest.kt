package researchstack.backend.adapter.role

import com.linecorp.armeria.server.ServiceRequestContext
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.aspectj.lang.ProceedingJoinPoint
import org.casbin.jcasbin.main.Enforcer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.application.exception.UnauthorizedException
import researchstack.backend.config.getUserId
import kotlin.test.assertEquals

interface Target {
    @Role
    fun testAuthorizeRole(
        @Tenants tenant: String,
        @Resources resource: String,
        @Actions action: String
    ): String

    @Roles(
        [
            Role(
                ["EXPLICITLY_SPECIFIED_TENANT"],
                ["EXPLICITLY_SPECIFIED_ACTION", "{a1}", "{}"],
                ["{r1}"]
            ),
            Role(["EXPLICITLY_SPECIFIED_TENANT"], ["EXPLICITLY_SPECIFIED_ACTION"], ["EXPLICITLY_SPECIFIED_RESOURCE"]),
            Role()
        ]
    )
    fun testAuthorizeRoles(
        @Tenants t0: String,
        @Resources r0: String,
        @Resources("r1") r1: String,
        @Actions a0: String,
        @Actions("a1") a1: String
    ): String

    @Role
    fun testStringArrayTypeOfSource(
        @Tenants t: Array<String>,
        @Resources r: Array<String>,
        @Actions a: Array<String>
    ): String

    @Role
    fun testUnsupportedTypeOfSource(
        @Tenants r: BogusClass
    ): String
}

data class BogusClass(
    val id: String
)

open class TestTarget : Target {
    @Role
    override fun testAuthorizeRole(
        @Tenants tenant: String,
        @Resources resource: String,
        @Actions action: String
    ) = ""

    @Roles(
        [
            Role(
                ["EXPLICITLY_SPECIFIED_TENANT"],
                ["EXPLICITLY_SPECIFIED_ACTION", "{a1}", "{}"],
                ["{r1}"]
            ),
            Role(["EXPLICITLY_SPECIFIED_TENANT"], ["EXPLICITLY_SPECIFIED_ACTION"], ["EXPLICITLY_SPECIFIED_RESOURCE"]),
            Role()
        ]
    )
    override fun testAuthorizeRoles(
        @Tenants t0: String,
        @Resources r0: String,
        @Resources("r1") r1: String,
        @Actions a0: String,
        @Actions("a1") a1: String
    ) = ""

    @Role
    override fun testStringArrayTypeOfSource(
        @Tenants t: Array<String>,
        @Resources r: Array<String>,
        @Actions a: Array<String>
    ) = ""

    @Role
    override fun testUnsupportedTypeOfSource(
        @Resources r: BogusClass
    ) = ""
}

internal class AccessControlAspectTest {
    private val serviceRequestContext = mockk<ServiceRequestContext>()

    private val joinPoint = mockk<ProceedingJoinPoint>()
    private val enforcer = mockk<Enforcer>()
    private val accessControlAspect = AccessControlAspect(enforcer)

    private val testTarget = TestTarget()

    val target = AspectJProxyFactory(testTarget).apply {
        addAspect(AccessControlAspect(enforcer))
    }.getProxy<Target>()

    private val userId = "7e3ba225-0e3c-4c87-9d02-b1fbe0802239"

    @BeforeEach
    fun setup() {
        mockkStatic(ServiceRequestContext::class)
        every {
            ServiceRequestContext.current()
        } returns serviceRequestContext
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `authorizeRole throws IllegalArgumentException when userId was empty`(userId: String) {
        every {
            serviceRequestContext.getUserId()
        } returns userId

        val exception = assertThrows<IllegalArgumentException> {
            accessControlAspect.authorizeRole(joinPoint, Role())
        }
        assertEquals(ExceptionMessage.EMPTY_USER_ID, exception.message)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `authorizeRole throws UnauthorizedException when userId doesn't have the authority to do a given action on a given resource`() {
        every {
            serviceRequestContext.getUserId()
        } returns userId
        every {
            enforcer.enforce(userId, "t0", "r0", "a0")
        } returns false

        val exception = assertThrows<UnauthorizedException> {
            target.testAuthorizeRole("t0", "r0", "a0")
        }
        assertEquals("No rule ($userId, t0, r0, a0)", exception.message)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `authorizeRole should work properly`() {
        every {
            serviceRequestContext.getUserId()
        } returns userId
        every {
            enforcer.enforce(userId, "t0", "r0", "a0")
        } returns true

        target.testAuthorizeRole("t0", "r0", "a0")

        verify(exactly = 1) {
            enforcer.enforce(userId, "t0", "r0", "a0")
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `authorizeRole throws TypeCastException when it received unsupported type of resource`() {
        every {
            serviceRequestContext.getUserId()
        } returns userId

        val exception = assertThrows<TypeCastException> {
            target.testUnsupportedTypeOfSource(BogusClass("id"))
        }
        assertEquals("Unsupported type: ${BogusClass::class}", exception.message)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `authorizeRole should be able to receive string array type of resource`() {
        every {
            serviceRequestContext.getUserId()
        } returns userId
        every {
            enforcer.enforce(userId, "t0", "r0", "a0")
        } returns true
        every {
            enforcer.enforce(userId, "t0", "r0", "a1")
        } returns true
        every {
            enforcer.enforce(userId, "t0", "r0", "a2")
        } returns true
        every {
            enforcer.enforce(userId, "t0", "r1", "a0")
        } returns true
        every {
            enforcer.enforce(userId, "t0", "r1", "a1")
        } returns true
        every {
            enforcer.enforce(userId, "t0", "r1", "a2")
        } returns true
        every {
            enforcer.enforce(userId, "t1", "r0", "a0")
        } returns true
        every {
            enforcer.enforce(userId, "t1", "r0", "a1")
        } returns true
        every {
            enforcer.enforce(userId, "t1", "r0", "a2")
        } returns true
        every {
            enforcer.enforce(userId, "t1", "r1", "a0")
        } returns true
        every {
            enforcer.enforce(userId, "t1", "r1", "a1")
        } returns true
        every {
            enforcer.enforce(userId, "t1", "r1", "a2")
        } returns true

        target.testStringArrayTypeOfSource(arrayOf("t0", "t1"), arrayOf("r0", "r1"), arrayOf("a0", "a1", "a2"))

        verify(exactly = 1) {
            enforcer.enforce(userId, "t0", "r0", "a0")
            enforcer.enforce(userId, "t0", "r0", "a1")
            enforcer.enforce(userId, "t0", "r0", "a2")
            enforcer.enforce(userId, "t0", "r1", "a0")
            enforcer.enforce(userId, "t0", "r1", "a1")
            enforcer.enforce(userId, "t0", "r1", "a2")
            enforcer.enforce(userId, "t1", "r0", "a0")
            enforcer.enforce(userId, "t1", "r0", "a1")
            enforcer.enforce(userId, "t1", "r0", "a2")
            enforcer.enforce(userId, "t1", "r1", "a0")
            enforcer.enforce(userId, "t1", "r1", "a1")
            enforcer.enforce(userId, "t1", "r1", "a2")
        }
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `authorizeRoles throws IllegalArgumentException when userId was empty`(userId: String) {
        every {
            serviceRequestContext.getUserId()
        } returns userId

        val exception = assertThrows<IllegalArgumentException> {
            accessControlAspect.authorizeRoles(joinPoint, Roles(arrayOf()))
        }
        assertEquals(ExceptionMessage.EMPTY_USER_ID, exception.message)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `authorizeRoles should work properly`() {
        every {
            serviceRequestContext.getUserId()
        } returns userId
        every {
            enforcer.enforce(userId, "EXPLICITLY_SPECIFIED_TENANT", "r1", "EXPLICITLY_SPECIFIED_ACTION")
        } returns true
        every {
            enforcer.enforce(userId, "EXPLICITLY_SPECIFIED_TENANT", "r1", "a1")
        } returns true
        every {
            enforcer.enforce(userId, "EXPLICITLY_SPECIFIED_TENANT", "r1", "a0")
        } returns true
        every {
            enforcer.enforce(
                userId,
                "EXPLICITLY_SPECIFIED_TENANT",
                "EXPLICITLY_SPECIFIED_RESOURCE",
                "EXPLICITLY_SPECIFIED_ACTION"
            )
        } returns true
        every {
            enforcer.enforce(userId, "t0", "r0", "a0")
        } returns true

        target.testAuthorizeRoles("t0", "r0", "r1", "a0", "a1")

        verify(exactly = 1) {
            enforcer.enforce(userId, "EXPLICITLY_SPECIFIED_TENANT", "r1", "EXPLICITLY_SPECIFIED_ACTION")
            enforcer.enforce(userId, "EXPLICITLY_SPECIFIED_TENANT", "r1", "a1")
            enforcer.enforce(userId, "EXPLICITLY_SPECIFIED_TENANT", "r1", "a0")
            enforcer.enforce(
                userId,
                "EXPLICITLY_SPECIFIED_TENANT",
                "EXPLICITLY_SPECIFIED_RESOURCE",
                "EXPLICITLY_SPECIFIED_ACTION"
            )
            enforcer.enforce(userId, "t0", "r0", "a0")
        }
    }
}
