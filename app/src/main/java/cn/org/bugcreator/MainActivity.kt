package cn.org.bugcreator

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cn.org.bugcreator.activity.IndexActivity
import cn.org.bugcreator.activity.RegisterActivity
import cn.org.bugcreator.util.OkHttpTool
import cn.org.bugcreator.vo.CommonResponse
import cn.org.bugcreator.vo.UserVo
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import okhttp3.*
import okio.ByteString
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var webSocket: WebSocket
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val loginButton = findViewById<Button>(R.id.login)
        val register = findViewById<Button>(R.id.register)
        register.setOnClickListener {
            val intent = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
        val userName = findViewById<EditText>(R.id.editTextText)
        val password = findViewById<EditText>(R.id.editTextText2)

        loginButton.setOnClickListener {
            val usernameText = userName.text.toString()
            val passwordText = password.text.toString()
            val dataToSend = JSONObject().apply {
                put("name", usernameText)
                put("userPassword", passwordText)
            }

            CoroutineScope(Dispatchers.IO).launch {
                val loginResult = OkHttpTool.post(
                    "http://192.168.0.4:8081/user/login",
                    dataToSend.toString(),
                    null
                )
                withContext(Dispatchers.Main) {
                }
                val responseType = object : TypeToken<CommonResponse<UserVo>>() {}.type
                val resObj: CommonResponse<UserVo> = Gson().fromJson(loginResult, responseType)
                if (resObj.code == 200) {
                    OkHttpTool.saveSession(resObj.data.session)
                    val userInfo = OkHttpTool.get("http://192.168.0.4:8081/user/getUserName")
                    withContext(Dispatchers.Main) {
                    }
                    connectWebSocket(resObj.data.name)
                    val intent = Intent(this@MainActivity, IndexActivity::class.java)
                    intent.putExtra("userName", userInfo)
                    intent.putExtra("JSON", OkHttpTool.getSession())
                    startActivity(intent)
                } else {
                    showToast(resObj.message)
                }

            }
        }
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun connectWebSocket(message: String) {
        val request =
            Request.Builder().url("ws://192.168.0.4:8081/socket/${message}").addHeader("Session", message)
                .build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                // Connection opened, handle it here
                Log.i("DEBUG", "OPEN")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                Log.i("DEBUG", "onMessage")
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
                Log.i("DEBUG", "onMessage")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                Log.i("DEBUG", "${reason}")
                Log.i("DEBUG", "onClosing")
                webSocket.close(1000, null)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.i("DEBUG", "onClosing")
                Log.i("DEBUG", "${reason}")
                super.onClosed(webSocket, code, reason)
                // Connection closed, handle it here
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.i("DEBUG", "onClosing")
                Log.i("DEBUG", "${response}")
                super.onFailure(webSocket, t, response)
                // Connection failed, handle it here
            }
        })
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        webSocket.close(1000, null)
//    }
}
