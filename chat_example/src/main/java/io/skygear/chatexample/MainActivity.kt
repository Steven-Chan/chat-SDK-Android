package io.skygear.chatexample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import io.skygear.skygear.Container
import io.skygear.skygear.User

class MainActivity : AppCompatActivity() {
    private val LOG_TAG: String? = "MainActivity"

    private var mSkygear: Container? = null
    private var mSignUpBtn: Button? = null
    private var mLogInBtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSignUpBtn = findViewById(R.id.sign_up_btn) as Button?
        mLogInBtn = findViewById(R.id.log_in_btn) as Button?

        mSignUpBtn?.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        mLogInBtn?.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
        }

        mSkygear = Container.defaultContainer(this)
    }

    override fun onResume() {
        super.onResume()

        val currentUser: User? = mSkygear?.getCurrentUser()
        if (currentUser != null) {
            startActivity(Intent(this, ConversationsActivity::class.java))

            finish()
        }
    }


}
