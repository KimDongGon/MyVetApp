package com.example.myvetapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        var fragment = supportFragmentManager.findFragmentById(R.id.frame)
        if(fragment == null){
            var Transaction = supportFragmentManager.beginTransaction()
            var Fragment = FindDiseaseFragment()
            Transaction.replace(R.id.frame, Fragment, "showFrag")
            Transaction.commit()
        }
        navView.setOnNavigationItemSelectedListener(this)
//        Handler().postDelayed(Runnable {
//            var i = Intent(this, LoginActivity::class.java);
//            startActivity(i)
//            finish()
//        }, 3000)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var Transaction = supportFragmentManager.beginTransaction()
        when(item.itemId){
            R.id.disease -> {
                var Fragment = FindDiseaseFragment()
                Transaction.replace(R.id.frame, Fragment, "diseaseFrag")
                Transaction.commit()
                return true
            }
            R.id.hosipital -> {
                var Fragment = HosipitalFragment()
                Transaction.replace(R.id.frame, Fragment, "hospitalFrag")
                Transaction.commit()
                return true
            }
            R.id.youtube -> {
                var Fragment = YoutubeFragment()
                Transaction.replace(R.id.frame, Fragment, "youtubeFrag")
                Transaction.commit()
                return true
            }
        }
        return false
    }
}
