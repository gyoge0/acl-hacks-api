FROM gradle:latest as builder

WORKDIR /builder

COPY ./gradle/libs.versions.toml ./gradle/
COPY ./gradle.properties .
COPY ./settings.gradle.kts .
COPY ./build.gradle.kts .
RUN gradle compileKotlin

COPY ./src/ ./src/
RUN gradle shadowJar

FROM amazoncorretto:17-alpine as run

WORKDIR /run
COPY --from=builder /builder/build/libs/acl-hacks-api-all.jar ./app.jar

EXPOSE 5000
ENTRYPOINT ["java", "-jar", "app.jar"]
