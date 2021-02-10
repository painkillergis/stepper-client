package com.painkillergis.stepper_client

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.http.*

fun newStepperClient(host: String) = newClient(host, "/stepper")

fun newClient(host: String, basePath: String) = HttpClient {
  install(JsonFeature)
  defaultRequest {
    url.protocol = URLProtocol.HTTP
    url.host = host
    url.encodedPath = "/$basePath/${url.encodedPath.trimStart('/')}"
  }
}
