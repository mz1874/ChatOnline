package cn.org.bugcreator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cn.hutool.json.JSONUtil
import cn.org.bugcreator.util.OkHttpTool
import cn.org.bugcreator.vo.CommonResult
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

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
        var userName = findViewById<EditText>(R.id.editTextText)
        var password = findViewById<EditText>(R.id.editTextText2)

        loginButton.setOnClickListener {
            val usernameText = userName.text.toString()
            val passwordText = password.text.toString()
            val dataToSend = JSONObject().apply {
                put("username", usernameText)
                put("password", passwordText)
            }

            CoroutineScope(Dispatchers.IO).launch {
                val loginResult = OkHttpTool.post("http://192.168.0.4:8080/user/login", dataToSend.toString(), null)
                withContext(Dispatchers.Main) {
                }
                var resObj =  Gson().fromJson(loginResult, CommonResult::class.java)
                OkHttpTool.saveSession(resObj.message)
                val userInfo = OkHttpTool.get("http://192.168.0.4:8080/user/getUserName")
                withContext(Dispatchers.Main) {
                }
                val intent = Intent(this@MainActivity, IndexActivity::class.java)
                intent.putExtra("userName", userInfo)
                startActivity(intent)

            }
        }
    }
}
