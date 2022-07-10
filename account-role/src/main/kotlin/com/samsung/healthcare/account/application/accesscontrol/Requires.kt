package com.samsung.healthcare.account.application.accesscontrol

import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
annotation class Requires(val authorities: Array<KClass<*>>)
