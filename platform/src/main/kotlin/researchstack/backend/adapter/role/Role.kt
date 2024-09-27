package researchstack.backend.adapter.role

@Repeatable
@Target(AnnotationTarget.FUNCTION)
annotation class Role(
    val tenants: Array<String> = ["{}"],
    val actions: Array<String> = ["{}"],
    val resources: Array<String> = ["{}"]
)
