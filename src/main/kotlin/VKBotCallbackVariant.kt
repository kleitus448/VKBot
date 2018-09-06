import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileReader
import java.net.InetAddress
import java.net.InetSocketAddress
import java.util.*

//КЛАСС, ОТВЕЧАЮЩИЙ ЗА ПРОСЛУШКУ СЕРВЕРА
class VkHandler() : HttpHandler {
    private val accessToken : String

    init {
        //Получение данных из config файла
        val properties = Properties()
        properties.load(FileReader(File(Consts.CONFIG_DIR)))
        this.accessToken = properties.getProperty(Consts.ACCESS_TOKEN)
    }

    //Прослушка уведомлений с Callback сервера
    override fun handle(exchange: HttpExchange) {
        val json = IOUtils.toString(exchange.requestBody, Charsets.UTF_8)
        println(json)
        Parser().parse(json, accessToken)
        val response = Consts.OK
        exchange.sendResponseHeaders(200,response.toByteArray(Charsets.UTF_8).size.toLong())
        exchange.responseBody.write(response.toByteArray(Charsets.UTF_8))
        exchange.responseBody.close()
    }
}

fun main(args: Array<String>) {
    val httpserver = HttpServer.create()
    val addr = InetAddress.getLocalHost()
    print("${addr.hostAddress}, ${addr.hostName}")
    httpserver.bind(InetSocketAddress(addr, 80), 0)
    httpserver.createContext("/", VkHandler())
    httpserver.setExecutor(null)
    httpserver.start()

}

