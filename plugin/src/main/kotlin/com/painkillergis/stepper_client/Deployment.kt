package com.painkiller.stepper_client

import io.ktor.client.features.*
import io.ktor.client.request.*
import kotlinx.coroutines.delay

suspend fun deploy(stepperHost: String, serviceName: String, group: String, imageName: String, version: String) {
  val deployment = mapOf(
    "group" to group,
    "imageName" to imageName,
    "version" to version,
  )

  println("sending deployment request:")
  println(deployment)

  newStepperClient(stepperHost).post<Unit>("/services/$serviceName/deployment") {
    header("content-type", "application/json")
    body = deployment
  }

  val darkClient = newClient(stepperHost, serviceName)
  var lastFetchedDeployedVersion = ""
  do {
    val deployedVersion = try {
      darkClient.get<Map<String, String>>("/version")["version"]
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
