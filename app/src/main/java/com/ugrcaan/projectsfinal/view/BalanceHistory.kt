package com.ugrcaan.projectsfinal

import android.app.Dialog
import android.graphics.Paint
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ugrcaan.projectsfinal.databinding.FragmentBalanceHistoryBinding
import com.ugrcaan.projectsfinal.databinding.FragmentWalletBinding
import com.ugrcaan.projectsfinal.model.Balance
import com.ugrcaan.projectsfinal.model.Card
import com.ugrcaan.projectsfinal.model.DatabaseHelper
import com.ugrcaan.projectsfinal.view.MainActivity
import com.ugrcaan.projectsfinal.viewmodel.BalanceAdapter
import com.ugrcaan.projectsfinal.viewmodel.CardAdapter
import com.ugrcaan.projectsfinal.viewmodel.OnCardClickListener

class BalanceHistory : Fragment() {
    private lateinit var binding: FragmentBalanceHistoryBinding
    private var balanceList = ArrayList<Balance>()
    private lateinit var databaseHelper : DatabaseHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        databaseHelper = DatabaseHelper(view.context)
        balanceList = databaseHelper.getBalances()
        binding.balanceRecycler.layoutManager = LinearLayoutManager(context)
        binding.balanceRecycler.adapter = BalanceAdapter(balanceList)

        binding.goBackLayout.setOnClickListener{
            (activity as MainActivity).returnToWalletFragment()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment and create a binding object.
        binding = FragmentBalanceHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }


}