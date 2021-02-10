package com.painkillergis.stepper_client

import com.painkiller.stepper_client.bootstrap
import com.painkiller.stepper_client.deploy
import com.painkiller.stepper_client.switchDeployments
import kotlinx.coroutines.runBlocking
import org.gradle.api.Project
import org.gradle.api.Plugin

class StepperClientPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    project.tasks.register("greeting") { task ->
      task.doLast {
        println("Hello from plugin 'com.painkillergis.stepper_client.stepperClient'")
      }
    }
    project.tasks.register("bootstrap") { task ->
      task.doLast {
        bootstrap(project.rootProject.name)
      }
    }
    project.tasks.register("darkDeploy") { task ->
      task.doLast {
        runBlocking {
          val groupPath = project.group as String
          deploy(
            "painkiller.arctair.com",
            "${project.rootProject.name}-dark",
            groupPath.substring(groupPath.lastIndexOf(".") + 1, groupPath.length),
            project.rootProject.name,
            getVersion(),
          )
        }
      }
    }
    project.tasks.register("switchDeployments") { task ->
      task.doLast {
        runBlocking {
          switchDeployments(
            "painkiller.arctair.com",
            project.rootProject.name,
            "${project.rootProject.name}-dark",
          )
        }
      }
    }
  }
}

fun getVersion() =
  ProcessBuilder("sh", "-c", "git rev-list --count HEAD")
    .start()
    .apply { waitFor() }
    .let { it.inputStream.bufferedReader().readText().trim() }
    .let { "v1.0.$it" }
