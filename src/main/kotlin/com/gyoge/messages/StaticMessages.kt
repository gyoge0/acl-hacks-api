package com.gyoge.messages

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

object StaticMessages {
    private val messages = mutableListOf("Hello ACL Hacks!")
    fun getAll() = messages
}

fun Route.staticMessages() {
    route("/static-message-board") {
        get {
            call.respond(StaticMessages.getAll())
        }
    }
}