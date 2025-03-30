package com.example.as_7_vedang_shah

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class FooterFragment : Fragment() {

    private lateinit var totalTextView: TextView
    private var totalAmount: Double = 0.0

    fun updateTotal(newTotal: Double) {
        totalAmount = newTotal
        if (this::totalTextView.isInitialized) {
            totalTextView.text = "Total: $$totalAmount"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_footer, container, false)
        totalTextView = view.findViewById(R.id.totalAmountTextView)
        totalTextView.text = "Total: $$totalAmount"
        return view
    }
}
