package com.example.foodsearch.presentation.details.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodsearch.R
import com.example.foodsearch.domain.models.OtherModels

class StepAdapter(
    private var steps: List<OtherModels.Step>?,
    private val context : Context
) : RecyclerView.Adapter<StepViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.instruction_step_item, parent, false)
        return StepViewHolder(view)
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {

        holder.bind(steps!![position])
    }

    override fun getItemCount(): Int {
        return steps!!.size
    }

}