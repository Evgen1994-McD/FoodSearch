package com.example.foodsearch.presentation.details.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.foodsearch.R
import com.example.foodsearch.domain.models.OtherModels

class StepViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    private val number: TextView = itemView.findViewById(R.id.step_number)
    private val string: TextView = itemView.findViewById(R.id.step_string)


    @SuppressLint("CheckResult")
    fun bind(step: OtherModels.Step) {


        number.text = step.number.toString()
        string.text = step.step


    }

}