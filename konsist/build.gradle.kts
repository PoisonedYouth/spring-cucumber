dependencies{
    testRuntimeOnly(project(":domain")){
        isTransitive = false
    }
    testRuntimeOnly(project(":application")){
        isTransitive = false
    }
    testRuntimeOnly(project(":framework")){
        isTransitive = false
    }

    testImplementation("com.lemonappdev:konsist:0.13.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
}