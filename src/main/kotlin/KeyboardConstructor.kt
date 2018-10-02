package KeyboardConstuctor

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import java.io.File
import java.io.FileReader
import java.util.*
import kotlin.collections.HashMap

//Конструирование меню с играми
fun jsonConstructor(nameBoard: String): String? {
    val name = "src/main/resources/$nameBoard.json"
    val result = FileReader(File(name)).readLines()
    return result.fold("") { acc, i -> acc + i }.replace(" ", "")
}

@Synchronized
fun gameConstructor(Array: ArrayList<String>, type: String): JSONObject {

    var i = 1
    val arr = JSONArray()
    for (s in Array) {
        val obj = JSONObject()
        val payload = JSONObject()
        payload.put(type, s)
        obj.put("type", "text")
        obj.put("payload", payload.toString())
        obj.put("label", s)

        val array = JSONArray()
        array.add(obj)

        val obj1 = JSONObject()
        obj1.put("action", obj)
        obj1.put("color", "negative")

        arr.add(obj1)
    }

    val ArrArr = JSONArray()
    ArrArr.add(arr)

    val keyboard = JSONObject()
    keyboard.put("one_time", false)
    keyboard.put("buttons", ArrArr)

    println(keyboard.toString())

    return keyboard

}


fun main(args: Array<String>) {
    val a = ArrayList<String>()
    a.add("DOTA")
    a.add("LOL")
    gameConstructor(a, "game")
}

