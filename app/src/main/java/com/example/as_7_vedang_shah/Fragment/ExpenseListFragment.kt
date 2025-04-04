package com.example.as_7_vedang_shah.Fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.as_7_vedang_shah.adapter.Expense
import com.example.as_7_vedang_shah.adapter.ExpenseAdapter

import com.example.as_7_vedang_shah.R
import com.example.as_7_vedang_shah.network.RetrofitInstance
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.Calendar
private const val FILE_NAME = "expenses.txt"

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

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
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


        val currencyConvSwitch = view.findViewById<MaterialSwitch>(R.id.currencyConvSwitch)
        val spinnerCurrency = view.findViewById<Spinner>(R.id.spinnerCurrency)
        val CurrencyConvText = view.findViewById<TextView>(R.id.CurrencyConvText)


        loadSupportedCurrencies(spinnerCurrency)

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

        datePickerButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog =
                DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                    selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    selectedDateText.text = selectedDate
                }, year, month, day)

            datePickerDialog.show()
        }

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

            expensedatalist.add(Expense(name, amount, selectedDate!!, "CAD", amount))

            expenseAdapter.notifyItemInserted(expensedatalist.size - 1)
            updateFooterTotal()
            saveExpensesToFile()

            expenseNameInput.text.clear()
            editTextExpenseAmount.text.clear()
            selectedDateText.text = "No Date Selected"
            selectedDate = null
        }

        tipsButton.setOnClickListener {
            val url = "https://www.canada.ca/en/services/finance.html"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }


        currencyConvSwitch.setOnCheckedChangeListener { _, enabled ->
            if (enabled) {
                val chosenCurrency = spinnerCurrency.selectedItem?.toString() ?: "cad"
                currencyConv(chosenCurrency, CurrencyConvText)
            } else {
                for (index in expensedatalist.indices) {
                    val original = expensedatalist[index]
                    expensedatalist[index] = Expense(
                        original.name,
                        original.amount,
                        original.date,
                        "CAD",
                        original.amount
                    )
                }
                expenseAdapter.notifyDataSetChanged()
                CurrencyConvText.text = "Converted Cost: ${expensedatalist.sumOf { it.amount }} CAD"
            }
        }


        spinnerCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, pos: Int, id: Long
            ) {
                if (currencyConvSwitch.isChecked) {
                    val activeCurrency = spinnerCurrency.selectedItem.toString()
                    newcurrencySelected(activeCurrency, CurrencyConvText)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }



    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun currencyConv(userGivenCurrency: String, textView: TextView) {
        lifecycleScope.launch {
            try {
                val currencyResponse = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.getExchangeRates()
                }

                val selectedCurrencyConvRate = currencyResponse.cad[userGivenCurrency.lowercase()] ?: 1.0

                for (index in expensedatalist.indices) {
                    val item = expensedatalist[index]
                    expensedatalist[index] = Expense(
                        item.name,
                        item.amount,
                        item.date,
                        userGivenCurrency.uppercase(),
                        item.amount * selectedCurrencyConvRate
                    )
                }

                expenseAdapter.notifyDataSetChanged()
                textView.text =
                    "Converted Cost: ${expensedatalist.sumOf { it.convertedCost }} ${userGivenCurrency.uppercase()}"

            } catch (e: Exception) {
                Snackbar.make(
                    requireView(),
                    "Failed to convert: ${e.message}",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun loadSupportedCurrencies(spinner: Spinner) {
        lifecycleScope.launch {
            try {
                val fetchedCurrencyList = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.getCurrencies()
                }

                if (fetchedCurrencyList.isNotEmpty()) {
                    val availableCodes = fetchedCurrencyList.keys.sorted()
                    val currencyAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        availableCodes
                    )
                    currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = currencyAdapter

                    val defaultIndex = availableCodes.indexOf("cad")
                    if (defaultIndex != -1) {
                        spinner.setSelection(defaultIndex)
                    }
                }
            } catch (e: Exception) {
                Snackbar.make(requireView(), "Could not load currencies", Snackbar.LENGTH_SHORT).show()
            }
        }
    }


    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun newcurrencySelected(availableCur: String, textView: TextView) {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.getExchangeRates()
                }
                val rate = response.cad[availableCur.lowercase()] ?: 1.0
                expensedatalist.forEachIndexed { idx, item ->
                    expensedatalist[idx] = Expense(
                        item.name,
                        item.amount,
                        item.date,
                        availableCur.uppercase(),
                        item.amount * rate
                    )
                }
                expenseAdapter.notifyDataSetChanged()
                textView.text =
                    "Converted Cost: ${expensedatalist.sumOf { it.convertedCost }} ${availableCur.uppercase()}"
            } catch (e: Exception) {
                Snackbar.make(
                    requireView(),
                    "Rate fetch failed: ${e.message}",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun updateFooterTotal() {
        val total = expensedatalist.sumOf { it.amount }
        footerFragment.updateTotal(total)
    }

    private fun saveExpensesToFile() {
        try {
            val json = Gson().toJson(expensedatalist)
            requireContext().openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use {
                it.write(json.toByteArray())
            }
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
        } catch (e: FileNotFoundException) {
            Log.e("FileStorage", "File not found: ${e.message}")
        } catch (e: IOException) {
            Log.e("FileStorage", "Error reading file: ${e.message}")
        }
        return savedList
    }
}