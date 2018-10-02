import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import org.apache.commons.io.IOUtils
import java.net.InetAddress
import java.net.InetSocketAddress

//КЛАСС, ОТВЕЧАЮЩИЙ ЗА ПРОСЛУШКУ СЕРВЕРА
class VkHandler : HttpHandler {
    //Прослушка уведомлений с Callback сервера
    override fun handle(exchange: HttpExchange) {
        val json = IOUtils.toString(exchange.requestBody, Charsets.UTF_8)
        //parse(json)
        //val response = Consts.OK
        val response = "35465c85"
        exchange.sendResponseHeaders(200,response.toByteArray(Charsets.UTF_8).size.toLong())
        exchange.responseBody.write(response.toByteArray(Charsets.UTF_8))
        exchange.responseBody.close()
    }
}

fun exitListener() {
    while (true) {
        val a = readLine()
        if (a == "stop") System.exit(0)
    }
}

fun main(args: Array<String>) {
    responseLatest()
    val httpServer = HttpServer.create()
    val address = InetAddress.getLocalHost()
    print("${address.hostAddress}, ${address.hostName}")
    httpServer.bind(InetSocketAddress(address, 80), 0)
    httpServer.createContext("/", VkHandler())
    httpServer.executor = null
    httpServer.start()
    exitListener()
}
