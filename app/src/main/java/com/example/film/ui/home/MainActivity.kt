package com.example.film.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.film.R
import com.example.film.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val actiivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(actiivityMainBinding.root)

        val sectionsPagerAdapter = SectionPagerAdapter(this, supportFragmentManager)
        actiivityMainBinding.viewPager.adapter = sectionsPagerAdapter
        actiivityMainBinding.tabs.setupWithViewPager(actiivityMainBinding.viewPager)

        supportActionBar?.elevation = 0f
    }
}