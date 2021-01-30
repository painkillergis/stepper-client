package com.painkillergis.stepper_client

import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test
import kotlin.test.assertNotNull

class StepperClientPluginTest {
  @Test
  fun `plugin registers task`() {
    val project = ProjectBuilder.builder().build()
    project.plugins.apply("com.painkillergis.stepper_client.stepperClient")

    assertNotNull(project.tasks.findByName("greeting"))
  }
}
