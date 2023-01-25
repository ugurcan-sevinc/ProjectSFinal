package com.ugrcaan.projectsfinal.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.recyclerview.widget.RecyclerView
import com.ugrcaan.projectsfinal.R
import com.ugrcaan.projectsfinal.databinding.ComponentBalanceRowBinding
import com.ugrcaan.projectsfinal.model.Balance
import com.ugrcaan.projectsfinal.model.Card

class BalanceAdapter(private val balanceList: ArrayList<Balance>): RecyclerView.Adapter<BalanceAdapter.BalanceHolder>() {
    class BalanceHolder(val binding: ComponentBalanceRowBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BalanceHolder {
        val binding = ComponentBalanceRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BalanceHolder(binding)
    }

    override fun onBindViewHolder(holder: BalanceHolder, position: Int) {
        holder.binding.usedCard.text = balanceList[position].usedCard
        "$${balanceList[position].amount}".also { holder.binding.amount.text = it }
        holder.binding.time.text = balanceList[position].time
    }

    override fun getItemCount(): Int {
        return balanceList.size
    }
}