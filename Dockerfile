ARG JAVA_VERSION=21
ARG GRADLE_VERSION=8.7.0
FROM gradle:${GRADLE_VERSION}-jdk${JAVA_VERSION}-alpine AS BUILD
WORKDIR /app
COPY --chown=gradle:gradle build.gradle.kts settings.gradle.kts ./
COPY --chown=gradle:gradle src ./src
RUN gradle clean bootJar

FROM azul/zulu-openjdk:${JAVA_VERSION}-jdk-crac AS LAYERS_BUILD
WORKDIR /app
COPY --from=BUILD app/build/libs/waliot-0.0.1.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM azul/zulu-openjdk:${JAVA_VERSION}-jre AS RUNNER
WORKDIR /app
COPY --from=LAYERS_BUILD app/spring-boot-loader/ ./
COPY --from=LAYERS_BUILD app/dependencies/ ./
COPY --from=LAYERS_BUILD app/application/ ./
COPY --from=LAYERS_BUILD app/snapshot-dependencies/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
