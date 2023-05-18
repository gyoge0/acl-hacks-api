package com.gyoge

import com.gyoge.advanced.classList
import com.gyoge.advanced.pathVariables
import com.gyoge.messages.complexMessages
import com.gyoge.messages.deletableMessages
import com.gyoge.messages.simpleMessages
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import com.gyoge.messages.staticMessages

fun Application.configureRouting() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
        })
    }

    routing {
        route("/") {
            staticResources(remotePath = "/", basePackage = "static")
        }
        staticMessages()
        simpleMessages()
        deletableMessages()
        complexMessages()
        classList()
        pathVariables()
    }
}
