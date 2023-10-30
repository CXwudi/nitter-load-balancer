plugins {
  id("org.springframework.boot") version "3.1.5"
  id("org.graalvm.buildtools.native") version "0.9.27"
  kotlin("jvm") version "1.9.10"
  kotlin("plugin.spring") version "1.9.10"
}

group = "mikufan.cx"
version = "1.0.0"

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
  }
}

dependencies {
  implementation(platform("org.springframework.boot:spring-boot-dependencies:3.1.5"))
	implementation(platform("org.springframework.cloud:spring-cloud-dependencies:2022.0.4"))
//  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("com.github.CXwudi:kotlin-jvm-inline-logging:1.0.1")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("org.apache.httpcomponents.client5:httpclient5")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer")
  implementation("com.github.ben-manes.caffeine:caffeine")
//  implementation("org.springframework.retry:spring-retry:2.0.4")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
}


kotlin {
  target {
    compilations.all {
      kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + listOf("-Xjsr305=strict", "-Xjvm-default=all") // enable strict null check + jvm default
      }
    }
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}
