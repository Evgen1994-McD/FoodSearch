package com.example.foodsearch.presentation.details

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodsearch.R
import com.example.foodsearch.databinding.FragmentDetailsRecipeBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class DetailsRecipe : Fragment() {

private lateinit var binding: FragmentDetailsRecipeBinding
    private val viewModel: DetailsRecipeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsRecipeBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val displayMetrics = resources.displayMetrics
        val screenHeightInDp = displayMetrics.heightPixels / displayMetrics.densityDpi * 320f
        val peekHeightPercentage = (screenHeightInDp * 0.2f).toInt() // 10%
        val btStandartContainer = binding.includedLayout.bottomSheet
        val btStBeh = BottomSheetBehavior.from(btStandartContainer)
        btStBeh.peekHeight = peekHeightPercentage




    }
}