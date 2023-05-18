package com.gyoge.advanced

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

object Stocks {
    @Suppress("SpellCheckingInspection")
    private val _tickers: MutableMap<String, MutableList<Double>> = mutableMapOf(
        "AMZN" to mutableListOf(100.0, 110.0, 107.3),
        "TSLA" to mutableListOf(74.55, 80.11, 30.22),
        "AAPL" to mutableListOf(354.22, 375.44, 380.21),
    )

    fun addPrice(ticker: String, price: Double) {
        if (ticker in _tickers) {
            _tickers[ticker]?.add(price)
        } else {
            _tickers[ticker] = mutableListOf(price)
        }
    }

    val tickers: Map<String, List<Double>>
        get() = _tickers
}

fun Routing.pathVariables() {
    route("/path-variables") {
        get {
            call.respond(HttpStatusCode.OK, Stocks.tickers.keys.toList())
        }
        get("/{ticker}") {
            when (val ticker = call.parameters["ticker"]?.uppercase()) {
                null -> call.respond(HttpStatusCode.BadRequest, "Ticker not specified")
                !in Stocks.tickers -> call.respond(HttpStatusCode.NotFound)
                else -> call.respond(HttpStatusCode.OK, Stocks.tickers[ticker] ?: emptyList())
            }
        }

        post("/{ticker}") {
            val ticker = call.parameters["ticker"]?.uppercase()

            if (ticker == null) call.respond(HttpStatusCode.BadRequest, "Ticker not specified")
            else {
                val price = call.receiveText().toDoubleOrNull() ?: return@post call.respond(
                    HttpStatusCode.BadRequest,
                    "Invalid price"
                )
                Stocks.addPrice(ticker, price)
                call.respond(HttpStatusCode.Created)
            }
        }
    }
}