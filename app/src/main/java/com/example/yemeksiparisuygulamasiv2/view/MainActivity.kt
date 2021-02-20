package com.example.yemeksiparisuygulamasiv2.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import com.example.yemeksiparisuygulamasiv2.R

class MainActivity : AppCompatActivity() {

    private lateinit var navigationController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

}