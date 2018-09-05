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
    
    fun keyboardConstructor(nameBoard :String): String? {
        val name = "src/main/resources/${nameBoard}.json"
        val result = FileReader(File(name)).readLines()
        return result.fold("") {acc, i -> acc + i}.replace(" ", "")
    }
}

//ОТПРАВКА СООБЩЕНИЯ
fun sendMessage(text:String, id:JsonElement, accessToken: String, music: String ="", isFirstConnect: Boolean = false){
    var keyboard: String
    if (isFirstConnect){
        keyboard = "keyboard"
    }else{keyboard = "started_keyboard"}

    val reqUrl =    "https://api.vk.com/method/messages.send?" +
                    "user_ids=${id}" +
                    "&message=${text}" +
                    "&attachment=${music}"+
                    "&v=5.52" +
                    "&access_token=${accessToken}" +
                    "&keyboard=${Parser().keyboardConstructor(keyboard).toString()}"
    println(reqUrl)
    URL(reqUrl).openStream().close()
}

fun forRequests(string: String):String{
    return string.replace("%","%25").replace(" ","%20")
}

class Switcher{
    fun switch(type: String, obj: JsonObject, accessToken: String){
        when(type){
            Consts.NEW_MESSAGE -> {
                val text = obj.get(Consts.TEXT).asString
                println(text)
                when(text){
                    Consts.WANNA_PLAY -> {sendMessage(forRequests(Consts.WANNA_PLAY_ANSWER_MESSAGE),obj.get(Consts.FROM_ID),accessToken)}
                    Consts.SPECIAL_REQUEST -> {sendMessage(forRequests("No, fuck you, letherman"),obj.get(Consts.FROM_ID),accessToken, "audio107563776_456239097")}
                }
                sendMessage(forRequests(Consts.DEFAULT_ANSWER),obj.get(Consts.FROM_ID), accessToken)
            }
            Consts.JOIN -> {
                when(obj.get("join_type").asString){
                    Consts.REQUEST -> {sendMessage(Consts.WELCOME_MESSAGE_REQUEST.replace(" ","%20"),obj.get(Consts.USER_ID),accessToken)}
                    Consts.APPROVED -> {sendMessage(Consts.WELCOME_MESSAGE_APPROVED,obj.get(Consts.USER_ID),accessToken)}
                    Consts.JOIN -> {sendMessage("Привет",obj.get(Consts.USER_ID),accessToken,isFirstConnect = true)}
                }
            }
        }
    }
}

fun main(args: Array<String>) {

}


//"attachments":[{"type":"audio","audio":{"id":456239097,"owner_id":107563776,"artist":"Баста","title":"Моя игра","duration":270,"date":1535839677,"url":"https:\/\/cs1-63v4.vkuseraudio.net\/p1\/59febe5911442f.mp3?extra=DGWShYlW5q0EZ5OEsfGn5KRBXQC6_QoB23FtXO8HLwpfnfc87qNTm72L4b-A472uvqmpNNbTzNOGf85P4I7KHnq2cHtV4X79NcLg1NRVNjklL62np29zdUdsu7XosHTS3WAdumMazNGn3DE","is_hq":true}}],"is_hidden":false}



