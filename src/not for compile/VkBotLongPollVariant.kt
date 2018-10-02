import com.vk.api.sdk.callback.longpoll.CallbackApiLongPoll
import com.vk.api.sdk.client.actors.GroupActor
import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.httpclient.HttpTransportClient
import com.vk.api.sdk.objects.messages.Message

class CallbackApiLongPollHandler(vk: VkApiClient, private val groupActor: GroupActor) : CallbackApiLongPoll(vk, groupActor) {

    private fun sendMessage(message: Message) {
        client.messages().send(groupActor, message.userId)
                .message(message.body).execute()
        println("QKRQ")
        println(message)
    }

    override fun messageNew(groupId: Int?, message: Message) {
        try {
            sendMessage(message)
        }
        catch (e: NullPointerException) {}
    }
}

fun main(args: Array<String>) {
    val vk = VkApiClient(HttpTransportClient.getInstance())
    val groupActor = GroupActor(Consts.GROUP_ID.toInt(), Consts.ACCESS_TOKEN)
    val handler = CallbackApiLongPollHandler(vk, groupActor)
    handler.run()
}


