package com.painkiller.stepper_client

import io.ktor.client.features.*
import io.ktor.client.request.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

suspend fun deploy(serviceName: String, imageName: String, version: String) {
  newStepperClient().post<Unit>("/services/$serviceName/deployment") {
    header("content-type", "application/json")
    body = mapOf(
      "imageName" to imageName,
      "version" to version,
    )
  }

  val stepperDarkClient = newStepperDarkClient()
  var lastFetchedDeployedVersion = ""
  do {
    val deployedVersion = try {
      stepperDarkClient.get<Map<String, String>>("/version")["version"]
    } catch (ignored: ServerResponseException) {
      null
    } ?: "unknown"
    if (deployedVersion !in listOf(lastFetchedDeployedVersion, version)) {
      println("waiting for version $version (currently $deployedVersion)")
    } else if (deployedVersion != version) {
      delay(1000)
    }
    lastFetchedDeployedVersion = deployedVersion
  } while (deployedVersion != version)
}
