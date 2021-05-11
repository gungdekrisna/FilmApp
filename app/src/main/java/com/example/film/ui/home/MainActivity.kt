package com.example.film.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.film.R
import com.example.film.databinding.ActivityMainBinding
import com.example.film.ui.favorite.favoriteHome.FavoriteActivity

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_favorite -> {
                val favorite = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(favorite)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}