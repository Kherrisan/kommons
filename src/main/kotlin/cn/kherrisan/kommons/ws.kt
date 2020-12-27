package cn.kherrisan.kommons

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import okhttp3.*
import okio.ByteString
import okio.ByteString.Companion.toByteString
import java.nio.ByteBuffer

@ExperimentalStdlibApi
class Websocket(
    url: String = "ws://localhost",
    private val sessionConfiguraBlock: WebsocketSession.() -> Unit
) {

    private val session = DefaultWebsocketSession()
    private val client = OkHttpClient()
    private val ws: WebSocket
    private val channel = Channel<ByteBuffer>(Channel.BUFFERED)

    init {
        val req = Request.Builder().url(url).build()
        ws = client.newWebSocket(req, Listener())
    }

    inner class Listener : WebSocketListener() {
        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            val buf = ByteBuffer.wrap(text.encodeToByteArray())
            channel.offer(buf)
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            channel.offer(bytes.asByteBuffer())
        }

        override fun onOpen(webSocket: WebSocket, response: Response) {
            session.sessionConfiguraBlock()
        }
    }

    interface WebsocketSession {
        fun send(buf: ByteBuffer)
        suspend fun receive(): ByteBuffer
    }

    inner class DefaultWebsocketSession : WebsocketSession, CoroutineScope by GlobalScope {
        override fun send(buf: ByteBuffer) {
            ws.send(buf.array().toByteString())
        }

        override suspend fun receive(): ByteBuffer {
            return channel.receive()
        }
    }
}

@ExperimentalStdlibApi
fun main() {
    Websocket("wss://broadcastlv.chat.bilibili.com:2245/sub") {

    }
}