package com.gme.importer

import com.gme.importer.marketstack.IntradayDataRequest
import com.gme.importer.marketstack.MarketStackFetcher
import io.ktor.client.HttpClient
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import kotlinx.coroutines.runBlocking
import java.nio.file.Files
import java.nio.file.Path

val logger = Logger.DEFAULT

fun main() {
  val client = HttpClient {
    install(HttpTimeout)
    install(JsonFeature) {
      serializer = GsonSerializer() {
        setPrettyPrinting()
        disableHtmlEscaping()
      }
    }
    install(Logging) {
      level = LogLevel.HEADERS
    }
  }
  val classloader = Thread.currentThread().contextClassLoader
  val marketWatchKey = Files.readString(
    Path.of(classloader.getResource("marketwatch.key")!!.toURI())
  )

  val request = IntradayDataRequest.buildRequest(marketWatchKey)

  runBlocking {
    val response = MarketStackFetcher(client).fetch(request)
    logger.log("URL: $response")
  }
//    val options = firebaseoptions.builder()
//        .setcredentials(googlecredentials.fromstream(key))
//        .setdatabaseurl("https://stonkcalculator.firebaseio.com/")
//        .build();
//    val app = firebaseapp.initializeapp(options);
//    val defaultdatabase = firestoreclient.getfirestore(app)
//
//    defaultdatabase.collection("test").listdocuments().foreach {
//        logger.default.log("doc: ${it}")
//    }
}
