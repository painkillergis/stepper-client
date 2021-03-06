plugins {
  `java-gradle-plugin`
  `maven-publish`
  kotlin("jvm") version "1.4.21"
}

repositories {
  jcenter()
}

dependencies {
  implementation("com.fkorotkov:kubernetes-dsl:2.8.1")
  implementation("io.fabric8:kubernetes-client:5.0.1")
  implementation("io.ktor:ktor-client-apache:1.4.1")
  implementation("io.ktor:ktor-client-core:1.4.1")
  implementation("io.ktor:ktor-client-jackson:1.4.1")
  implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
  testImplementation("org.jetbrains.kotlin:kotlin-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

version =
  ProcessBuilder("sh", "-c", "git rev-list --count HEAD")
    .start()
    .apply { waitFor() }
    .let { it.inputStream.bufferedReader().readText().trim() }
    .let { "1.0.$it" }

configurations.all {
  resolutionStrategy {
    activateDependencyLocking()
    componentSelection
      .all(object : Action<ComponentSelection> {
        @Mutate
        override fun execute(selection: ComponentSelection) {
          val version = selection.candidate.version
          when {
            version.matches(
              Regex(
                ".*-rc$",
                RegexOption.IGNORE_CASE
              )
            ) -> selection.reject("Release candidates are excluded")
            version.matches(Regex(".*-M\\d+$")) -> selection.reject("Milestones are excluded")
            version.matches(Regex(".*-alpha\\d+$")) -> selection.reject("Alphas are excluded")
            version.matches(Regex(".*-beta\\d+$")) -> selection.reject("Betas are excluded")
          }
        }
      })
  }
}

gradlePlugin {
  val stepperClient by plugins.creating {
    id = "com.painkillergis.stepper_client.stepperClient"
    implementationClass = "com.painkillergis.stepper_client.StepperClientPlugin"
  }
}

val functionalTestSourceSet = sourceSets.create("functionalTest")

gradlePlugin.testSourceSets(functionalTestSourceSet)
configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])

val functionalTest by tasks.registering(Test::class) {
  testClassesDirs = functionalTestSourceSet.output.classesDirs
  classpath = functionalTestSourceSet.runtimeClasspath
}

tasks.check {
  dependsOn(functionalTest)
}

publishing {
  repositories {
    maven {
      url = uri("s3://mvn.painkillergis.com")
      credentials(AwsCredentials::class) {
        accessKey = ProcessBuilder("sh", "-c", "grep _id ~/.aws/credentials | sed 's/.*= //'")
          .start()
          .apply { waitFor() }
          .let { it.inputStream.bufferedReader().readText().trim() }
        secretKey = ProcessBuilder("sh", "-c", "grep _secret ~/.aws/credentials | sed 's/.*= //'")
          .start()
          .apply { waitFor() }
          .let { it.inputStream.bufferedReader().readText().trim() }
      }
    }
  }
}
