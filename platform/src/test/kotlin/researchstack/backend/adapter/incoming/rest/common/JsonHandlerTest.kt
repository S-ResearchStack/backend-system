package researchstack.backend.adapter.incoming.rest.common

import com.google.gson.GsonBuilder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.domain.subject.SubjectInfo
import researchstack.backend.enums.SubjectStatus
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class JsonHandlerTest {
    private val subjectInfo = SubjectInfo("testStudy", "1", SubjectStatus.PARTICIPATING, "test-subject-id1")
    private val secondSubjectInfo = SubjectInfo("testStudy", "2", SubjectStatus.DROP, "test-subject-id2")

    @Test
    @Tag(POSITIVE_TEST)
    fun `object to Json should work properly`() {
        val jsonSubjectInfo = GsonBuilder().setPrettyPrinting().create().toJson(subjectInfo)
        val response = JsonHandler.toJson(subjectInfo)
        assertEquals(jsonSubjectInfo, response)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `list to Json should work properly`() {
        val infoList = listOf(subjectInfo, secondSubjectInfo)
        val jsonInfoList = GsonBuilder().setPrettyPrinting().create().toJson(infoList)
        val response = JsonHandler.toJson(infoList)
        assertEquals(jsonInfoList, response)
    }
}
