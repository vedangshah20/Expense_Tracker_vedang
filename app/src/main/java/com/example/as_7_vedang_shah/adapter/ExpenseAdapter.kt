package com.example.as_7_vedang_shah.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.as_7_vedang_shah.R

data class Expense(
    val name: String,
    val amount: Double,
    val date: String,
    val currency: String,
    val convertedCost: Double
)

class ExpenseAdapter(
    private val expenseList: MutableList<Expense>,
    private val onDeleteButton: (Int) -> Unit,
    private val navController: NavController
) : RecyclerView.Adapter<ExpenseAdapter.ExpenseListHolder>() {

    class ExpenseListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val expenseName: TextView = itemView.findViewById(R.id.expenseNameText)
        val expenseAmount: TextView = itemView.findViewById(R.id.expenseAmountText)
        val expenseDate: TextView = itemView.findViewById(R.id.expenseDateText)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
        val showDetailsButton: Button = itemView.findViewById(R.id.showDetailsButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseListHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ExpenseListHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ExpenseListHolder, position: Int) {
        val expense = expenseList[position]
        holder.expenseName.text = expense.name
        holder.expenseAmount.text = "Original: ${expense.amount} CAD\n" +
                "Converted: %.2f %s".format(expense.convertedCost, expense.currency)
        holder.expenseDate.text = expense.date

        holder.showDetailsButton.setOnClickListener {
            val bundle = Bundle().apply {
                putString("name", expense.name)
                putDouble("amount", expense.amount)
                putString("expense_currency", expense.currency)
                putString("date", expense.date)
                putDouble("expense_converted_cost", expense.convertedCost)
            }
            navController.navigate(R.id.action_expenseListFragment_to_expenseDetailsFragment, bundle)
        }

        // Delete button
        holder.deleteButton.setOnClickListener {
            onDeleteButton(position)
        }
    }

    override fun getItemCount(): Int = expenseList.size
}
