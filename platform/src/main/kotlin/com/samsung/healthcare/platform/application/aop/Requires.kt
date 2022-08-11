package com.samsung.healthcare.platform.application.aop

import com.samsung.healthcare.platform.enums.Role

@Target(AnnotationTarget.FUNCTION)
annotation class Requires(val roles: Array<Role>)
