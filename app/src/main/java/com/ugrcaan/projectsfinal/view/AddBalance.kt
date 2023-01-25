package com.ugrcaan.projectsfinal.view

import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ugrcaan.projectsfinal.R
import com.ugrcaan.projectsfinal.databinding.FragmentAddBalanceBinding
import com.ugrcaan.projectsfinal.model.Balance
import com.ugrcaan.projectsfinal.model.DatabaseHelper

class AddBalance : Fragment() {
    private lateinit var binding: FragmentAddBalanceBinding
    private lateinit var databaseHelper : DatabaseHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        databaseHelper = DatabaseHelper(view.context)

        val selectedCard = databaseHelper.getActiveCards().first()

        binding.cardName.text = selectedCard.name
        binding.cardNumber.text = selectedCard.number
        if (selectedCard.type == 0){
            binding.cardType.setImageResource(R.drawable.icon_mastercard)
        } else {
            binding.cardType.setImageResource(R.drawable.icon_visa)
        }

        binding.goBackLayout.setOnClickListener{
            (activity as MainActivity).returnToWalletFragment()
        }

        binding.balance10.setOnClickListener {
            binding.amountText.setText(binding.balance10.text)
        }

        binding.balance25.setOnClickListener {
            binding.amountText.setText(binding.balance25.text)
        }

        binding.balance50.setOnClickListener {
            binding.amountText.setText(binding.balance50.text)
        }

        binding.balance75.setOnClickListener {
            binding.amountText.setText(binding.balance75.text)
        }

        binding.addBalanceBtn.setOnClickListener {
            val sharedPreferences = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)
            val oldBalance = sharedPreferences.getFloat("key", 0f)
            val addedBalance = binding.amountText.text.toString().toFloat()
            val editor = sharedPreferences.edit()
            editor.putFloat("key", oldBalance + addedBalance)
            editor.apply()
            val time = getCurrentDateTime()
            val balance = Balance(addedBalance.toString(), usedCard = selectedCard.name, time)
            databaseHelper.insertBalance(balance)

            (activity as MainActivity).returnToWalletFragment()
        }

    }

    fun getCurrentDateTime(): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        return "$year-$month-$day $hour:$minute"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment and create a binding object.
        binding = FragmentAddBalanceBinding.inflate(inflater, container, false)
        return binding.root
    }
}