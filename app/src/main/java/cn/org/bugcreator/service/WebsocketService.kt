// WebSocketService.kt

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import okhttp3.*
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class WebSocketService : Service() {

    private val binder = LocalBinder()
    private var webSocket: WebSocket? = null
    private val client = OkHttpClient()

    inner class LocalBinder : Binder() {
        fun getService(): WebSocketService = this@WebSocketService
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("WebSocketService", "onCreate")
    }

    fun connectWebSocket(message: String) {
        val request =
            Request.Builder().url("ws://192.168.0.4:8081/socket/${message}").addHeader("Session", message)
                .build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                Log.i("WebSocketService", "onOpen")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                Log.i("WebSocketService", "onMessage")
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
                Log.i("WebSocketService", "onMessage")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                Log.i("WebSocketService", "onClosing: $reason")
                webSocket.close(1000, null)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                Log.i("WebSocketService", "onClosed: $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                Log.i("WebSocketService", "onFailure: ${t.message}")
            }
        })
    }

    fun sendMessage(message: String) {
        webSocket?.send(message)
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocket?.close(1000, "Service stopped")
        Log.i("WebSocketService", "onDestroy")
    }
}
