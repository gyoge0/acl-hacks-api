package com.gyoge.advanced

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.serialization.Serializable

@Serializable
data class Student(
    val firstName: String,
    val lastName: String,
    val age: Int,
)

object Class {
    private val students = mutableListOf(
        Student(
            "John",
            "Chapin",
            16
        )
    )

    fun getAll() = students.toList()
    fun addStudent(
        firstName: String,
        lastName: String,
        ageString: String
    ) {
        students.add(
            Student(
                firstName,
                lastName,
                ageString.toInt()
            )
        )
    }
}

fun Routing.classList() {
    route("/class-list") {
        get {
            call.respond(HttpStatusCode.OK, Class.getAll())
        }

        post {
            if (call.request.contentType() != ContentType.Application.FormUrlEncoded) {
                call.respond(HttpStatusCode.BadRequest)
            }

            val parameters = call.parameters
            val firstName = parameters.getOrFail("first-name")
            val lastName = parameters.getOrFail("last-name")
            val ageString = parameters.getOrFail("age")

            if (ageString.toIntOrNull() == null) {
                call.respond(HttpStatusCode.BadRequest)
            } else if (ageString.toInt() > 18 || ageString.toInt() < 5) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    "Age must be between 5 and 18"
                )
            } else {
                Class.addStudent(
                    firstName,
                    lastName,
                    ageString
                )
                call.respond(HttpStatusCode.Created)
            }
        }
    }
}