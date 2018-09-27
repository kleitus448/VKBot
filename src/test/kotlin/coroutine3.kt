import kotlinx.coroutines.experimental.*

fun main(args: Array<String>) = runBlocking<Unit> {
    launch {
        repeat(1000) { a: Int ->
            println("I'm sleeping $a ...")
            delay(500L)
        }
    }
    delay(1300L) // just quit after delay
    repeat(3) {a: Int -> print("$a")}
}