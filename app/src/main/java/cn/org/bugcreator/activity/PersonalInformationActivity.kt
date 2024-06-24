package cn.org.bugcreator.activity

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cn.hutool.json.JSONUtil
import cn.org.bugcreator.R
import cn.org.bugcreator.vo.UserEntity
import com.bumptech.glide.Glide

class PersonalInformationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_personal_information)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val intent = intent
        val name = intent.getStringExtra("name");
        val imageSrc = intent.getStringExtra("imageSrc");
        val address = intent.getStringExtra("address");
        val gender = intent.getBooleanExtra("gender", true);

        var nameView = findViewById<TextView>(R.id.textView3)
        var backView = findViewById<ImageView>(R.id.imageView2)
        var imageView = findViewById<ImageView>(R.id.imageView3)
        var addressView = findViewById<TextView>(R.id.addressinfo)
        var genderView = findViewById<TextView>(R.id.genderinfo)

        val gender_result = if (gender) "Male" else "Female"

        nameView.text = name
        addressView.text = address
        genderView .text = gender_result

        backView.setOnClickListener {
            finish()
        }

        Glide.with(this)
            .load(imageSrc)
            .into(imageView)
    }
}