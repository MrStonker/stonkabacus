package com.gme

import com.gme.marketstack.IntradayDataRequest
import com.gme.marketstack.MarketStackFetcher
import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.http.URLBuilder
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.nio.file.Files
import java.nio.file.Path
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
import java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME
import java.time.format.FormatStyle
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit

val logger = Logger.DEFAULT

fun main(args: Array<String>): Unit = EngineMain.main(args)

@JvmOverloads
fun Application.module(testing: Boolean = false) {
    fun main(args: Array<String>): Unit = EngineMain.main(args)
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
    //runBlocking {
        // Sample for making a HTTP Client request
        /*
        val message = client.post<JsonSampleClass> {
            url("http://127.0.0.1:8080/path/to/endpoint")
            contentType(ContentType.Application.Json)
            body = JsonSampleClass(hello = "world")
        }
        */
    //}
    val classloader = Thread.currentThread().contextClassLoader
    val marketWatchKey = Files.readString(
        Path.of(classloader.getResource("marketwatch.key")!!.toURI()))

    val formatter = DateTimeFormatter
        .ofPattern("yyyy-MM-dd hh:mm:ss")
        .withZone(ZoneId.systemDefault());
    val now = Instant.now()
    val before = now.minus(40, ChronoUnit.DAYS )

    val request =
        IntradayDataRequest(
            accessKey = marketWatchKey,
            symbols = MarketStackFetcher.THE_SYMBOL,
            interval = "1hour",
            sort = "DESC",
            dateFrom = formatter.format(before),
            dateTo = formatter.format(now),
            limit = 1000)

    val url = URLBuilder(MarketStackFetcher.MARKET_WATCH_URL)
        .apply {
            path(request.path())
            parameters.appendAll(request.toParameters())
        }
        .buildString()

    logger.log("URL: $url")


//    val classloader = thread.currentthread().contextclassloader
//    val key = classloader.getresourceasstream("firebase.json")
//
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

data class JsonSampleClass(val hello: String)



