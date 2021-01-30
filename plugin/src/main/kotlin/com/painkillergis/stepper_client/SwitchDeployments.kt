package com.painkiller.stepper_client

import io.ktor.client.request.*

suspend fun switchDeployments(firstServiceName: String, lastServiceName: String) =
  newStepperClient().post<Unit>("/services/$firstServiceName/switchDeploymentsWith/$lastServiceName")
