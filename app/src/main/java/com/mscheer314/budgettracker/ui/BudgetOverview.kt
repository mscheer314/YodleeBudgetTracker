package com.mscheer314.budgettracker.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.android.databinding.ActivityBudgetOverviewBinding

class BudgetOverview : AppCompatActivity() {
    private lateinit var binding: ActivityBudgetOverviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBudgetOverviewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val eventsInfo = intent.getSerializableExtra("eventsInfo")

        Log.i(localClassName, "EventsInfo: " + eventsInfo.toString())
    }
}