package researchstack.backend.config

import com.linecorp.armeria.server.auth.AuthService
import com.linecorp.armeria.server.grpc.GrpcService
import com.linecorp.armeria.server.logging.AccessLogWriter
import com.linecorp.armeria.server.logging.LoggingService
import com.linecorp.armeria.spring.ArmeriaServerConfigurator
import io.grpc.protobuf.services.ProtoReflectionService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import researchstack.backend.adapter.exception.GrpcExceptionHandler
import researchstack.backend.adapter.exception.HttpExceptionHandler
import researchstack.backend.adapter.incoming.decorator.CorsHandler
import researchstack.backend.adapter.incoming.decorator.IntegratedJwtAuthorizer
import researchstack.backend.adapter.incoming.grpc.applog.AppLogGrpcController
import researchstack.backend.adapter.incoming.grpc.auth.AuthGrpcController
import researchstack.backend.adapter.incoming.grpc.file.FileGrpcController
import researchstack.backend.adapter.incoming.grpc.healthdata.HealthDataGrpcController
import researchstack.backend.adapter.incoming.grpc.study.StudyGrpcController
import researchstack.backend.adapter.incoming.grpc.studydata.StudyDataGrpcController
import researchstack.backend.adapter.incoming.grpc.subject.SubjectGrpcController
import researchstack.backend.adapter.incoming.grpc.task.TaskGrpcController
import researchstack.backend.adapter.incoming.grpc.version.VersionGrpcController
import researchstack.backend.adapter.incoming.rest.applog.AppLogRestController
import researchstack.backend.adapter.incoming.rest.auth.AuthRestController
import researchstack.backend.adapter.incoming.rest.dashboard.DashboardRestController
import researchstack.backend.adapter.incoming.rest.data.DataRestController
import researchstack.backend.adapter.incoming.rest.education.EducationRestController
import researchstack.backend.adapter.incoming.rest.file.FileRestController
import researchstack.backend.adapter.incoming.rest.healthdata.HealthDataRestController
import researchstack.backend.adapter.incoming.rest.inlabvisit.InLabVisitRestController
import researchstack.backend.adapter.incoming.rest.investigator.InvestigatorRestController
import researchstack.backend.adapter.incoming.rest.oidc.GoogleOidcRestController
import researchstack.backend.adapter.incoming.rest.study.ParticipationRequirementRestController
import researchstack.backend.adapter.incoming.rest.study.StudyRestController
import researchstack.backend.adapter.incoming.rest.studydata.StudyDataRestController
import researchstack.backend.adapter.incoming.rest.subject.SubjectRestController
import researchstack.backend.adapter.incoming.rest.task.TaskRestController
import java.time.Duration
@Configuration
class ArmeriaConfiguration() {
    @Bean
    fun armeriaServerConfigurator(
        corsHandler: CorsHandler,
        httpExceptionHandler: HttpExceptionHandler,
        integratedJwtAuthorizer: IntegratedJwtAuthorizer,
        authGrpcController: AuthGrpcController,
        fileGrpcController: FileGrpcController,
        healthDataGrpcController: HealthDataGrpcController,
        studyGrpcController: StudyGrpcController,
        taskGrpcController: TaskGrpcController,
        subjectGrpcController: SubjectGrpcController,
        versionGrpcController: VersionGrpcController,
        studyDataGrpcController: StudyDataGrpcController,
        googleOidcHttpController: GoogleOidcRestController,
        investigatortRestController: InvestigatorRestController,
        studyRestController: StudyRestController,
        dashboardRestController: DashboardRestController,
        studyDataRestController: StudyDataRestController,
        participationRequirementController: ParticipationRequirementRestController,
        taskHttpController: TaskRestController,
        educationRestController: EducationRestController,
        inLabVisitRestController: InLabVisitRestController,
        fileRestController: FileRestController,
        appLogGrpcController: AppLogGrpcController,
        appLogRestController: AppLogRestController,
        healthDataRestController: HealthDataRestController,
        taskRestController: TaskRestController,
        dataRestController: DataRestController,
        subjectRestController: SubjectRestController,
        authRestController: AuthRestController
    ) = ArmeriaServerConfigurator { sb ->
        sb.decorator(corsHandler.build())
        sb.annotatedService(authRestController)
        sb.annotatedService(googleOidcHttpController)
        sb.annotatedService(investigatortRestController, AuthService.newDecorator(integratedJwtAuthorizer))
        sb.annotatedService(studyRestController, AuthService.newDecorator(integratedJwtAuthorizer))
        sb.annotatedService(dashboardRestController, AuthService.newDecorator(integratedJwtAuthorizer))
        sb.annotatedService(studyDataRestController, AuthService.newDecorator(integratedJwtAuthorizer))
        sb.annotatedService(participationRequirementController, AuthService.newDecorator(integratedJwtAuthorizer))
        sb.annotatedService(taskHttpController, AuthService.newDecorator(integratedJwtAuthorizer))
        sb.annotatedService(educationRestController, AuthService.newDecorator(integratedJwtAuthorizer))
        sb.annotatedService(inLabVisitRestController, AuthService.newDecorator(integratedJwtAuthorizer))
        sb.annotatedService(fileRestController, AuthService.newDecorator(integratedJwtAuthorizer))
        sb.errorHandler(httpExceptionHandler)

        sb.annotatedService(appLogRestController, AuthService.newDecorator(integratedJwtAuthorizer))
        sb.annotatedService(healthDataRestController, AuthService.newDecorator(integratedJwtAuthorizer))
        sb.annotatedService(taskRestController, AuthService.newDecorator(integratedJwtAuthorizer))
        sb.annotatedService(dataRestController, AuthService.newDecorator(integratedJwtAuthorizer))
        sb.annotatedService(subjectRestController, AuthService.newDecorator(integratedJwtAuthorizer))

        sb.accessLogWriter(AccessLogWriter.common(), true)
        sb.service(
            GrpcService.builder()
                .addService(authGrpcController)
                .addService(ProtoReflectionService.newInstance())
                .exceptionMapping(GrpcExceptionHandler())
                .build(),
            LoggingService.newDecorator()
        )
        sb.service(
            GrpcService.builder()
                .addService(fileGrpcController)
                .addService(studyGrpcController)
                .addService(taskGrpcController)
                .addService(subjectGrpcController)
                .addService(versionGrpcController)
                // Server Reflection is needed for test with gRPC CLI like grpcurl
                // https://armeria.dev/docs/server-grpc/#server-reflection
                .addService(ProtoReflectionService.newInstance())
                .exceptionMapping(GrpcExceptionHandler())
                .enableUnframedRequests(true)
                .build(),
            AuthService.newDecorator(integratedJwtAuthorizer),
            LoggingService.newDecorator()
        )
        sb.service(
            GrpcService.builder()
                .addService(healthDataGrpcController)
                .addService(studyDataGrpcController)
                .useBlockingTaskExecutor(true)
                .addService(ProtoReflectionService.newInstance())
                .exceptionMapping(GrpcExceptionHandler())
                .build(),
            AuthService.newDecorator(integratedJwtAuthorizer),
            LoggingService.newDecorator()
        )
        sb.service(
            GrpcService.builder()
                .addService(appLogGrpcController)
                .addService(ProtoReflectionService.newInstance())
                .exceptionMapping(GrpcExceptionHandler())
                .build(),
            LoggingService.newDecorator()
        )
        sb.serviceUnder("/docs", docService)
        sb.maxRequestLength(104900000) // bytes (100 MiB)
        sb.requestTimeout(Duration.ofSeconds(10)) // (default: 10 seconds)
        sb.http2MaxHeaderListSize(16384) // (default: 8192 bytes)
    }
}
