FROM gradle:latest as builder

WORKDIR /builder

COPY ./gradle/libs.versions.toml ./gradle/
COPY ./gradle.properties .
COPY ./*.gradle.kts .
RUN gradle compileKotlin

COPY ./src/ ./src/
RUN gradle shadowJar

FROM amazoncorretto:17-alpine as run

WORKDIR /run
COPY --from=builder /builder/build/libs/aclhacks23-api-all.jar ./app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]