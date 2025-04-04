package com.example.as_7_vedang_shah.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.as_7_vedang_shah.R

class ExpenseDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.expense_details, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val expenseNameTextView = view.findViewById<TextView>(R.id.expenseNameDetail)
        val expenseOriginalAmountTextView = view.findViewById<TextView>(R.id.expenseAmountDetail)
        val expenseDateTextView = view.findViewById<TextView>(R.id.expenseDateDetail)
        val expenseCurrencyTextView = view.findViewById<TextView>(R.id.expenseCurrencyDetail)
        val expenseConvertedCostTextView = view.findViewById<TextView>(R.id.expenseConvertedDetail)


        val expenseName = arguments?.getString("name")
        val expenseOriginalAmount = arguments?.getDouble("amount")
        val expenseDate = arguments?.getString("date")
        val expenseCurrency = arguments?.getString("expense_currency")
        val expenseConvertedCost = arguments?.getDouble("expense_converted_cost") ?: expenseOriginalAmount

        arguments?.clear()

        expenseNameTextView.text = "Expense Name: $expenseName"
        expenseOriginalAmountTextView.text = "Expense Original Amount: $expenseOriginalAmount CAD"
        expenseDateTextView.text = "Expense Date: $expenseDate"
        expenseCurrencyTextView.text = "Currency: $expenseCurrency"

        if (expenseConvertedCost != expenseOriginalAmount && expenseCurrency != "CAD") {
            expenseConvertedCostTextView.text = "Converted Cost: ${ FormateForConvAmount(expenseConvertedCost, expenseCurrency)}"
        } else {
            expenseConvertedCostTextView.text = "Converted Cost: $expenseConvertedCost $expenseCurrency"
        }
    }

    private fun  FormateForConvAmount(amount: Double?, currency: String?): String {
        return when (currency?.uppercase()) {
            "CAD" -> "$amount CAD"
            "INR" -> "$amount INR"
            "JPY" -> "$amount JPY"
            "USD" -> "$amount USD"
            else -> "$amount $currency"
        }
    }
}
