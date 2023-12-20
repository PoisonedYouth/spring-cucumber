import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.9.10" apply true
	id("com.ncorti.ktfmt.gradle") version "0.15.1" apply true
	id("io.gitlab.arturbosch.detekt") version "1.23.3" apply true
}

group = "com.poisonedyouth"
version = "0.0.1-SNAPSHOT"

allprojects{
	repositories {
		mavenCentral()
	}
}

subprojects{
	apply {
		plugin("org.jetbrains.kotlin.jvm")
		plugin("io.gitlab.arturbosch.detekt")
		plugin("com.ncorti.ktfmt.gradle")
	}

	tasks.withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs += "-Xjsr305=strict"
			jvmTarget = "17"
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}

	ktfmt{
		kotlinLangStyle()
	}
}

