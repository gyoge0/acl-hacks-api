package com.gyoge.messages

import com.gyoge.module
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class SimpleMessagesTest {

    @Test
    fun testGetSimple() = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        client.get("/simple-message-board").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertContentEquals(body<List<String>>(), SimpleMessages.getAll())
        }
    }

    @Test
    fun testPostSimple() = testApplication {
        application {
            module()
        }
        client.post("/simple-message-board"){
            setBody("test")
        }.apply {
            assertEquals(HttpStatusCode.Created, status)
            assertContains(SimpleMessages.getAll(), "test")
        }
    }

    @Test
    fun testPostSimpleNoContent() = testApplication {
        application {
            module()
        }
        client.post("/simple-message-board").apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            assertEquals("No message specified", bodyAsText())
        }
    }
}