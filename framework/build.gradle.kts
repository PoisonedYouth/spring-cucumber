plugins {
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("plugin.spring") version "1.9.10"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":application"))

    // Spring
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.20")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.0")

    // Exposed
    implementation("org.jetbrains.exposed:exposed-spring-boot-starter:0.45.0")
    implementation("org.jetbrains.exposed:exposed-java-time:0.45.0")

    //Persistence
    runtimeOnly("org.postgresql:postgresql:42.7.1")
    implementation("com.zaxxer:HikariCP:5.1.0")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
    testImplementation("com.h2database:h2:2.2.224")

}

tasks.bootRun{
    enabled = false
}

tasks.bootJar{
    enabled = false
}

