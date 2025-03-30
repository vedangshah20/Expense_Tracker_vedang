package com.example.as_7_vedang_shah

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import androidx.fragment.app.commit


class MainActivity : AppCompatActivity() {

    //recyclerView display the expense list
    private lateinit var expenseRecyclerView: RecyclerView
    private lateinit var expenseAdapter: ExpenseAdapter
    private val expensedatalist = mutableListOf<Expense>()

    private lateinit var selectedDateText: TextView
    private var selectedDate: String? = null
    private var isFooterVisible = false
    private lateinit var footerFragment: FooterFragment


    //started when activity is first created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LIFECYCLE", "onCreate called")
        setContentView(R.layout.activity_main)

        //i was having error in importing commit then i had to add this line in gradle file
        //implementation "androidx.fragment:fragment-ktx:1.6.2"
        // Add HeaderFragment dynamically
        supportFragmentManager.commit {
            replace(R.id.headerContainer, HeaderFragment())
        }

        //added  footerfragment dynamically
        footerFragment = FooterFragment()
        supportFragmentManager.commit {
            replace(R.id.footerContainer, footerFragment)
        }
        isFooterVisible = true

        val expenseNameInput = findViewById<EditText>(R.id.nameInput)
        val editTextExpenseAmount = findViewById<EditText>(R.id.amountInput)
        val buttontoaddexpense = findViewById<Button>(R.id.submitButton)
        val datePickerButton = findViewById<Button>(R.id.pickDateButton)
        selectedDateText = findViewById(R.id.selectedDateText)
        val tipsButton = findViewById<Button>(R.id.tipsButton)

        //recyclerView
        expenseRecyclerView = findViewById(R.id.recyclerView)
        expenseRecyclerView.layoutManager = LinearLayoutManager(this)

        expenseAdapter = ExpenseAdapter(
            expensedatalist,
            onDeleteButton = { position ->
                expensedatalist.removeAt(position)
                expenseAdapter.notifyItemRemoved(position)
                updateFooterTotal()
            },

            //explicit intent to show expense details
            //for this took help from this link: https://developer.android.com/guide/components/intents-filters
            onItemClick = { expense ->
                val intent = Intent(this, ExpenseDetailsActivity::class.java)
                intent.putExtra("name", expense.name)
                intent.putExtra("amount", expense.amount.toString())
                intent.putExtra("date", expense.date)
                startActivity(intent)
            }
        )

        expenseRecyclerView.adapter = expenseAdapter

        //date picker
        datePickerButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate = "${selectedDay}/${selectedMonth + 1}/${selectedYear}"
                selectedDateText.text = selectedDate
            }, year, month, day)

            datePickerDialog.show()
        }

        //add a new expense
        buttontoaddexpense.setOnClickListener {
            val name = expenseNameInput.text.toString().trim()
            val amount = editTextExpenseAmount.text.toString().trim().toDoubleOrNull()

            //if name, amount or date is empty show a message
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter name of the expense", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (amount == null || amount <= 0) {
                Toast.makeText(this, "Please enter amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedDate.isNullOrEmpty()) {
                Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //add expense to the list
            expensedatalist.add(Expense(name, amount, selectedDate!!))
            expenseAdapter.notifyItemInserted(expensedatalist.size - 1)
            updateFooterTotal()

            //clear input fields
            expenseNameInput.text.clear()
            editTextExpenseAmount.text.clear()
            selectedDateText.text = "No Date Selected"
            selectedDate = null
        }

        //implicit intent to show tips
        tipsButton.setOnClickListener {
            val url = "https://www.canada.ca/en/services/finance.html"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }

    private fun updateFooterTotal() {
        val total = expensedatalist.sumOf { it.amount }
        footerFragment.updateTotal(total)
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


class Expense(val name: String, val amount: Double, val date: String)
