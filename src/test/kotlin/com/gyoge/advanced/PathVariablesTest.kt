package com.gyoge.advanced

import com.gyoge.module
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class PathVariablesTest {
    @Test
    fun testGetIDs() = testApplication {
        application {
            module()
        }

        val client = createClient{
            install(ContentNegotiation) {
                json()
            }
        }

        client.get("/path-variables").apply {
            assertContentEquals(Stocks.tickers.keys.toList(), body<List<String>>())
        }
    }

    @Test
    fun testGetTicker() = testApplication {
        application {
            module()
        }

        val client = createClient{
            install(ContentNegotiation) {
                json()
            }
        }

        client.get("/path-variables/amzn").apply {
            assertContentEquals(Stocks.tickers["AMZN"], body<List<Double>>())
        }
    }

    @Test
    fun testPostTickerPrice() = testApplication {
        application {
            module()
        }

        client.post("/path-variables/amzn") {
            contentType(ContentType.Text.Plain)
            setBody("1000.0")
        }.apply {
            assertEquals(HttpStatusCode.Created, status)
        }

        assert(Stocks.tickers["AMZN"]?.contains(1000.0) == true)
    }

    @Test
    fun testPostNewTicker() = testApplication {
        application {
            module()
        }

        client.post("/path-variables/abc") {
            contentType(ContentType.Text.Plain)
            setBody("1000.0")
        }.apply {
            assertEquals(HttpStatusCode.Created, status)
        }

        assertNotNull(Stocks.tickers["ABC"])
        assertContentEquals(listOf(1000.0), Stocks.tickers["ABC"])
    }
}