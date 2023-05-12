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

class DeletableMessagesTest {

    @Test
    fun testDeleteDeletable() = testApplication {
        application {
            module()
        }

        DeletableMessages.add("test")

        client.delete("/deletable-message-board") {
            setBody("test")
            contentType(ContentType.Text.Plain)
        }.apply {
            assertEquals(HttpStatusCode.NoContent, status)
            assertNotEquals("test", DeletableMessages.getAll().single())
        }
    }

    @Test
    fun testDeleteDeletableNoMessage() = testApplication {
        application {
            module()
        }

        client.delete("/deletable-message-board") {
            setBody("")
            contentType(ContentType.Text.Plain)
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            assertEquals("No message specified", bodyAsText())
        }
    }

    @Test
    fun testDeleteDeletableNotExists() = testApplication {
        application {
            module()
        }


        client.delete("/deletable-message-board") {
            setBody("100")
            contentType(ContentType.Text.Plain)
        }.apply {
            assertEquals(HttpStatusCode.NotFound, status)
            assertEquals("Message not found", bodyAsText())
        }
    }


    @Test
    fun testGetDeletable() = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        client.get("/deletable-message-board").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertContentEquals(body<List<String>>(), DeletableMessages.getAll())
        }
    }

    @Test
    fun testPostDeletable() = testApplication {
        application {
            module()
        }
        client.post("/deletable-message-board") {
            setBody("test")
        }.apply {
            assertEquals(HttpStatusCode.Created, status)
            assertContains(DeletableMessages.getAll(), "test")
        }
    }


    @Test
    fun testPostDeletableNoContent() = testApplication {
        application {
            module()
        }
        client.post("/deletable-message-board").apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            assertEquals("No message specified", bodyAsText())
        }
    }
}