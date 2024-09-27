package researchstack.backend.adapter.role

@Target(AnnotationTarget.FUNCTION)
annotation class Roles(val value: Array<Role>)
