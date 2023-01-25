package com.ugrcaan.projectsfinal

import android.app.Dialog
import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ugrcaan.projectsfinal.databinding.FragmentWalletBinding
import com.ugrcaan.projectsfinal.model.Card
import com.ugrcaan.projectsfinal.model.DatabaseHelper
import com.ugrcaan.projectsfinal.view.MainActivity
import com.ugrcaan.projectsfinal.viewmodel.CardAdapter
import com.ugrcaan.projectsfinal.viewmodel.OnCardClickListener

class Wallet : Fragment(), OnCardClickListener {
    private lateinit var binding: FragmentWalletBinding
    private var cardList = ArrayList<Card>()
    private lateinit var databaseHelper : DatabaseHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        databaseHelper = DatabaseHelper(view.context)
        val sharedPreferences = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val value = sharedPreferences.getFloat("key", 0f)

        cardList = databaseHelper.getAllCards()
        cardList.sortByDescending { it.active }

        binding.autoAddBalanceIndicator.paintFlags = binding.autoAddBalanceIndicator.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.usersBalanceText.text = value.toString()
        binding.cardsView.layoutManager = LinearLayoutManager(context)
        binding.cardsView.adapter = CardAdapter(cardList,this)
        binding.autoAddBalanceArrow.setOnClickListener{enOrDisAutoAdd()}
        binding.autoAddBalanceIndicator.setOnClickListener{
            val dialog = Dialog(view.context)
            dialog.setContentView(R.layout.component_customalert)
            val button = dialog.findViewById<Button>(R.id.dismissButton)
            button.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
        binding.balanceHistoryLayout.setOnClickListener{
            (activity as MainActivity).showBalanceHistoryFragment()
        }

        binding.addBalanceBtn.setOnClickListener{
            (activity as MainActivity).showAddBalanceFragment()
        }
        binding.addCardBtn.setOnClickListener {
            (activity as MainActivity).showAddCardFragment()
        }



    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment and create a binding object.
        binding = FragmentWalletBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCardClicked(card: Card) {
        // Set all cards to inactive
        cardList.forEach { it.active = false }
        // Set the selected card to active
        card.active = true
        // Update the cards in the database
        databaseHelper.activateCard(card)
        // Notify the adapter to update the views
        cardList.sortByDescending { it.active }
        binding.cardsView.adapter?.notifyDataSetChanged()
    }

    override fun onCardLongClicked(card: Card) {
        val position = cardList.indexOf(card)
        databaseHelper.deleteCard(cardList[position])
        cardList.removeAt(position)
        binding.cardsView.adapter?.notifyItemRemoved(position)
    }

    fun enOrDisAutoAdd(){
        if (binding.autoAddBalanceIndicator.text == "Disabled")
            binding.autoAddBalanceIndicator.text = getString(R.string.autoAddEn)
        else
            binding.autoAddBalanceIndicator.text = getString(R.string.autoAddDis)
    }

}