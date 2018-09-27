import kotlinx.coroutines.experimental.*

fun main(args: Array<String>) = runBlocking<Unit> {
    val job = launch {
        doWorld()
    }
    println("Hello,")
    job.join()
}

suspend fun doWorld() {
    delay(1000L)
    println("World!")
}