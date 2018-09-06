import com.google.gson.Gson
import com.google.gson.*
import java.io.File
import java.io.FileReader
import java.net.URL

//КЛАСС ДЛЯ ОБРАБОТКИ JSON-ФАЙЛА
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

    //Конструирование меню с играми
    fun keyboardConstructor(nameBoard :String): String? {
        val name = "src/main/resources/${nameBoard}.json"
        val result = FileReader(File(name)).readLines()
        return result.fold("") {acc, i -> acc + i}.replace(" ", "")
    }
}

//ОТПРАВКА СООБЩЕНИЯ
fun sendMessage(text:String, id:JsonElement, accessToken: String, music: String ="", isFirstConnect: Boolean = false){
    val keyboard: String = if (isFirstConnect) "keyboard" else "started_keyboard"
    val reqUrl = "https://api.vk.com/method/messages.send?" +
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

    //Формирует ответы на события c Callback Server'а
    fun switch(type: String, obj: JsonObject, accessToken: String){
        when(type){

            //Получение нового сообщения
            Consts.NEW_MESSAGE -> {
                val text = obj.get(Consts.TEXT).asString
                println(text)
                when(text){
                    //Запрос "Хочу играть"
                    Consts.WANNA_PLAY ->
                        sendMessage(forRequests(Consts.WANNA_PLAY_ANSWER_MESSAGE),
                                    obj.get(Consts.FROM_ID),
                                    accessToken)

                    //Особый запрос =)
                    Consts.SPECIAL_REQUEST ->
                        sendMessage(forRequests(Consts.SPECIAL_ANSWER),
                                    obj.get(Consts.FROM_ID),
                                    accessToken,
                                    Consts.SPECIAL_ATTACH)
                }

                //Стандартный ответ
                sendMessage(forRequests(Consts.DEFAULT_ANSWER),
                            obj.get(Consts.FROM_ID),
                            accessToken)
            }

            //Вступление в группу с ботом
            Consts.JOIN -> {
                when(obj.get("join_type").asString){

                    //Подача заявки
                    Consts.REQUEST ->
                        sendMessage(forRequests(Consts.WELCOME_MESSAGE_REQUEST),
                                    obj.get(Consts.USER_ID),
                                    accessToken)

                    //Одобрение заявки
                    Consts.APPROVED ->
                        sendMessage(Consts.WELCOME_MESSAGE_APPROVED,
                                    obj.get(Consts.USER_ID),
                                    accessToken)

                    //Вступление в группу
                    Consts.JOIN ->
                        sendMessage("Привет",
                                    obj.get(Consts.USER_ID),
                                    accessToken,
                                    isFirstConnect = true)
                }
            }
        }
    }
}



