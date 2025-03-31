package com.example.as_7_vedang_shah

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*

private const val FILE_NAME = "expenses.json"

class ExpenseListFragment : Fragment() {

    private lateinit var expenseRecyclerView: RecyclerView
    private lateinit var expenseAdapter: ExpenseAdapter
    private val expensedatalist = mutableListOf<Expense>()

    private lateinit var selectedDateText: TextView
    private var selectedDate: String? = null
    private lateinit var footerFragment: FooterFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.expense_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().supportFragmentManager.commit {
            replace(R.id.headerContainer, HeaderFragment())
        }


        footerFragment = FooterFragment()
        requireActivity().supportFragmentManager.commit {
            replace(R.id.footerContainer, footerFragment)
        }

        val navController = findNavController()


        val expenseNameInput = view.findViewById<EditText>(R.id.nameInput)
        val editTextExpenseAmount = view.findViewById<EditText>(R.id.amountInput)
        val buttontoaddexpense = view.findViewById<Button>(R.id.submitButton)
        val datePickerButton = view.findViewById<Button>(R.id.pickDateButton)
        selectedDateText = view.findViewById(R.id.selectedDateText)
        val tipsButton = view.findViewById<Button>(R.id.tipsButton)

        //recyclerView
        expenseRecyclerView = view.findViewById(R.id.recyclerView)
        expenseRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        expensedatalist.clear()
        expensedatalist.addAll(loadExpensesFromFile())


        expenseAdapter = ExpenseAdapter(
            expenseList = expensedatalist,
            onDeleteButton = { position ->
                expensedatalist.removeAt(position)
                expenseAdapter.notifyItemRemoved(position)
                updateFooterTotal()
                saveExpensesToFile()
            },
            navController = navController
        )

        expenseRecyclerView.adapter = expenseAdapter
        updateFooterTotal()

        //date Picker
        datePickerButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                selectedDateText.text = selectedDate
            }, year, month, day)

            datePickerDialog.show()
        }

        //add new expense
        buttontoaddexpense.setOnClickListener {
            val name = expenseNameInput.text.toString().trim()
            val amount = editTextExpenseAmount.text.toString().trim().toDoubleOrNull()

            if (name.isEmpty()) {
                Toast.makeText(context, "Please enter name of the expense", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (amount == null || amount <= 0) {
                Toast.makeText(context, "Please enter amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedDate.isNullOrEmpty()) {
                Toast.makeText(context, "Please select a date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            expensedatalist.add(Expense(name, amount, selectedDate!!))
            expenseAdapter.notifyItemInserted(expensedatalist.size - 1)
            updateFooterTotal()
            saveExpensesToFile()

            //clear inputs
            expenseNameInput.text.clear()
            editTextExpenseAmount.text.clear()
            selectedDateText.text = "No Date Selected"
            selectedDate = null
        }

        //tip link
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

    //save expense data to the json file
    private fun saveExpensesToFile() {
        try {
            val json = Gson().toJson(expensedatalist)
            requireContext().openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use {
                it.write(json.toByteArray())
            }
            Log.d("FileStorage", "Expenses saved successfully")
        } catch (e: IOException) {
            Log.e("FileStorage", "Error saving expenses: ${e.message}")
        }
    }


    private fun loadExpensesFromFile(): MutableList<Expense> {
        val savedList = mutableListOf<Expense>()
        try {
            val file = File(requireContext().filesDir, FILE_NAME)
            if (!file.exists()) return savedList

            val json = file.readText()
            val type = object : TypeToken<List<Expense>>() {}.type
            val loadedExpenses: List<Expense> = Gson().fromJson(json, type)
            savedList.addAll(loadedExpenses)

            Log.d("FileStorage", "Expenses loaded successfully")
        } catch (e: FileNotFoundException) {
            Log.e("FileStorage", "File not found: ${e.message}")
        } catch (e: IOException) {
            Log.e("FileStorage", "Error reading file: ${e.message}")
        }
        return savedList
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
