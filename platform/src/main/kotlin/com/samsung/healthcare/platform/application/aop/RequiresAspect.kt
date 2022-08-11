package com.samsung.healthcare.platform.application.aop

import com.samsung.healthcare.platform.application.exception.ForbiddenException
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.stereotype.Component

@Aspect
@Component
class RequiresAspect {
    @Before("@annotation(requires)")
    fun checkAuthorization(requires: Requires) {
        val userRoles = listOf<String>("") // get from token.. or API..
        val requiredRoles: List<String> = requires.roles.map { it.value }

        if (!requiredRoles.any { it in userRoles })
            throw ForbiddenException("You don't have permission. This action requires any of $requiredRoles.")
    }
}
