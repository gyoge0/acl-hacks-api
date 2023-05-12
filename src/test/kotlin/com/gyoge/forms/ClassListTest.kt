package com.gyoge.forms

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
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class ClassListTest {
    @Test
    fun testPostClassList() = testApplication {
        application {
            module()
        }

        client.post("/class-list") {
            parameter("first-name", "Yogesh")
            parameter("last-name", "Thambidurai")
            parameter("age", "11")
            contentType(ContentType.Application.FormUrlEncoded)
        }.apply {
            assertEquals(HttpStatusCode.Created, status)
            assertContains(Class.getAll(), Student("Yogesh", "Thambidurai", 11))
        }
    }

    @Test
    fun testPostClassListMissingParams() = testApplication {
        application {
            module()
        }

        client.post("/class-list") {
            contentType(ContentType.Application.FormUrlEncoded)
        }.apply {
            assertFalse(status.isSuccess())
        }
        client.post("/class-list") {
            parameter("first-name", "Yogesh")
            contentType(ContentType.Application.FormUrlEncoded)
        }.apply {
            assertFalse(status.isSuccess())
        }
        client.post("/class-list") {
            parameter("first-name", "Yogesh")
            parameter("last-name", "Thambidurai")
            contentType(ContentType.Application.FormUrlEncoded)
        }.apply {
            assertFalse(status.isSuccess())
        }
    }

    @Test
    fun testPostClassListBadAge() = testApplication {
        application {
            module()
        }

        client.post("/class-list") {
            parameter("first-name", "Yogesh")
            parameter("last-name", "Thambidurai")
            parameter("age", "4")
            contentType(ContentType.Application.FormUrlEncoded)
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            assertEquals("Age must be between 5 and 18", bodyAsText())
        }
        client.post("/class-list") {
            parameter("first-name", "Yogesh")
            parameter("last-name", "Thambidurai")
            parameter("age", "5")
            contentType(ContentType.Application.FormUrlEncoded)
        }.apply {
            assert(status.isSuccess())
        }
        client.post("/class-list") {
            parameter("first-name", "Yogesh")
            parameter("last-name", "Thambidurai")
            parameter("age", "18")
            contentType(ContentType.Application.FormUrlEncoded)
        }.apply {
            assert(status.isSuccess())
        }
        client.post("/class-list") {
            parameter("first-name", "Yogesh")
            parameter("last-name", "Thambidurai")
            parameter("age", "19")
            contentType(ContentType.Application.FormUrlEncoded)
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            assertEquals("Age must be between 5 and 18", bodyAsText())
        }
    }

    @Test
    fun testPostClassListNoHeader() = testApplication {
        application {
            module()
        }

        client.post("/class-list") {
            parameter("first-name", "Yogesh")
            parameter("last-name", "Thambidurai")
            parameter("age", "11")
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
        }
    }

    @Test
    fun testGetClassList() = testApplication {
        application {
            module()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        client.get("/class-list").apply {
            assert(status.isSuccess())
            assertEquals(Class.getAll(), body<List<Student>>())
        }
    }
}