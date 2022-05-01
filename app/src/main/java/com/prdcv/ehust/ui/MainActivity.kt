package com.prdcv.ehust.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.prdcv.ehust.R
import com.prdcv.ehust.ui.main.MainFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(android.R.id.content, MainFragment.newInstance()).commit()
    }


}