package cn.org.bugcreator.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cn.org.bugcreator.LoginActivity
import cn.org.bugcreator.R
import cn.org.bugcreator.util.OkHttpTool
import cn.org.bugcreator.vo.CommonResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val accountEditText = findViewById<EditText>(R.id.account)
        val passwordEditText = findViewById<EditText>(R.id.editTextTextPassword)
        val secondPasswordEditText = findViewById<EditText>(R.id.SecondPassword)
        val addressEditText = findViewById<EditText>(R.id.editTextText4)
        val genderRadioGroup = findViewById<RadioGroup>(R.id.genderRadioGroup)
        val registerButton = findViewById<Button>(R.id.register_button)
        val resetButton = findViewById<Button>(R.id.reset_button)

        registerButton.setOnClickListener {
            val account = accountEditText.text.toString()
            val password = passwordEditText.text.toString()
            val secondPassword = secondPasswordEditText.text.toString()
            val address = addressEditText.text.toString()

            val selectedGenderId = genderRadioGroup.checkedRadioButtonId
            val selectedGenderRadioButton = findViewById<RadioButton>(selectedGenderId)
            val gender = selectedGenderRadioButton?.text.toString()

            if (account.isEmpty()) {
                Toast.makeText(this, "Account is required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (secondPassword.isEmpty()) {
                Toast.makeText(this, "Confirm Password is required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != secondPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (address.isEmpty()) {
                Toast.makeText(this, "Address is required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedGenderId == -1) {
                Toast.makeText(this, "Gender is required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val isMale = gender.equals("Male", ignoreCase = true)

            val dataToSend = JSONObject().apply {
                put("name", account)
                put("userPassword", password)
                put("address", address)
                put("gender",isMale)
            }
            val responseType = object : TypeToken<CommonResponse<Object>>() {}.type
            var resultCode = 0;
            CoroutineScope(Dispatchers.IO).launch {
                val result = OkHttpTool.post("http://192.168.0.4:8081/user/register", dataToSend.toString(), null)
                val resObj: CommonResponse<String> = Gson().fromJson(result, responseType)
                if (resObj.code == 200){
                    resultCode = 200;
                }
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RegisterActivity, "Register successful", Toast.LENGTH_SHORT).show()
                    if (resultCode == 200){
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                    }
                }
            }

        }
        resetButton.setOnClickListener {
            accountEditText.text.clear()
            passwordEditText.text.clear()
            secondPasswordEditText.text.clear()
            addressEditText.text.clear()
            genderRadioGroup.clearCheck()
            genderRadioGroup.check(R.id.radioMale)
        }
    }
}
