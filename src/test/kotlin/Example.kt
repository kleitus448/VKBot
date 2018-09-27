//import com.vk.api.sdk.callback.longpoll.CallbackApiLongPoll
//import com.vk.api.sdk.client.VkApiClient
//import com.vk.api.sdk.client.actors.GroupActor
//import com.vk.api.sdk.httpclient.HttpTransportClient
//
//var vk = VkApiClient(HttpTransportClient.getInstance())
//
//fun main(args : Array<String>) {
////    var transportClient : TransportClient = HttpTransportClient.getInstance()
//    val groupActor: GroupActor = GroupActor(170533307,
//            "720f1c5d6bc37a7af3c81e16cc47f168481be99fa1d8dfbee7ad336a2e384a6a7379c5f5d65e45a2853a3")
//    //getVk.messages().send(groupActor, 40657539).message("Like a boss")
//    //var listMessage: List<Message> = getVk.messages().getHistory(groupActor).userId(40657539).execute().items
//    //while (true) {
//        var ts = vk.groups().getLongPollServer(groupActor).execute().ts
//        var msg_id = vk.groups().getLongPollServer(groupActor).execute()
//        //var pts = vk.groups().getLongPollServer(groupActor).execute().
//        //var key = vk.groups().getLongPollServer(groupActor).execute().key
//        //var server = vk.groups().getLongPollServer(groupActor).execute().server
//        var callbackLongPoll = CallbackApiLongPoll(vk, groupActor, 25)
//            callbackLongPoll.run()
//        val messages = vk.messages().getLongPollHistory(groupActor)
//                .ts(ts).maxMsgId()
//                .onlines(false)
//                .previewLength(0)
//                .execute()
//        /*for (i in messages) {
//            vk.messages().send(groupActor, i.userId).message(i.body)
//        }*/
//    //}
//}
//        //val messages = getVk.groups().getLongPollSettings(groupActor).execute().events.
//        //print(getVk.messages().getLongPollServer(groupActor).execute().ts)
//    //print("https://$server?act=a_check&key=$key&ts=$ts&wait=10")
//    //print(vk.transportClient.get("$server?act=a_check&key=$key&ts=$ts&wait=25").content)
//        //val messages = getVk.messages().getLongPollHistory(groupActor).execute().messages.messages.filter { !it.isReadState
////    print(groupActor.groupId)
////    getVk.
////    val userIds = getVk.groups().getMembers(groupActor).execute()
////    print(userIds)
////        val messages = getVk.messages().getHistory(groupActor).execute().unread
////        print(messages)
//        //val new_dialogs = getVk.messages().getDialogs(groupActor).execute()
////        print(messages.size)
////        if (messages.isNotEmpty())
////        {
////            for (i in messages) {
////                getVk.messages().send(groupActor, i.userId).message(i.body)
////            }
////        }
//    //}
//    //for (i in listMessage) {
//    //    println(i.body)
//    //}
//    //print(getVk.messages().get(groupActor).count(3).execute())
//
////            fun sendMessage(message: String, userId: String) {
////
////}