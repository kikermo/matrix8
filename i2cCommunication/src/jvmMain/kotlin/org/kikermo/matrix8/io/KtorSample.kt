package org.kikermo.matrix8.io

import com.apple.eawt.Application

fun Application.configureRouting() {
    routing {
        staticResources("static", "static")

        route("/raspiservie") {
            get {
                call.respond(
                    RaspberryInfo(deviceName = "My RPI")
                )
            }
            post("/setname") {
                try {
                    val deviceName = call.receive<Name>()
                    setDeviceName(deviceName)
                    call.respond(HttpStatusCode.NoContent)
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }
    }
}