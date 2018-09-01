import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.GroupActor
import com.vk.api.sdk.httpclient.HttpTransportClient
import java.io.File
import java.io.FileReader
import java.util.*

fun main(args: Array<String>) {
    val vk = VkApiClient(HttpTransportClient.getInstance())
    val name = "src/main/resources/config.properties"
    val properties = Properties()
    properties.load(FileReader(File(name)))
    val groupId = properties.getProperty("groupId")
    val accessToken = properties.getProperty("accessToken")
    val groupActor = GroupActor(groupId.toInt(), accessToken)
    val id = vk.groups().getCallbackServers(groupActor).execute().items[0].id
    //vk.groups().editCallbackServer(groupActor, id, "https://46.188.5.29:8079", "BotServer").execute()
}