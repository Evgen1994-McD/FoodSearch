package com.example.foodsearch.presentation.details

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.foodsearch.R
import com.example.foodsearch.databinding.FragmentDetailsRecipeBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsRecipe : Fragment() {
private var tempUrl = "https://img.spoonacular.com/recipe-cards/recipe-card-1758354479442.png"
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

        observeState()

        val btStandartContainer = binding.includedLayout.bottomSheet
        val btStBeh = BottomSheetBehavior.from(btStandartContainer)
        btStBeh.state = BottomSheetBehavior.STATE_HIDDEN

binding.addInBook.setOnClickListener {
    expandBottomSheet()
}


        binding.icLike.setOnClickListener {
            viewModel.getRecipeCard()
        }

        Glide.with(binding.imMineDetail.context)
            .load(tempUrl)
            .centerInside()

            .into(binding.imMineDetail)


    }

    private fun expandBottomSheet(){
        val btStandartContainer = binding.includedLayout.bottomSheet
        val btStBeh = BottomSheetBehavior.from(btStandartContainer)
//        val displayMetrics = resources.displayMetrics
//        val screenHeightInDp = displayMetrics.heightPixels / displayMetrics.densityDpi * 320f
//        val peekHeightPercentage = (screenHeightInDp * 0.2f).toInt() // 10%
//        btStBeh.peekHeight = peekHeightPercentage
btStBeh.state = BottomSheetBehavior.STATE_HALF_EXPANDED

    }


    private fun observeState()= with(binding){
        viewModel.getLiveData.observe(viewLifecycleOwner){ uri ->
            Glide.with(imMineDetail.context)
                .load(uri)
//                .transform(RoundedCorners(radiusInPX.toInt()))
//                .apply(options)
//                .placeholder(R.drawable.ic_placeholder_45)
//                .error(R.drawable.ic_placeholder_45)
                .into(imMineDetail)

        }
    }

}