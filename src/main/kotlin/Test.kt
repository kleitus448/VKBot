import org.apache.http.NameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.BasicNameValuePair
import java.io.DataInputStream
import java.io.InputStream
import java.util.ArrayList



fun main(args: Array<String>) {
    val url = "http://192.168.1.38:80"

    val client = DefaultHttpClient()
    val post = HttpPost(url)

    // add header
    post.setHeader("User-Agent", "Mozilla/4.0")
    val urlParameters = ArrayList<NameValuePair>()
    urlParameters.add(BasicNameValuePair("21","223"))
    post.entity = UrlEncodedFormEntity(urlParameters)
    client.close()
}