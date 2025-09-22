package com.example.foodsearch.presentation.book

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.foodsearch.presentation.book.favorite.FavoriteFragment
import com.example.foodsearch.presentation.book.saved.SavedRecipeFragment
import com.example.foodsearch.presentation.search.SearchFragment


class VpAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {


    override fun getItemCount(): Int = 2 // Количество страниц

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> FavoriteFragment.newInstance()
            1 -> SavedRecipeFragment.newInstance()
            else -> throw IllegalArgumentException("Invalid position!")
        }
}