package com.ugrcaan.projectsfinal.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ugrcaan.projectsfinal.databinding.FragmentHistoryBinding
import com.ugrcaan.projectsfinal.model.DatabaseHelper
import com.ugrcaan.projectsfinal.model.DriveHistory
import com.ugrcaan.projectsfinal.viewmodel.DriveAdapter


class History : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private var driveHistoryList = ArrayList<DriveHistory>()
    private lateinit var databaseHelper : DatabaseHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        databaseHelper = DatabaseHelper(view.context)
        driveHistoryList = databaseHelper.getAllDriveHistory()

        Log.d("Drive", "HI I'm HERE")
        driveHistoryList.forEach {
            Log.d("ID", it.scId)
            Log.d("Distance", it.distance)
            Log.d("Price", it.price)
        }

        binding.driveRecycler.layoutManager = LinearLayoutManager(context)
        binding.driveRecycler.adapter = DriveAdapter(driveHistoryList)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }
}