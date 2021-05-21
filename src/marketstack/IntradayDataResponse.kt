package com.gme.marketstack

import java.util.*

/**
 * Represents the full Market Stack response for the intraday API. This wraps
 * both pagination data and the actual ticket data.
 *
 * [Market Watch API docs](https://marketstack.com/documentation#intraday_data)
 */
data class IntradayDataResponse(
    val pagination: Pagination,
    val data: List<IntraDayData>
)

data class Pagination(
    /** Returns your pagination limit value, or default(100) if not set. */
    val limit: Int,

    /** Returns your pagination offset value, or default(0) if not set. */
    val offset: Int,

    /** Returns the results count on the current page. */
    val count: Int,

    /** Returns the total count of results available. */
    val total: Int
)

data class IntraDayData(
    /**
     * Returns the exact UTC date/time the given data was collected in
     * ISO-8601 format.
     */
    val date: Date,

    /** Returns the stock ticker symbol of the current data object. */
    val symbol: String,

    /**
     * Returns the exchange MIC identification associated with the current
     * data object.
     */
    val exchange: String,

    /** Returns the raw opening price of the given stock ticker. */
    val open: Double,

    /** Returns the raw high price of the given stock ticker. */
    val high: Double,

    /** Returns the raw low price of the given stock ticker. */
    val low: Double,

    /** Returns the raw closing price of the given stock ticker. */
    val close: Double?,

    /** Returns the last executed trade of the given symbol on its exchange. */
    val last: Double?,

    /** Returns the volume of the given stock ticker. */
    val volume: Long?
)
