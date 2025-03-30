package com.example.as_7_vedang_shah

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ExpenseDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.expense_details)

        val nameTextView = findViewById<TextView>(R.id.expenseNameDetail)
        val amountTextView = findViewById<TextView>(R.id.expenseAmountDetail)
        val dateTextView = findViewById<TextView>(R.id.expenseDateDetail)

        val name = intent.getStringExtra("name") // fixed key names
        val amount = intent.getStringExtra("amount")
        val date = intent.getStringExtra("date")

        nameTextView.text = "Name: $name"
        amountTextView.text = "Amount: $amount"
        dateTextView.text = "Date: $date"
    }
}
