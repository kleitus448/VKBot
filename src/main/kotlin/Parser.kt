import com.google.gson.Gson
import com.google.gson.*
import java.io.File
import java.io.FileReader
import java.net.URL

class Parser {
    fun parse(Query:String, accessToken: String) {
        val json = Gson().fromJson(Query, JsonObject::class.java)
        val type = json.get("type").asString
        val obj = json.get("object").asJsonObject
        try {
            Switcher().switch(type,obj,accessToken)
        } catch (e:Exception){
            e.printStackTrace()
        }
    }
    
    fun keyboardConstructor(): String? {
        val name = "src/main/resources/keyboard.json"
        val result = FileReader(File(name)).readLines()
        return result.fold("") {acc, i -> acc + i}.replace(" ", "")
    }


}
fun sendMessage(text:String, id:JsonElement, accessToken: String){
    val reqUrl = "https://api.vk.com/method/messages.send?" +
            "user_ids=${id}" +
            "&message=${text}" +
            "&v=5.52" +
            "&access_token=${accessToken}" +
            "&keyboard=${Parser().keyboardConstructor().toString()}"
    println(reqUrl)
    URL(reqUrl).openStream().close()
}

class Switcher{
    fun switch(type: String, obj: JsonObject, accessToken: String){
        when(type){
            Consts.NEW_MESSAGE -> {
                val text = obj.get(Consts.TEXT).asString
                when(text){

                }
                sendMessage(text.replace("%","%25").replace(" ","%20"),obj.get(Consts.FROM_ID), accessToken)
            }
            Consts.JOIN -> {
                when(obj.get("join_type").asString){
                    "request" -> {sendMessage(Consts.WELCOME_MESSAGE_REQUEST.replace(" ","%20"),obj.get(Consts.USER_ID),accessToken)}
                    "approved" -> {sendMessage(Consts.WELCOME_MESSAGE_APPROVED,obj.get(Consts.USER_ID),accessToken)}
                    Consts.JOIN -> {sendMessage("Привет",obj.get(Consts.USER_ID),accessToken)}
                }
            }
        }
    }
}

fun main(args: Array<String>) {

}




