package com.ugrcaan.projectsfinal.viewmodel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ugrcaan.projectsfinal.databinding.ComponentDriveRowBinding
import com.ugrcaan.projectsfinal.model.DriveHistory

class DriveAdapter(private val driveList: ArrayList<DriveHistory>): RecyclerView.Adapter<DriveAdapter.DriveHolder>() {
    class DriveHolder(val binding: ComponentDriveRowBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriveHolder {
        val binding = ComponentDriveRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DriveHolder(binding)
    }

    override fun onBindViewHolder(holder: DriveHolder, position: Int) {
        holder.binding.scID.text = driveList[position].scId
        holder.binding.distance.text = driveList[position].distance
        holder.binding.amount.text = driveList[position].price
    }

    override fun getItemCount(): Int {
        return driveList.size
    }
}
