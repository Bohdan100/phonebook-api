plugins {
	id("org.springframework.boot") version "4.0.2"
	id("io.spring.dependency-management") version "1.1.7"
	kotlin("jvm") version "2.3.0"
	kotlin("plugin.spring") version "2.3.0"
	// no-args constructor Hibernate
	kotlin("plugin.jpa") version "2.3.0"
}

group = "corp.phonebook"
version = "1.0.0"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// Spring Security
	implementation("org.springframework.boot:spring-boot-starter-security")
	// Spring Web
	implementation("org.springframework.boot:spring-boot-starter-web")
	// Spring Data
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	// Flyway
	implementation("org.springframework.boot:spring-boot-starter-flyway")
	implementation("org.flywaydb:flyway-database-postgresql")
	// PostgreSQL
	implementation("org.postgresql:postgresql")
	// Jakarta library - validation
	implementation("org.springframework.boot:spring-boot-starter-validation")
	// Dev Tools
	developmentOnly("org.springframework.boot:spring-boot-devtools")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	// Tests
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
