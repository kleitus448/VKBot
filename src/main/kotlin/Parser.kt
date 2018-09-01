import com.google.gson.Gson
import com.google.gson.*
import java.io.File
import java.io.FileReader
import java.net.URL
import java.util.*

class Parser {
    fun parse(Query:String) {
        val json = Gson().fromJson(Query, JsonObject::class.java)
        val type = json.get("type").asString
        val obj = json.get("object").asJsonObject
        try {
           when(type){
                Consts.new_message -> {
                    val text = obj.get(Consts.text).asString.replace("%","%25").replace(" ","%20")
                    val name = "src/main/resources/config.properties"
                    val properties = Properties()
                    properties.load(FileReader(File(name)))
                    val accessToken = properties.getProperty("accessToken")
                    sendMessage(text,obj.get(Consts.fromId), accessToken)
                }
                Consts.join -> {
                    val name = "src/main/resources/config.properties"
                    val properties = Properties()
                    properties.load(FileReader(File(name)))
                    val accessToken = properties.getProperty("accessToken")
                    when(obj.get("join_type").asString){
                        "request" -> {sendMessage(Consts.welcome_message_request.replace(" ","%20"),obj.get(Consts.userId),accessToken)}
                        "approved" -> {sendMessage(Consts.welcome_message_approved,obj.get(Consts.userId),accessToken)}
                        "join" -> {sendMessage("Привет",obj.get(Consts.userId),accessToken)}
                    }
                }
            }
        } catch (e:Exception){
            e.printStackTrace()
        }
    }
    
    fun keyboardConstructor(): String? {
        val name = "src/main/resources/keyboard.json"
        val result = FileReader(File(name)).readLines()
        return result.fold("") {acc, i -> acc + i}.replace(" ", "")
    }

    //@Suppress("ACTUAL_FUNCTION_WITH_DEFAULT_ARGUMENTS")
    fun sendMessage(text:String, id:JsonElement, accessToken: String){
        var reqUrl = "https://api.vk.com/method/messages.send?" +
                "user_ids=${id}" +
                "&message=${text}" +
                "&v=5.52" +
                "&access_token=${accessToken}" +
                "&keyboard=${this.keyboardConstructor().toString()}"
        println(reqUrl)
        URL(reqUrl).openStream().close()
    }
}

fun main(args: Array<String>) {

}




