import com.vk.api.sdk.callback.longpoll.CallbackApiLongPoll
import com.vk.api.sdk.client.actors.GroupActor
import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.httpclient.HttpTransportClient
import com.vk.api.sdk.objects.messages.Message
import java.io.File
import java.io.FileReader
import java.io.InputStream
import java.util.*

class CallbackApiLongPollHandler(vk: VkApiClient, private val groupActor: GroupActor) : CallbackApiLongPoll(vk, groupActor) {

    private fun sendMessage(message: Message) {
        client.messages().send(groupActor, message.userId)
                .message(message.body).execute()
        println("QKRQ")
        println(message)
    }

    override fun messageNew(groupId: Int?, message: Message) {
        //print(message.attachments[0].)
        try {
            sendMessage(message)
//            for (attachment in message.attachments) {
//                println(attachment.toString())
//            }
//            client.messages().send(groupActor, message.userId)
//                    .message(message.body).attachment(message.attachments.fold(""
//                    ) { total, next -> total + next.toString() })
//                    .execute()

        }
        catch (e: NullPointerException) {}
    }
}

fun main(args: Array<String>) {
    val vk = VkApiClient(HttpTransportClient.getInstance())
    val name = "src/main/resources/config.properties"
    val properties = Properties()
    properties.load(FileReader(File(name)))
    val groupId = properties.getProperty("groupId")
    val accessToken = properties.getProperty("accessToken")
    val groupActor = GroupActor(groupId.toInt(), accessToken)
    val handler = CallbackApiLongPollHandler(vk, groupActor)
    handler.run()
}

/*fun accumulateAttachments() {
   TODO: Regular Expressions for Message
}*/

