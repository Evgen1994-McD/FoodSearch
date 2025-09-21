package com.example.foodsearch.presentation.details

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodsearch.R
import com.example.foodsearch.databinding.FragmentDetailsRecipeBinding
import com.example.foodsearch.domain.models.RecipeDetails
import com.example.foodsearch.presentation.details.adapters.IngredientAdapter
import com.example.foodsearch.presentation.details.adapters.StepAdapter
import com.example.foodsearch.presentation.search.SearchFragment
import com.example.foodsearch.presentation.search.SearchScreenState
import com.example.foodsearch.presentation.search.adapter.RecipeAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsRecipe : Fragment() {
    private lateinit var binding: FragmentDetailsRecipeBinding
    private var lastState = null
    private lateinit var arrow: ImageView
    private lateinit var arrowInstructions: ImageView
    private var isExpanded = false
    private var isExpandedInstructions = false
    private var ab: ActionBar? =
        null

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
        ab =
            (activity as AppCompatActivity).supportActionBar




        observeState()
viewModel.getDetailsRecipeInfo(

)

        arrow = binding.btArrowIngredients
        arrowInstructions=binding.btArrowInstructions
        expandRecyclerView(binding.rcViewIngredients, arrow)
        expandRecyclerView(binding.rcViewInstructions,arrowInstructions)

        arrow.setOnClickListener {
            if (isExpanded) {
                collapseRecyclerView(binding.rcViewIngredients, arrow)
                isExpanded = false
            } else {
                expandRecyclerView(binding.rcViewIngredients, arrow)
                isExpanded = true

            }
        }

        arrowInstructions.setOnClickListener {
            if (isExpandedInstructions) {
                collapseRecyclerView(binding.rcViewInstructions, arrowInstructions)
                isExpandedInstructions = false

            } else {
                expandRecyclerView(binding.rcViewInstructions,arrowInstructions)
                isExpandedInstructions = true

            }

        }





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
                }

                is DetailsSearchScreenState.ErrorNoEnternet -> {
                    if (newState.message == "Exception") {
                    }
                }

                is DetailsSearchScreenState.ErrorNotFound -> {
                    if (newState.message == "retry") {
                    } else if (newState.message == null) {
                    }
                }


                is DetailsSearchScreenState.SearchResults -> {
                    val recipeToDisplay = newState.data


                                Glide.with(imMineDetail.context)
                .load(recipeToDisplay?.image)
                .placeholder(R.drawable.ic_ph_kitchen)
                .error(R.drawable.ic_ph_kitchen)
                .into(imMineDetail)

                  val tags = getTags(recipeToDisplay)
                    binding.tags.text = tags.toString()
                    binding.tvName.text = recipeToDisplay?.title.toString()
                    binding.tvServ.text = recipeToDisplay?.servings.toString()+" "+ getString(R.string.servings)
                    binding.tvTime.text = recipeToDisplay?.readyInMinutes.toString()+" "+ getString(R.string.minutes)
                    binding.tvCost.text = recipeToDisplay?.pricePerServing.toString()+" "+ getString(R.string.cost)
                    binding.dishType.text = recipeToDisplay?.dishTypes.toString()



                    rcViewIngredients.layoutManager = LinearLayoutManager(requireContext())
                    rcViewIngredients.adapter = IngredientAdapter(recipeToDisplay?.extendedIngredients, requireContext())


val steps = recipeToDisplay?.analyzedInstructions?.flatMap { it.steps }
                    rcViewInstructions.layoutManager = LinearLayoutManager(requireContext())
                    rcViewInstructions.adapter =
                        StepAdapter(steps, requireContext())


                }




                null -> TODO()
            }
        }

    }



    private fun getTags(recipe: RecipeDetails?): List<String> {
        val tags = mutableListOf<String>()

        if (recipe?.vegetarian ?: false) tags.add("[vegetarian]")
        if (recipe?.vegan ?: false) tags.add("[vegan]")
        if (recipe?.glutenFree ?: false) tags.add("[glutenFree]")
        if (recipe?.dairyFree ?: false) tags.add("[dairyFree]")
        if (recipe?.veryHealthy ?: false) tags.add("[veryHealthy]")
        if (recipe?.cheap ?: false) tags.add("[cheap]")
        if (recipe?.veryPopular ?: false) tags.add("[veryPopular]")
        if (recipe?.sustainable ?: false) tags.add("[sustainable]")
        if (recipe?.lowFodmap ?: false) tags.add("[lowFodmap]")

        return tags
    }


    private fun expandRecyclerView(recyclerView: RecyclerView, imageView: ImageView) = with(binding) {
        recyclerView.visibility = View.VISIBLE
        ObjectAnimator.ofFloat(recyclerView, "translationY", 0f).apply {
            duration = 300
            start()
        }
        imageView.animate().rotation(180f).setDuration(300).start()
    }

    private fun collapseRecyclerView(recyclerView: RecyclerView, imageView: ImageView) = with(binding){
        ObjectAnimator.ofFloat(recyclerView, "translationY", recyclerView.height.toFloat()).apply {
            duration = 300
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    recyclerView.visibility = View.GONE
                }
            })
            start()
        }
        imageView.animate().rotation(0f).setDuration(300).start()
    }





}