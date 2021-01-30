package com.painkillergis.stepper_client

import org.gradle.api.Project
import org.gradle.api.Plugin

class StepperClientPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    project.tasks.register("greeting") { task ->
      task.doLast {
        println("Hello from plugin 'com.painkillergis.stepper_client.greeting'")
      }
    }
  }
}
