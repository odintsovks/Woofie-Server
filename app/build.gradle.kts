plugins {
    java
    id("org.springframework.boot") version "3.2.5" // Рекомендуемая стабильная версия
    id("io.spring.dependency-management") version "1.1.4"
    id("io.gatling.gradle") version "3.14.9.5"
}

group = "com.woofie"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    // Spring Boot Starters
    implementation("org.springframework.boot:spring-boot-starter-web") // Для REST API
    implementation("org.springframework.boot:spring-boot-starter-data-jpa") // Для работы с БД

    // Database
    runtimeOnly("org.postgresql:postgresql") // Драйвер PostgreSQL

    // Lombok (для @Data, @RequiredArgsConstructor и т.д.)
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
