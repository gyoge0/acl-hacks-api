package com.gyoge.messages

import com.gyoge.module
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlin.test.*

class ComplexMessagesTest {

    @Test
    fun testDeleteComplex() = testApplication {
        application {
            module()
        }

        ComplexMessages.add(Message("test", 10))

        client.delete("/complex-message-board") {
            setBody(10.toString())
            contentType(ContentType.Text.Plain)
        }.apply {
            assertEquals(HttpStatusCode.NoContent, status)
            assertNotEquals(10, ComplexMessages.getAll().single().id)
        }
    }

    @Test
    fun testDeleteComplexNoMessage() = testApplication {
        application {
            module()
        }

        client.delete("/complex-message-board") {
            setBody("")
            contentType(ContentType.Text.Plain)
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            assertEquals("No message specified", bodyAsText())
        }
    }
    @Test
    fun testDeleteComplexNotExists() = testApplication {
        application {
            module()
        }

        client.delete("/complex-message-board") {
            setBody("100")
            contentType(ContentType.Text.Plain)
        }.apply {
            assertEquals(HttpStatusCode.NotFound, status)
            assertEquals("Message not found", bodyAsText())
        }
    }

    @Test
    fun testGetComplex() = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        client.get("/complex-message-board").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertContentEquals(body<List<Message>>(), ComplexMessages.getAll())
        }
    }

    @Test
    fun testPostComplex() = testApplication {
        application {
            module()
        }
        client.post("/complex-message-board") {
            setBody("test")
        }.apply {
            assertEquals(HttpStatusCode.Created, status)
            assertContains(ComplexMessages.getAll().map { it.message}, "test")
        }
    }

    @Test
    fun testPostComplexNoContent() = testApplication {
        application {
            module()
        }
        client.post("/complex-message-board").apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            assertEquals("No message specified", bodyAsText())
        }
    }
}