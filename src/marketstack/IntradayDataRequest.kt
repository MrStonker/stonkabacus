package com.gme.marketstack

import io.ktor.http.Parameters
import io.ktor.http.ParametersBuilder
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Market Stack request for the intraday API. This data class represents the
 * request URL and is to be used in conjunction with the [MarketStackFetcher].
 *
 * [Market Watch API docs](https://marketstack.com/documentation#intraday_data)
 */
data class IntradayDataRequest(
  /** Specify your API access key, available in your account dashboard. */
  val accessKey: String,

  /**
   * Specify one or multiple comma-separated stock symbols (tickers) for your
   * request, e.g. GME or GME,AMC. Each symbol consumes one API request.
   *
   * Maximum: 100 symbols
   */
  val symbols: String,

  /**
   * Filter your results based on a specific stock exchange by specifying
   * the MIC identification of a stock exchange.
   *
   * Example: IEXG
   */
  val exchange: String? = null,

  /**
   * Specify your preferred data interval.
   *
   * Available values: 1min, 5min, 10min, 15min, 30min, 1hour (Default),
   * 3hour, 6hour, 12hour and 24hour.
   */
  val interval: String? = null,

  /**
   * By default, results are sorted by date/time descending.
   * Use this parameter to specify a sorting order.
   *
   * Available values: DESC (Default), ASC.
   */
  val sort: String? = null,

  /**
   * Filter results based on a specific timeframe by passing a from-date in
   * YYYY-MM-DD format. You can also specify an exact time in ISO-8601 date
   * format, e.g. 2020-05-21T00:00:00+0000.
   */
  val dateFrom: String? = null,

  /**
   * Filter results based on a specific timeframe by passing an end-date in
   * YYYY-MM-DD format. You can also specify an exact time in ISO-8601 date
   * format, e.g. 2020-05-21T00:00:00+0000.
   */
  val dateTo: String? = null,

  /**
   * Specify a pagination limit (number of results per page) for your API
   * request. Default limit value is 100, maximum allowed limit value is 1000.
   */
  val limit: Int? = null,

  /**
   * Specify a pagination offset value for your API request.
   *
   * Example: An offset value of 100 combined with a limit value of 10 would
   * show results 100-110. Default value is 0, starting with the first
   * available result.
   */
  val offset: Int? = null
) {

  fun path() = "v1/intraday"

  fun toParameters(): Parameters {
    return ParametersBuilder()
      .apply {
        append("access_key", accessKey)
        append("symbols", symbols)

        exchange?.let { append("exchange", it) }
        interval?.let { append("interval", it) }
        sort?.let { append("sort", it) }
        dateFrom?.let { append("date_from", it) }
        dateTo?.let { append("date_to", it) }
        limit?.let { append("limit", it.toString()) }
        offset?.let { append("offset", it.toString()) }
      }
      .build()
  }

  companion object {
    private const val DATE_FORMAT = "yyyy-MM-dd hh:mm:ss"
    private val DATE_RANGE = Duration.ofDays(4)
    private const val INTERVAL = "1hour"
    private const val LIMIT = 1_000
    private const val SORT_ORDER = "DESC"

    fun buildRequest(accessKey: String): IntradayDataRequest {
      val formatter = DateTimeFormatter
        .ofPattern(DATE_FORMAT)
        .withZone(ZoneId.systemDefault())
      val now = Instant.now()
      val before = now.minus(DATE_RANGE)

      return IntradayDataRequest(
        accessKey = accessKey,
        symbols = MarketStackFetcher.THE_SYMBOL,
        interval = INTERVAL,
        sort = SORT_ORDER,
        dateFrom = formatter.format(before),
        dateTo = formatter.format(now),
        limit = LIMIT
      )
    }
  }
}
