package com.example.as_7_vedang_shah


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var expenseRecyclerView: RecyclerView
    private lateinit var expenseAdapter: ExpenseAdapter

    //mutable list to add expense name and amount
    private val expensedatalist = mutableListOf<Expense>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

            //check if the input filed is empty for both name and expense
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter expense name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (amount == null || amount <= 0) {
                Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //add new expense to the list
            expensedatalist.add(Expense(name, amount))
            expenseAdapter.notifyItemInserted(expensedatalist.size - 1)

            expenseNameInput.text.clear()
            editTextExpenseAmount.text.clear()
        }
    }
}

data class Expense(val name: String, val amount: Double)
