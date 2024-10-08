package researchstack.backend.enums

enum class ActivityType(val value: String) {
    UNSPECIFIED("Unspecified"),
    TAPPING_SPEED("TappingSpeed"),
    REACTION_TIME("ReactionTime"),
    GUIDED_BREATHING("GuidedBreathing"),
    RANGE_OF_MOTION("RangeOfMotion"),
    GAIT_AND_BALANCE("GaitAndBalance"),
    STROOP_TEST("StroopTest"),
    SPEECH_RECOGNITION("SpeechRecognition"),
    MOBILE_SPIROMETRY("MobileSpirometry"),
    SUSTAINED_PHONATION("SustainedPhonation"),
    FIVE_METER_WALK_TEST("FiveMeterWalkTest"),
    STATE_BALANCE_TEST("StateBalanceTest"),
    ROMBERG_TEST("RombergTest"),
    SIT_TO_STAND("SitToStand"),
    ORTHOSTATIC_BP("OrthostaticBp"),
    BIA_MEASUREMENT("BiaMeasurement"),
    BP_MEASUREMENT("BpMeasurement"),
    ECG_MEASUREMENT("EcgMeasurement"),
    PPG_MEASUREMENT("PpgMeasurement"),
    SPO2_MEASUREMENT("Spo2Measurement"),
    BP_AND_BIA_MEASUREMENT("BpAndBiaMeasurement"),
    STABLE_MEASUREMENT("StableMeasurement"),
    SHAPE_PAINTING("ShapePainting"),
    CATCH_LADYBUG("CatchLadyBug"),
    MEMORIZE("Memorize"),
    MEMORIZE_WORDS_START("MemorizeWordsStart"),
    MEMORIZE_WORDS_END("MemorizeWordsEnd"),
    DESCRIBE_IMAGE("DescribeImage"),
    READ_TEXT_ALOUD("ReadTextAloud"),
    ANSWER_VERBALLY("AnswerVerbally"),
    ANSWER_WRITTEN("AnswerWritten")
}
