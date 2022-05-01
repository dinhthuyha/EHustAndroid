package com.prdcv.ehust

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.prdcv.ehust.ui.login.LoginFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.layout,LoginFragment()).commit()
    }


}