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

        val textViewName = view.findViewById<TextView>(R.id.expenseNameDetail)
        val textViewAmount = view.findViewById<TextView>(R.id.expenseAmountDetail)
        val textViewDate = view.findViewById<TextView>(R.id.expenseDateDetail)
        val textViewCurrency = view.findViewById<TextView>(R.id.expenseCurrencyDetail)
        val textViewConvertedCost = view.findViewById<TextView>(R.id.expenseConvertedDetail)

        val name = arguments?.getString("name")
        val amount = arguments?.getDouble("amount")
        val date = arguments?.getString("date")
        val currency = arguments?.getString("expense_currency")
        val convertedCost = arguments?.getDouble("expense_converted_cost") ?: amount


        arguments?.clear()

        textViewName.text = "Expense Name: $name"
        textViewAmount.text = "Expense Original Amount: $amount CAD"
        textViewDate.text = "Expense Date: $date"
        textViewCurrency.text = "Currency: $currency"

        if (convertedCost != amount && currency != "CAD") {
            textViewConvertedCost.text = "Converted Cost: ${FormateForConvCurrecy(convertedCost, currency)}"
        } else {
            textViewConvertedCost.text = "Converted Cost: $convertedCost $currency"
        }
    }

    private fun FormateForConvCurrecy(amount: Double?, currency: String?): String {
        return when (currency?.uppercase()) {
            "CAD" -> "${amount} CAD"
            "ISK" -> "$amount kr"
            "INR" -> "$amount INR"
            "JPY" -> "$amount JPY"
            "RUB" -> "$amount RUB"
            "USD" -> "$amount USD"
            else -> "$amount $currency"
        }
    }
}
