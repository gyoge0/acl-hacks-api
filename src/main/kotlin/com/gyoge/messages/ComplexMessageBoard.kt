package com.gyoge.messages

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class Message(val message: String, val id: Int)

object ComplexMessages {
    var currentMessageId = 0
        get() = ++field

    private val messages = mutableListOf(
        Message(
            "Hello ACL Hacks!",
            currentMessageId
        )
    )


    fun add(message: Message) {
        messages.add(message)
    }

    fun delete(id: Int) {
        messages.removeAll { it.id == id }
    }

    fun get(id: Int) = messages.singleOrNull { it.id == id }
    fun getAll() = messages.toList()
}

fun Routing.complexMessages() {
    route("/complex-message-board") {
        post {
            val message = call.receiveText()
            if (message.isEmpty()) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    "No message specified",
                )
            } else {
                ComplexMessages.add(Message(message, ComplexMessages.currentMessageId))
                call.respond(HttpStatusCode.Created)
            }
        }

        get {
            call.respond(ComplexMessages.getAll())
        }

        delete {
            val id = call.receiveText()
            if (id.isEmpty()) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    "No message specified",
                )
            }
            else if (id.toIntOrNull() == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    "Message ID must be an integer"
                )
            } else if (ComplexMessages.get(id.toInt()) == null) {
                call.respond(
                    HttpStatusCode.NotFound,
                    "Message not found",
                )
            } else {
                ComplexMessages.delete(id.toInt())
                call.respond(HttpStatusCode.NoContent)
            }

        }
    }
}