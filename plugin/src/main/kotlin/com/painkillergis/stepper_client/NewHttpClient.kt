package com.painkillergis.stepper_client

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.http.*

fun newClient(baseUri : String) = HttpClient {
  install(JsonFeature)
  defaultRequest {
    val (protocolString, hostname, basePath) = Regex("(?<protocol>\\w+)://(?<hostname>[\\w.-]+(:\\d+)?)(?<basePath>.*)")
      .matchEntire(baseUri)
      ?.groupValues
      ?.slice(listOf(1, 2, 4)) ?: throw RuntimeException("Base URI '$baseUri' is malformed")
    url.protocol = URLProtocol.createOrDefault(protocolString)
    url.host = hostname
    url.encodedPath =
      if (url.encodedPath.isEmpty()) basePath
      else "${basePath}/${url.encodedPath}"
  }
}
