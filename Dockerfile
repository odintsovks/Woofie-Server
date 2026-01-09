FROM gradle:8.14.3-jdk21 AS build

COPY . /app

WORKDIR /app

RUN gradle bootJar

FROM eclipse-temurin:21-jre-jammy AS final

ARG UID=10001
RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid "${UID}" \
    appuser
USER appuser

COPY --from=build app/app/build/libs/app-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "app.jar" ]
