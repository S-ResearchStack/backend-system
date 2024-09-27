package researchstack.backend.domain.study

import researchstack.backend.enums.HealthDataType

class ParticipationRequirement(
    val eligibilityTest: EligibilityTest?,
    val informedConsent: InformedConsent,
    val healthDataTypeList: List<HealthDataType>,
    val taskInfo: List<DataSpec>?
)
