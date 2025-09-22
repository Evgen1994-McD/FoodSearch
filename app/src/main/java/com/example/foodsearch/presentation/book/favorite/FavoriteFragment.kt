package com.example.foodsearch.presentation.book.favorite

import android.content.Context
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodsearch.R
import com.example.foodsearch.databinding.FragmentFavoriteBinding
import com.example.foodsearch.domain.models.RecipeDetails
import com.example.foodsearch.domain.models.RecipeSummary
import com.example.foodsearch.presentation.book.adapter.FavoriteAdapter
import com.example.foodsearch.presentation.book.adapter.OnFavoriteRecipeClickListener
import com.example.foodsearch.presentation.search.adapter.OnRecipeClickListener
import com.example.foodsearch.presentation.search.adapter.RecipeAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment(), OnFavoriteRecipeClickListener {
    private lateinit var binding: FragmentFavoriteBinding
    private val viewModel: FavoriteViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getRecipes()
        observeFavoriteRecipes()


    }

    companion object {

        fun newInstance() = FavoriteFragment()

    }


    private fun View.makeGone() {
        this.visibility = View.GONE // функция для вью гон
    }

    private fun View.makeVisible() {
        this.visibility = View.VISIBLE // функция для вью визибл
    }

    private fun View.makeInvisible() {
        this.visibility = View.INVISIBLE // функция для вью инвизибл
    }


    private fun displayTRecipes(recipes: List<RecipeDetails>) = with(binding) {

        rcView1.layoutManager = LinearLayoutManager(requireContext())
        rcView1.adapter = FavoriteAdapter(recipes, requireContext(), this@FavoriteFragment)
        rcView1.makeVisible()
        phNtsh.makeInvisible()
        msgTxtBottom.makeInvisible()
    }

    private fun displayPlaceholders() = with(binding) {


        rcView1.makeInvisible()
        phNtsh.makeVisible()
        msgTxtBottom.makeVisible()
    }


    fun getRecipeClickAndStart(recipeSummary: RecipeDetails) {
        val bundle = Bundle().apply {
            putSerializable("id", recipeSummary.id)

        }
        findNavController().navigate(R.id.detailsRecipe, bundle)

    }


    private fun observeFavoriteRecipes() {
        viewModel.getLiveData.observe(viewLifecycleOwner) { recipes ->
            if (!recipes.isNullOrEmpty()) {
                displayTRecipes(recipes)

            } else displayPlaceholders()

        }

    }

    override fun onRecipeClicker(recipeDetails: RecipeDetails) {
        getRecipeClickAndStart(recipeDetails)
    }

}