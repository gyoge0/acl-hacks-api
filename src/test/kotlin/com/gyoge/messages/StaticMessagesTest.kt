package com.gyoge.messages

import com.gyoge.module
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class StaticMessagesTest {

    @Test
    fun testGetStatic() = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        client.get("/static-message-board").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertContentEquals(body<List<String>>(), StaticMessages.getAll())
        }
    }
}