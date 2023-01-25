package com.ugrcaan.projectsfinal.view

import android.os.Bundle
import android.provider.ContactsContract.Data
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.ugrcaan.projectsfinal.R
import com.ugrcaan.projectsfinal.databinding.FragmentAddCardBinding
import com.ugrcaan.projectsfinal.model.Card
import com.ugrcaan.projectsfinal.model.DatabaseHelper


class AddCard : Fragment() {
    private lateinit var binding: FragmentAddCardBinding
    private lateinit var databaseHelper : DatabaseHelper


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        databaseHelper = DatabaseHelper(view.context)
        var cardType = 0

        binding.cardName.addTextChangedListener{
            binding.clCardName.text = it.toString()
        }
        binding.cardNumber.addTextChangedListener{
            binding.clCardNumber.hint = it.toString()
            if (it.toString().startsWith("4")){
                binding.clCardType.setImageResource(R.drawable.icon_visa)
                cardType = 1
            }
            else if (it.toString().startsWith("5")){
                binding.clCardType.setImageResource(R.drawable.icon_mastercard)
                cardType = 0
            }
            else {
                binding.clCardNumber.hint = "Your card has to start with 4 or 5 !"
            }
        }
        binding.goBackLayout.setOnClickListener{
            (activity as MainActivity).returnToWalletFragment()
        }

        binding.addCardBtn.setOnClickListener {
            val cardName = binding.cardName.text.toString()
            val cardNumber = binding.cardNumber.text.toString()
            databaseHelper.insertCard(Card(cardName, cardNumber, false, cardType))
            (activity as MainActivity).returnToWalletFragment()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment and create a binding object.
        binding = FragmentAddCardBinding.inflate(inflater, container, false)
        return binding.root
    }
}