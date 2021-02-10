package com.painkillergis.stepper_client

import io.ktor.client.*
import io.ktor.client.request.*

suspend fun switchDeployments(
  httpClient: HttpClient,
  firstServiceName: String,
  lastServiceName: String
) =
  httpClient.post<Unit>("/services/$firstServiceName/switchDeploymentsWith/$lastServiceName")
