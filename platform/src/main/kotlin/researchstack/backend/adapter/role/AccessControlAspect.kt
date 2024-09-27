package researchstack.backend.adapter.role

import com.linecorp.armeria.server.ServiceRequestContext
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.casbin.jcasbin.main.Enforcer
import org.springframework.stereotype.Component
import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.application.exception.UnauthorizedException
import researchstack.backend.config.getUserId
import researchstack.backend.domain.subject.Subject
import researchstack.backend.util.validateContext
import java.lang.reflect.Parameter
import java.lang.reflect.Type

@Aspect
@Component
class AccessControlAspect(
    private val enforcer: Enforcer
) {
    private val ARG_REGEX: Regex = "^[{].*}$".toRegex()

    @Around("@annotation(role)")
    fun authorizeRole(joinPoint: ProceedingJoinPoint, role: Role): Any {
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        authorize(joinPoint, role, userId)

        return joinPoint.proceed()
    }

    @Around("@annotation(roles)")
    fun authorizeRoles(joinPoint: ProceedingJoinPoint, roles: Roles): Any {
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        roles.value.forEach { role ->
            authorize(joinPoint, role, userId)
        }

        return joinPoint.proceed()
    }

    private fun authorize(joinPoint: ProceedingJoinPoint, role: Role, userId: String) {
        getTenants(joinPoint, role).forEach { tenant ->
            getResources(joinPoint, role).forEach { resource ->
                getActions(joinPoint, role).forEach { action ->
                    if (!enforcer.enforce(userId, tenant, resource, action)) {
                        throw UnauthorizedException("No rule ($userId, $tenant, $resource, $action)")
                    }
                }
            }
        }
    }

    private fun getTenants(joinPoint: ProceedingJoinPoint, role: Role): List<String> {
        val tenantArgs = mutableListOf<String>()
        val tenants = mutableListOf<String>()

        role.tenants.forEach {
            if (ARG_REGEX matches it) {
                tenantArgs.add(it.substring(1, it.lastIndex))
            } else {
                tenants.add(it)
            }
        }

        tenants.addAll(
            joinPoint.getParameters()
                .withIndex()
                .filter {
                    (it.value.annotations.find { it is Tenants } as Tenants?)?.id in tenantArgs
                }
                .map {
                    toStringList(it.value.parameterizedType, joinPoint.args[it.index])
                }
                .flatten()
        )

        return tenants
    }

    private fun getResources(joinPoint: ProceedingJoinPoint, role: Role): List<String> {
        val resourceArgs = mutableListOf<String>()
        val resources = mutableListOf<String>()

        role.resources.forEach {
            if (ARG_REGEX matches it) {
                resourceArgs.add(it.substring(1, it.lastIndex))
            } else {
                resources.add(it)
            }
        }

        resources.addAll(
            joinPoint.getParameters()
                .withIndex()
                .filter {
                    (it.value.annotations.find { it is Resources } as Resources?)?.id in resourceArgs
                }
                .map {
                    toStringList(it.value.parameterizedType, joinPoint.args[it.index])
                }
                .flatten()
        )

        return resources
    }

    private fun getActions(joinPoint: ProceedingJoinPoint, role: Role): List<String> {
        val actionArgs = mutableListOf<String>()
        val actions = mutableListOf<String>()

        role.actions.forEach {
            if (ARG_REGEX matches it) {
                actionArgs.add(it.substring(1, it.lastIndex))
            } else {
                actions.add(it)
            }
        }

        actions.addAll(
            joinPoint.getParameters()
                .withIndex()
                .filter {
                    (it.value.annotations.find { it is Actions } as Actions?)?.id in actionArgs
                }
                .map {
                    toStringList(it.value.parameterizedType, joinPoint.args[it.index])
                }
                .flatten()
        )

        return actions
    }

    private fun toStringList(type: Type, arg: Any): List<String> {
        return when (type) {
            String::class.java -> {
                listOf(arg as String)
            }

            Array<String>::class.java -> {
                (arg as Array<*>).filterIsInstance<String>()
            }

            Subject.SubjectId::class.java -> {
                listOf((arg as Subject.SubjectId).value)
            }

            else -> {
                if (arg is List<*>) {
                    arg.filterIsInstance<String>()
                } else {
                    throw TypeCastException("Unsupported type: $type")
                }
            }
        }
    }

    private fun JoinPoint.getParameters(): Array<Parameter> = (this.signature as MethodSignature).method.parameters
}
