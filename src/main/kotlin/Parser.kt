import com.google.gson.Gson
import com.google.gson.JsonObject
import org.apache.commons.io.IOUtils
import org.json.simple.JSONObject
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL
import KeyboardConstuctor.*
import Consts.*
import com.google.gson.JsonArray

//ОБРАБОТКА JSON-ФАЙЛА
@Synchronized
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

//ОТПРАВКА СООБЩЕНИЯ
@Synchronized
fun sendMessage(
        text: String,
        id: String,
        currentKeyboard: String = "",
        attach : String = ""
){
    val keyboard = "&keyboard=$currentKeyboard"
    val reqUrl = "https://api.vk.com/method/messages.send?" +
                 "user_ids=$id" +
                 "&message=$text" +
                 "&attachment=$attach"+
                 "&v=5.80" +
                 "&access_token=$ACCESS_TOKEN" +
                  keyboard
    println(reqUrl)
    URL(reqUrl).openStream().close()
}

//ДОСТАТЬ ИМЯ И ФАМИЛИЮ ПОЛЬЗОВАТЕЛЯ VK
@Synchronized
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


@Synchronized
fun forRequests(string: String):String{
    return string.replace("%","%25").replace(" ","%20")
}

class Switcher{
    @Synchronized
    private fun sendPOSTToBackEnd(ID: String = "",
                                  GAME: String = "",
                                  NAME: String = "",
                                  requestURL: String = "") : String {
        val obj = JSONObject()
        obj["ID"] = ID
        obj["GAME"] = GAME
        obj["NAME"] = NAME
        val url = URL("http://$URL_LOCALHOST$requestURL/")
        val urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.requestMethod = "POST"
        urlConnection.doOutput = true
        val wr = DataOutputStream(urlConnection.outputStream)
        wr.writeBytes(obj.toJSONString())
        wr.flush()
        wr.close()
        return IOUtils.toString(urlConnection.inputStream, Charsets.UTF_8)
    }

    //Формирует ответы на события c Callback Server'а
    @Synchronized
    fun switch(type: String, obj: JsonObject){
        val id = obj.get(FROM_ID).asString
        when(type){
            //Получение нового сообщения
            NEW_MESSAGE -> {
                print(obj.toString() + "\n")

                val dataUser = getUsernameVK(id)
                val name = dataUser.getAsJsonPrimitive("first_name").toString() + " " +
                           dataUser.getAsJsonPrimitive("last_name") .toString()
                try {
                    val reqType = obj.get(PAYLOAD).asString.split("\"")[1]
                    val game = obj.get(PAYLOAD).asString.split("\"")[3]
                    println("$reqType $game")
                    when (reqType) {
                        WANNA_PLAY -> {
                            val gameArr = ArrayList<String>();
                            val response = jsonConstructor("games.json")
                            val jsonArray = Gson().fromJson(response, JsonObject::class.java).getAsJsonArray("games");
                            for (i in jsonArray) {gameArr.add(i.asJsonObject.get("gamename").asString)}
                            sendMessage(forRequests(WANNA_PLAY_ANSWER_MESSAGE), id,
                                    currentKeyboard = gameConstructor(gameArr, GAME).toString()
                            )
                        }

                        QUIT_QUEUE -> {
                            //TODO("Запрос на список игр")
                            Switcher().sendPOSTToBackEnd(id, game, name, URL_QUIT_QUEUE)
                            sendMessage(forRequests(WANNA_PLAY_ANSWER_MESSAGE), id,
                                    currentKeyboard = jsonConstructor(STARTED_KEYBOARD)!!
                            )
                        }

                        /*POSITION_LIST -> {
                            TODO("Запрос на список игр")
                            Switcher().sendPOSTToBackEnd(requestURL = URL_)
                            sendMessage(forRequests(WANNA_PLAY_ANSWER_MESSAGE), id,
                                    currentKeyboard = jsonConstructor(STARTED_KEYBOARD)!!
                            )
                        }*/

                        GAME -> {
                            //TODO("Отправка запроса на backend для постановки в очередь")
                            Switcher().sendPOSTToBackEnd(id, game, name, URL_ENTER_QUEUE)
                            sendMessage(forRequests(ADD_TO_QUEUE_ANSWER), id,
                                    currentKeyboard = jsonConstructor(STARTED_KEYBOARD)!!
                            )
                        }

                        SCORE -> {
                            //TODO("Отправка запроса на backend для вывода счёта по конкретной игре")
                            Switcher().sendPOSTToBackEnd(id, NAME = name, requestURL = URL_RECORDS)
                            sendMessage(forRequests(ADD_TO_QUEUE_ANSWER), id,
                                    currentKeyboard = jsonConstructor(STARTED_KEYBOARD)!!
                            )
                        }

                        //QUEUE -> TODO("Отправка запроса на backend для вывода позиции в очереди для конкретной игры")

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    sendMessage(forRequests(DEFAULT_ANSWER), id,
                            currentKeyboard = jsonConstructor(STARTED_KEYBOARD).toString())
                }

            }
            //Вступление в группу с ботом
            JOIN -> {
                when(obj.get("join_type").asString) {

                    //Подача заявки
                    REQUEST -> sendMessage(forRequests(WELCOME_MESSAGE_REQUEST), id)

                    //Одобрение заявки
                    APPROVED -> sendMessage(forRequests(WELCOME_MESSAGE_APPROVED), id)

                    //Вступление в группу
                    JOIN -> sendMessage("Привет", id, currentKeyboard = STARTED_KEYBOARD)
                }
            }
        }
    }
}
//Ответ на последние сообщения, пока бот был неактивен
fun responseLatest() {
    val reqUrl = "https://api.vk.com/method/messages.getConversations?" +
            "filter=unread" +
            "&access_token=$ACCESS_TOKEN" +
            "&v=5.80"
    val str = IOUtils.toString(URL(reqUrl).openStream(), Charsets.UTF_8)
    val json = Gson().fromJson(str, JsonObject::class.java)
    val items: JsonArray?
    try {
        items = json.getAsJsonObject("response").getAsJsonArray("items")
        for (item in items) {
            Switcher().switch(NEW_MESSAGE, item.asJsonObject.get("last_message").asJsonObject)
        }
    } catch (e: NullPointerException) {}
}

fun main(args: Array<String>) {
    println(getUsernameVK("1"))
}




