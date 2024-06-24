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
import cn.org.bugcreator.activity.MainActivity
import cn.org.bugcreator.activity.RegisterActivity
import cn.org.bugcreator.util.OkHttpTool
import cn.org.bugcreator.vo.CommonResponse
import cn.org.bugcreator.vo.UserVo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import okhttp3.*
import okio.ByteString
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val loginButton = findViewById<Button>(R.id.login)
        val register = findViewById<Button>(R.id.register)
        register.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
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
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    showToast(resObj.message)
                }

            }
        }
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
        }
    }



}
