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
import com.example.foodsearch.presentation.search.SearchScreenState
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsRecipe : Fragment() {
    private lateinit var binding: FragmentDetailsRecipeBinding
    private var lastState = null

    private val viewModel: DetailsRecipeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeState()
viewModel.getDetailsRecipeInfo()


        val btStandartContainer = binding.includedLayout.bottomSheet
        val btStBeh = BottomSheetBehavior.from(btStandartContainer)
        btStBeh.state = BottomSheetBehavior.STATE_HIDDEN

        binding.addInBook.setOnClickListener {
            expandBottomSheet()
        }


        binding.icLike.setOnClickListener {

        }


    }

    private fun expandBottomSheet() {
        val btStandartContainer = binding.includedLayout.bottomSheet
        val btStBeh = BottomSheetBehavior.from(btStandartContainer)

        btStBeh.state = BottomSheetBehavior.STATE_HALF_EXPANDED

    }


    private fun observeState() = with(binding) {
        viewModel.getLiveData.observe(viewLifecycleOwner) { newState ->

            when (newState) {
                is DetailsSearchScreenState.Loading -> {
//                    pbs.makeVisible()
                }

                is DetailsSearchScreenState.ErrorNoEnternet -> {
                    if (newState.message == "Exception") {
//                        pbs.makeGone()
                    }
                }

                is DetailsSearchScreenState.ErrorNotFound -> {
                    if (newState.message == "retry") {
//                        pbs.makeGone()
                    } else if (newState.message == null) {
//                        pbs.makeGone()
                    }
                }


                is DetailsSearchScreenState.SearchResults -> {
//                    pbs.makeGone()
                    val recipeToDisplay = newState.data


                                Glide.with(imMineDetail.context)
                .load(recipeToDisplay?.image)
                .placeholder(R.drawable.ic_ph_kitchen)
                .error(R.drawable.ic_ph_kitchen)
                .into(imMineDetail)




//                    recipeToDisplay?.let { displayRecipes(it) }

                }


//

                null -> TODO()
            }
        }

    }


}