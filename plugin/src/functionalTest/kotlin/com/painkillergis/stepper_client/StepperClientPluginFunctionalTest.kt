package com.painkillergis.stepper_client

import java.io.File
import org.gradle.testkit.runner.GradleRunner
import kotlin.test.Test
import kotlin.test.assertTrue

class StepperClientPluginFunctionalTest {
  @Test
  fun `can run task`() {
    val projectDir = File("build/functionalTest")
    projectDir.mkdirs()
    projectDir.resolve("settings.gradle").writeText("")
    projectDir.resolve("build.gradle").writeText(
      """
          plugins {
            id('com.painkillergis.stepper_client.stepperClient')
          }
        """
    )

    val runner = GradleRunner.create()
    runner.forwardOutput()
    runner.withPluginClasspath()
    runner.withArguments("greeting")
    runner.withProjectDir(projectDir)
    val result = runner.build()

    assertTrue(result.output.contains("Hello from plugin 'com.painkillergis.stepper_client.stepperClient'"))
  }
}
