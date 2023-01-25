package com.ugrcaan.projectsfinal.viewmodel

import android.provider.ContactsContract.Data
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.recyclerview.widget.RecyclerView
import com.ugrcaan.projectsfinal.R
import com.ugrcaan.projectsfinal.databinding.ComponentCardRowBinding
import com.ugrcaan.projectsfinal.model.Card
import com.ugrcaan.projectsfinal.model.DatabaseHelper

interface OnCardClickListener {
    fun onCardClicked(card: Card)
    fun onCardLongClicked(card: Card)
}

class CardAdapter(private val cardList: ArrayList<Card>, private val listener: OnCardClickListener): RecyclerView.Adapter<CardAdapter.CardHolder>() {
    private lateinit var databaseHelper : DatabaseHelper
    class CardHolder(val binding: ComponentCardRowBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val binding = ComponentCardRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        databaseHelper = DatabaseHelper(parent.context)
        return CardHolder(binding)
    }

    override fun onBindViewHolder(holder: CardHolder, position: Int) {

        holder.binding.cardName.text = cardList[position].name
        holder.binding.cardNumber.text = cardList[position].number
        if (cardList[position].active){
            holder.binding.active.visibility = View.VISIBLE
        }else {
            holder.binding.active.visibility = View.INVISIBLE
        }
        if (cardList[position].type == 0){
            holder.binding.cardType.setImageResource(R.drawable.icon_mastercard)
        } else {
            holder.binding.cardType.setImageResource(R.drawable.icon_visa)
        }

        holder.itemView.setOnClickListener {
            listener.onCardClicked(cardList[position])
        }

        holder.itemView.setOnLongClickListener {
            listener.onCardLongClicked(cardList[position])
            true
        }
    }

    override fun getItemCount(): Int {
        return cardList.size
    }
}