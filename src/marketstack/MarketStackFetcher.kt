package com.gme.marketstack

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.URLBuilder

class MarketStackFetcher(private val client: HttpClient) {

    suspend fun fetch(request: IntradayDataRequest) : IntradayDataResponse {
        return client.get(
            URLBuilder(MARKET_WATCH_URL)
                .apply {
                    path(request.path())
                    parameters.appendAll(request.toParameters())
                }
                .buildString()
        )
    }

    companion object {
        const val MARKET_WATCH_URL = "http://api.marketstack.com"

        const val THE_SYMBOL = "GME"
    }
}
