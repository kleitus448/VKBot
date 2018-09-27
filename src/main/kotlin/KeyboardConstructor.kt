package KeyboardConstuctor

import org.json.simple.JSONArray
import java.io.File
import java.io.FileReader
import java.util.Collections.*
import org.json.simple.JSONObject
import java.util.*
import kotlin.collections.HashMap
import kotlin.concurrent.getOrSet

    //Конструирование меню с играми
        fun keyboardConstructor(nameBoard: String): String? {
        val name = "src/main/resources/${nameBoard}.json"
        val result = FileReader(File(name)).readLines()
        return result.fold("") { acc, i -> acc + i }.replace(" ", "")
    }

    @Synchronized
    fun  gameContructor(Array: ArrayList<String>): JSONObject {
        var map = HashMap<String,String>()
        var i = 1

        for (s in Array) {
            map.set(s, "{\"button\": \"" + i + "\"}")
            i++
        }

        var Arr = JSONArray()

        for (s in map) {
            var obj = JSONObject()
            obj.put("type", "text")
            obj.put("payload", s.value)
            obj.put("label", "\"" + s.key + "\"")

            var arr = JSONArray()
            arr.add(obj)

            var OBJ = JSONObject()
            OBJ.put("action", arr)
            OBJ.put("color", "negative")

            Arr.add(OBJ)
        }


        var keybaord = JSONObject()
        keybaord.put("one_time", false)
        keybaord.put("buttons", Arr)

        println(keybaord.toString())

        return keybaord

    }


    fun main(args: Array<String>) {
        var a = ArrayList<String>()
        a.add("DOTA")
        a.add("LOL")
        gameContructor(a)
    }

/*              *********       ***********       ***********      **********          *****       ***********       **         *        *      *       *       *     *      *         *****
                *                    *            *         *               *         *     *      *                *  *        *        *      *       *       *     *     *         *     *
                *                    *            *         *               *        *       *     *               *    *       *        *      *       *       *     *    *         *       *
                *********            *            *         *      **********       ***********    *              *      *      **********      *       *       *     * * *         ***********
                *                    *            *         *               *       *         *    *             *        *              *      *       *       *     *    *        *         *
                *                    *            *         *               *       *         *    *            *          *             *      *       *       *     *     *       *         *
                *********            *            ***********      **********       *         *    *            *          *    **********      *****************     *      *      *         *
                */