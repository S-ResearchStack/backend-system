package researchstack.backend.adapter.outgoing.mongo.config

import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories
import reactor.core.publisher.Flux

@EnableReactiveMongoRepositories
@EnableReactiveMongoAuditing
class MongoConfig

const val MONGO_STUDY_ID_PREFIX = "study_"

fun getStudyDatabaseName(studyId: String): String {
    return MONGO_STUDY_ID_PREFIX + studyId
}

fun <T> Flux<T>.pagination(page: Long?, size: Long?): Flux<T> {
    val p = page ?: 0
    val s = size ?: 0
    return if (p >= 0L && s > 0L) skip(p * s).take(s) else this
}
