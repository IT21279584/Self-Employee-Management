package com.example.selfemployeesmanagement

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard )
        supportActionBar?.hide()

        val searchCardView : CardView = findViewById(R.id.cvSearch)
        val exitCardView : CardView = findViewById(R.id.cvExit)
        val profileCardView : CardView = findViewById(R.id.cvProfile)


        searchCardView.setOnClickListener {
            val intent = Intent(this, EmployeeListActivity::class.java)
            startActivity(intent)
        }

        exitCardView.setOnClickListener {
            finishAffinity()
        }

        profileCardView.setOnClickListener {
            val intent = Intent(this, EmployeeSelfProfile::class.java)
            startActivity(intent)
        }
}
}