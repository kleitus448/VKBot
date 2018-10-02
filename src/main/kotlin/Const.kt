package Consts

import java.io.File
import java.io.FileReader
import java.util.*

//CONFIG
private fun getProperty(type: String): String {
    val properties = Properties()
    properties.load(FileReader(File("src/main/resources/config.properties")))
    return properties.getProperty(type)
}

val GROUP_ID = getProperty("groupId")
val ACCESS_TOKEN = getProperty("accessToken")
val URL_LOCALHOST = getProperty("url_localhost")
val URL_ENTER_QUEUE = getProperty("url_enter_queue")
val URL_GAME_LIST = getProperty("url_game_list")
val URL_RECORDS = getProperty("url_records")
val URL_QUIT_QUEUE = getProperty("url_quit_queue")

//MESSAGE ATTRIBUTES
const val DATE = "date"
const val FROM_ID = "from_id"
const val TEXT = "text"
const val OK = "ok"
const val USER_ID = "user_id"
const val PAYLOAD = "payload"

//TYPES OF EVENTS
const val NEW_MESSAGE = "message_new"
const val JOIN = "group_join"
const val REQUEST = "request"
const val APPROVED = "approved"

//ANSWER MESSAGES
const val WELCOME_MESSAGE_REQUEST = "Привет. Мы рады, что ты решил присоединиться к нам. В ближайшее время твой запрос будет рассмотрен."
const val WELCOME_MESSAGE_APPROVED = "ddddd"
const val WANNA_PLAY_ANSWER_MESSAGE = "Выбери игру из списка. Нажми на соответствующую кнопку."
const val SPECIAL_ANSWER = "No, fuck you, leather man"
const val DEFAULT_ANSWER = "Используйте клавиатуру для общения с ботом"
const val ADD_TO_QUEUE_ANSWER = "Вы были добавлены в очередь. Ваша текущая позиция: "

//COMANDS BY USER
const val WANNA_PLAY = "Хочу играть"
const val QUIT_QUEUE = "quit_game"
const val POSITION_LIST = "position_last"
const val SPECIAL_REQUEST = "Fuck yourself bitch"
const val GAME = "game"
const val SCORE = "score"
const val QUEUE = "queue"

//ATTACHMENTS SECTION
const val SPECIAL_ATTACH = "audio-170533307_456239019"
const val KEYBOARD = "keyboard"
const val KEYBOARD_RESULT = "keyboard_result"
const val STARTED_KEYBOARD = "started_keyboard"

//FLAGS FOR CHECK
const val FIRST_CONNECT = true



