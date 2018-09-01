import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.jetty.Jetty

fun main(args: Array<String>) {
    val server = embeddedServer(Jetty, configure = {
        // Property to provide a lambda that will be called during Jetty
        // server initialization with the server instance as an argument.
        configureServer = {
            //server.
        }
    }) {
        routing {
            get("/") {
                call.respondText("Hello World!", ContentType.Text.Plain)
            }
            get("/demo") {
                call.respondText("HELLO WORLD!")
            }
        }
    }
    server.start(wait = true)
}
