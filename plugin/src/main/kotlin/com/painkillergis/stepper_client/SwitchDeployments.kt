package com.painkiller.stepper_client

import io.ktor.client.request.*

suspend fun switchDeployments(stepperHost: String, firstServiceName: String, lastServiceName: String) =
  newStepperClient(stepperHost).post<Unit>("/services/$firstServiceName/switchDeploymentsWith/$lastServiceName")
