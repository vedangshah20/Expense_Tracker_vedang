package com.example.as_7_vedang_shah

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExpenseAdapter(
    private val expenseList: MutableList<Expense>, //list of expenses
    private val onDeleteButton: (Int) -> Unit,
    private val onItemClick: (Expense) -> Unit
) : RecyclerView.Adapter<ExpenseAdapter.expenselistholder>() {

    class expenselistholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ExpenseName: TextView = itemView.findViewById(R.id.expenseNameText)
        val expenseAmount: TextView = itemView.findViewById(R.id.expenseAmountText)
        val expenseDate: TextView = itemView.findViewById(R.id.expenseDateText)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): expenselistholder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return expenselistholder(view)
    }

    override fun onBindViewHolder(holder: expenselistholder, position: Int) {
        val expense = expenseList[position]
        holder.ExpenseName.text = expense.name
        holder.expenseAmount.text = "$${expense.amount}"
        holder.expenseDate.text = expense.date
        holder.deleteButton.setOnClickListener {
            onDeleteButton(position)
        }
        holder.itemView.setOnClickListener {
            onItemClick(expense)
        }
    }

    override fun getItemCount(): Int = expenseList.size
}
