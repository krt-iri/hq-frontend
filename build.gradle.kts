val annotationsVersion="26.0.2" // https://mvnrepository.com/artifact/org.jetbrains/annotations https://github.com/JetBrains/java-annotations
val checkstyleVersion="10.25.0" // https://github.com/checkstyle/checkstyle
val semver4jVersion = "5.7.0" // https://mvnrepository.com/artifact/org.semver4j/semver4j
val mockitoVersion = "5.18.0" // https://mvnrepository.com/artifact/org.mockito/mockito-core
val mockitoAgent = configurations.create("mockitoAgent")

plugins {
  java
  id("application")
  id("idea")
  id("jacoco")
  id("checkstyle")
  id("io.freefair.lombok") version "8.13.1" // https://plugins.gradle.org/plugin/io.freefair.lombok
  id("org.springframework.boot") version "3.5.0" // https://plugins.gradle.org/plugin/org.springframework.boot
  id("io.spring.dependency-management") version "1.1.7" // https://plugins.gradle.org/plugin/io.spring.dependency-management
  id("org.cyclonedx.bom") version "2.3.1" // https://plugins.gradle.org/plugin/org.cyclonedx.bom
  id("org.asciidoctor.jvm.convert") version "4.0.4" // https://plugins.gradle.org/plugin/org.asciidoctor.jvm.convert
  id("com.vaadin") version "24.7.6" // https://plugins.gradle.org/plugin/com.vaadin
}

lombok {
  version.set("1.18.38")
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.jetbrains:annotations:${annotationsVersion}")
  implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
  implementation("org.springframework.boot:spring-boot-starter-quartz")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("com.vaadin:vaadin-spring-boot-starter")
  implementation("org.semver4j:semver4j:${semver4jVersion}")
  compileOnly("org.projectlombok:lombok")
  developmentOnly("org.springframework.boot:spring-boot-devtools")
  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
  annotationProcessor("org.projectlombok:lombok")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.springframework.security:spring-security-test")
  testImplementation("org.mockito:mockito-core:${mockitoVersion}")
  mockitoAgent("org.mockito:mockito-core:${mockitoVersion}") { isTransitive = false }
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

base {
  group = "de.greluc.krt.iri.frontend"
  version = "0.0.1"
  description = "Iridium HQ - Frontend"
}

java {
  toolchain {
    sourceCompatibility = JavaVersion.VERSION_24
    targetCompatibility = JavaVersion.VERSION_24
    toolchain.languageVersion.set(JavaLanguageVersion.of(24))
    withSourcesJar()
  }
}

idea {
  module {
    inheritOutputDirs = true
    isDownloadJavadoc = true
    isDownloadSources = true
  }
}

application {
  mainClass.set("de.greluc.krt.iri.hq.frontend.FrontendApplication")
}

checkstyle {
  toolVersion = checkstyleVersion
}

configurations {
  compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
  }
}

extra["snippetsDir"] = file("build/generated-snippets")
extra["vaadinVersion"] = "24.7.6"

dependencyManagement {
  imports {
    mavenBom("com.vaadin:vaadin-bom:${property("vaadinVersion")}")
  }
}

tasks {
  withType(JavaCompile::class.java) {
    options.encoding = "UTF-8"
  }

  build {
    finalizedBy(cyclonedxBom)
  }

  bootRun {
    jvmArgs("--enable-native-access=ALL-UNNAMED")
  }

  javadoc {
    options {
      (this as CoreJavadocOptions).addStringOption("Xdoclint:none", "-quiet")
    }
    setDestinationDir(project.file("docs/javadoc"))
  }

  cyclonedxBom {
    setProjectType("library")
    setSchemaVersion("1.6")
    setDestination(project.file("docs"))
    setOutputName("bom")
    setOutputFormat("all")
    setIncludeBomSerialNumber(true)
    setIncludeLicenseText(true)
  }

  test {
    useJUnitPlatform()
    jvmArgs("-javaagent:${mockitoAgent.asPath}", "--enable-native-access=ALL-UNNAMED")
    finalizedBy(jacocoTestReport)
    outputs.dir(project.extra["snippetsDir"]!!)
  }

  asciidoctor {
    inputs.dir(project.extra["snippetsDir"]!!)
    dependsOn(test)
  }
}
