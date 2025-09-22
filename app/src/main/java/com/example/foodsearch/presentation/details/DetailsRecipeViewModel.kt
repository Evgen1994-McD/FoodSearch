package com.example.foodsearch.presentation.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodsearch.domain.models.RecipeDetails
import com.example.foodsearch.domain.search.SearchInteractor
import com.example.foodsearch.presentation.search.SearchScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class DetailsRecipeViewModel @Inject constructor(
    private val searchInteractor: SearchInteractor,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    //private val id = 631852
    val id: Int = savedStateHandle.get<Int>("id") ?: -1
    private var currentRecipe: RecipeDetails? = null

    private val mutableScreenState = MutableLiveData<DetailsSearchScreenState?>()
    val getLiveData: LiveData<DetailsSearchScreenState?> get() = mutableScreenState


    private val mutableScreenStateIsLike = MutableLiveData<Boolean>()
    val getLiveDataIsLike: LiveData<Boolean> get() = mutableScreenStateIsLike


    suspend fun tryGetRecipeFromDataBase(id: Int) {
        searchInteractor.searchRecipeDetailsInfo(id)
        if (searchInteractor.getRecipeDetailsById(id)?.isLike == true) {
            mutableScreenStateIsLike.postValue(true)
        } else {
            mutableScreenStateIsLike.postValue(false)
        }
    }


    suspend fun replaceRecipe(recipeDetails: RecipeDetails) {
        searchInteractor.insertRecipeDetails(recipeDetails)
        getDetailsRecipeInfo()

    }


    fun like() = viewModelScope.launch {
        val recipe = currentRecipe?.copy(isLike = true)
        recipe?.let { replaceRecipe(it) }
    }

    fun disLike() = viewModelScope.launch {
        val recipe = currentRecipe?.copy(isLike = false)
        recipe?.let { replaceRecipe(it) }
    }


//


    fun getDetailsRecipeInfo() {


        viewModelScope.launch(Dispatchers.IO) {

            try {
                tryGetRecipeFromDataBase(id)
            } catch (e: Exception) {
                Log.d("error", " $  ${e.message}")
            }



            mutableScreenState.postValue(DetailsSearchScreenState.Loading) // при начале запроса - выставляем лоадинг в тру
            val pair = searchInteractor.searchRecipeDetailsInfo(id)
            when {
                pair.first == null && pair.second == "Exception" -> {
                    mutableScreenState.postValue(DetailsSearchScreenState.ErrorNoEnternet(pair.second))
                }

                pair.first == null && pair.second == null -> {
                    mutableScreenState.postValue(DetailsSearchScreenState.ErrorNotFound(null))
                }

                pair.first != null -> {
                    tryGetRecipeFromDataBase(id)
                    currentRecipe = pair.first
                    mutableScreenState.postValue(DetailsSearchScreenState.SearchResults(pair.first))
                }
            }
        }
    }

}











