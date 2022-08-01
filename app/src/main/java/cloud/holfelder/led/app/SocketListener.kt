package cloud.holfelder.led.app

import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class SocketListener : WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: Response) {
        println("Response: ${response.message()}")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        println("Message: $text")
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        println("Message: $bytes")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        println("Connection closing!")
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        println("Connection closed: $code, $reason!")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        println("Connection failed!")
        println(t)
        println(response)
    }
}