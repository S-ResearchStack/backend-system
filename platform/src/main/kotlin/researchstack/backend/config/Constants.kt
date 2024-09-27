package researchstack.backend.config

import io.netty.util.AttributeKey

val STUDY_ID_KEY: AttributeKey<String> = AttributeKey.newInstance("study-id")
val USER_ID_KEY: AttributeKey<String> = AttributeKey.newInstance("user-id")
val USER_EMAIL_KEY: AttributeKey<String> = AttributeKey.newInstance("user-email")
const val RAW_DATA_EXTENSION: String = ".dutraw"
const val META_INFO_EXTENSION: String = ".metainfo"
const val MESSAGE_LOG_EXTENSION: String = ".messagelog"
const val DASH: Char = '-'
const val SLASH: Char = '/'
const val STUDY_DATA_ROOT_ID: String = "root"
