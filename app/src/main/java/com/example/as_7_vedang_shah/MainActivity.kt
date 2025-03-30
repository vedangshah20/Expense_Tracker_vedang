package com.example.as_7_vedang_shah

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var expenseRecyclerView: RecyclerView
    private lateinit var expenseAdapter: ExpenseAdapter
    private val expensedatalist = mutableListOf<Expense>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LIFECYCLE", "onCreate called")
        setContentView(R.layout.activity_main)

        val expenseNameInput = findViewById<EditText>(R.id.nameInput)
        val editTextExpenseAmount = findViewById<EditText>(R.id.amountInput)
        val buttontoaddexpense = findViewById<Button>(R.id.submitButton)

        expenseRecyclerView = findViewById(R.id.recyclerView)
        expenseRecyclerView.layoutManager = LinearLayoutManager(this)

        expenseAdapter = ExpenseAdapter(expensedatalist) { position ->
            expensedatalist.removeAt(position)
            expenseAdapter.notifyItemRemoved(position)
        }

        expenseRecyclerView.adapter = expenseAdapter

        buttontoaddexpense.setOnClickListener {
            val name = expenseNameInput.text.toString().trim()
            val amount = editTextExpenseAmount.text.toString().trim().toDoubleOrNull()

            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter expense name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (amount == null || amount <= 0) {
                Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            expensedatalist.add(Expense(name, amount))
            expenseAdapter.notifyItemInserted(expensedatalist.size - 1)

            expenseNameInput.text.clear()
            editTextExpenseAmount.text.clear()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("ActivityLifecycle", "onStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.d("ActivityLifecycle", "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.d("ActivityLifecycle", "onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.d("ActivityLifecycle", "onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ActivityLifecycle", "onDestroy called")
    }
}


data class Expense(val name: String, val amount: Double)
