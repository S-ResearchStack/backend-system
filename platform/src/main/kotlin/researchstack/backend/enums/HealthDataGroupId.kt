package researchstack.backend.enums

enum class HealthDataGroupId(
    val value: String
) {
    SAMSUNG_HEALTH("samsunghealth"),
    PHONE_SENSOR("phonesensor"),
    WEAR("wear"),
    DEVICE_STAT("devicestat");

    companion object {
        private val healthDataGroupTypeMap = mapOf(
            "samsunghealthlegacy" to listOf(
                "BLOOD_GLUCOSE",
                "BLOOD_PRESSURE",
                "EXERCISE",
                "HEART_RATE",
                "HEIGHT",
                "OXYGEN_SATURATION",
                "SLEEP_SESSION",
                "SLEEP_STAGE",
                "STEPS",
                "WEIGHT"
            ),
            "phonesensor" to listOf(
                "ACCELEROMETER",
                "LIGHT"
            ),
            "wear" to listOf(
                "WEAR_ACCELEROMETER",
                "WEAR_BIA",
                "WEAR_ECG",
                "WEAR_HEART_RATE",
                "WEAR_PPG_GREEN",
                "WEAR_PPG_IR",
                "WEAR_PPG_RED",
                "WEAR_SPO2",
                "WEAR_SWEAT_LOSS"
            ),
            "devicestat" to listOf(
                "DEVICE_STAT_MOBILE_WEAR_CONNECTION",
                "DEVICE_STAT_WEAR_BATTERY",
                "DEVICE_STAT_WEAR_OFF_BODY",
                "DEVICE_STAT_WEAR_POWER_ON_OFF"
            )
        )

        fun getHealthDataGroupTypes(healthDataGroupId: HealthDataGroupId): List<String> {
            return healthDataGroupTypeMap[healthDataGroupId.value] ?: emptyList()
        }
    }
}
