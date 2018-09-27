import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.apache.commons.io.IOUtils
import org.json.simple.JSONObject
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.InetAddress
import java.net.URL
import KeyboardConstuctor.*

//КЛАСС ДЛЯ ОБРАБОТКИ JSON-ФАЙЛА
class Parser {
    fun parse(Query:String) {
        val json = Gson().fromJson(Query, JsonObject::class.java)
        val type = json.get("type").asString
        val obj = json.get("object").asJsonObject
        try {
            Switcher().switch(type,obj)
        } catch (e:Exception){
            e.printStackTrace()
        }
    }
}

//ОТПРАВКА СООБЩЕНИЯ
fun sendMessage(
        text: String,
        id: JsonElement,
        currentKeyboard: String = "",
        attach : String = ""
){
    var keyboard = ""
    if (!currentKeyboard.isEmpty()) {
        keyboard = "&keyboard=" + keyboardConstructor(currentKeyboard)
    }

    val reqUrl = "https://api.vk.com/method/messages.send?" +
                 "user_ids=${id}" +
                 "&message=${text}" +
                 "&attachment=${attach}"+
                 "&v=5.80" +
                 "&access_token=${Consts.ACCESS_TOKEN}" +
                  keyboard
    println(reqUrl)
    URL(reqUrl).openStream().close()
}

//ДОСТАТЬ ИМЯ И ФАМИЛИЮ ПОЛЬЗОВАТЕЛЯ VK
fun getUsernameVK(user_id: String): JsonObject {
    val reqUrl = "https://api.vk.com/method/users.get?" +
            "user_ids=$user_id" +
            "&fields=first_name" +
            "&access_token=${Consts.ACCESS_TOKEN}" +
            "&v=5.80"
    val str = IOUtils.toString(URL(reqUrl).openStream(), Charsets.UTF_8)
    val json = Gson().fromJson(str, JsonObject::class.java)
    return json.getAsJsonArray("response").get(0).asJsonObject
}

fun forRequests(string: String):String{
    return string.replace("%","%25").replace(" ","%20")
}

class Switcher{

    private fun sendPOSTToBackEnd(ID : String, GAME: String, NAME: String){
        val obj = JSONObject()
        obj["ID"] = ID
        obj["GAME"] = GAME
        obj["NAME"] = NAME
        val url = URL("http://${InetAddress.getLocalHost().hostAddress}:80/")
        val urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.requestMethod = "POST"
        urlConnection.doOutput = true
        val wr = DataOutputStream(urlConnection.outputStream)
        wr.writeBytes(obj.toJSONString())
        wr.flush()
        wr.close()
        urlConnection.inputStream.readBytes()// Подтверждаю !
    }

    //Формирует ответы на события c Callback Server'а
    fun switch(type: String, obj: JsonObject){
        when(type){
            //Получение нового сообщения
            Consts.NEW_MESSAGE -> {
                print(obj.toString())
                val text = obj.get(Consts.TEXT).asString
                val id = obj.get(Consts.FROM_ID).asString
                val game = ""//obj.get().asString
                val name = ""//getUsernameVK(id)
                when(text){
                    //Запрос "Хочу играть"
                    Consts.WANNA_PLAY ->
                        Switcher().sendPOSTToBackEnd(id, game, name)



                    //Особый запрос =)
                    Consts.SPECIAL_REQUEST ->
                        sendMessage(forRequests(Consts.SPECIAL_ANSWER),
                                    obj.get(Consts.FROM_ID),
                                    attach = Consts.SPECIAL_ATTACH)

                    //Стандартный ответ
                    else ->
                        sendMessage(forRequests(Consts.DEFAULT_ANSWER),
                                    obj.get(Consts.FROM_ID),
                                    currentKeyboard = Consts.KEYBOARD)
                }

                if (obj.has("payload")) {}

            }

            //Вступление в группу с ботом
            Consts.JOIN -> {
                when(obj.get("join_type").asString){
                    //Подача заявки
                    Consts.REQUEST ->
                        sendMessage(forRequests(Consts.WELCOME_MESSAGE_REQUEST),
                                    obj.get(Consts.USER_ID))

                    //Одобрение заявки
                    Consts.APPROVED ->
                        sendMessage(forRequests(Consts.WELCOME_MESSAGE_APPROVED),
                                    obj.get(Consts.USER_ID))
                    //Вступление в группу
                    Consts.JOIN ->
                        sendMessage("Привет",
                                    obj.get(Consts.USER_ID),
                                    currentKeyboard = Consts.STARTED_KEYBOARD)
                }
            }
        }
    }
}

/*sendMessage(forRequests(Consts.WANNA_PLAY_ANSWER_MESSAGE),
            obj.get(Consts.FROM_ID),
            Consts.ACCESS_TOKEN)*/
fun main(args: Array<String>) {
    println(getUsernameVK("44796262"))
}