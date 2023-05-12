package com.gyoge.messages

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

object SimpleMessages {
    private val messages = mutableListOf("Hello ACL Hacks!")
    fun add(message: String) {
        messages.add(message)
    }

    fun getAll() = messages
}

fun Route.simpleMessages() {
    route("/simple-message-board") {
        post {
            val message = call.receiveText()
            if (message.isEmpty()) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    "No message specified",
                )
            } else {
                SimpleMessages.add(message)
                call.respond(HttpStatusCode.Created)
            }
        }
        get {
            call.respond(SimpleMessages.getAll())
        }
    }
}