package com.example.as_7_vedang_shah

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

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
        val textViewAmount=view.findViewById<TextView>(R.id.expenseAmountDetail)
        val textviewDate = view.findViewById<TextView>(R.id.expenseDateDetail)


        val name = arguments?.getString("name")
        val amount = arguments?.getString("amount")
        val date = arguments?.getString("date")

        arguments?.clear()

        textViewName.text = "Expense Name: $name"
        textViewAmount.text = "Expense Amount: $amount"
        textviewDate.text = "Expense Date: $date"
    }
}
