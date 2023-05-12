package com.gyoge.messages

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

object DeletableMessages {
    private val messages = mutableListOf("Hello ACL Hacks!")

    fun add(message: String) {
        messages.add(message)
    }

    fun delete(message: String) {
        messages.remove(message)
    }
    fun getAll() = messages
}

fun Route.deletableMessages() {
    route("/deletable-message-board") {
        post {
            val message = call.receiveText()
            if (message.isEmpty()) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    "No message specified",
                )
            } else {
                DeletableMessages.add(message)
                call.respond(HttpStatusCode.Created)
            }
        }
        get {
            call.respond(DeletableMessages.getAll())
        }
        delete {
            val message = call.receiveText()
            if (message.isEmpty()) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    "No message specified",
                )
            }
            else if (message !in DeletableMessages.getAll()) {
                call.respond(
                    HttpStatusCode.NotFound,
                    "Message not found",
                )
            }
            else {
                DeletableMessages.delete(message)
                call.respond(HttpStatusCode.NoContent)
            }

        }
    }
}
