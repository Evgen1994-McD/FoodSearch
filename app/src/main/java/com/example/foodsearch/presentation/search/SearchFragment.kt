package com.example.foodsearch.presentation.search

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.ProgressBar
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodsearch.R
import com.example.foodsearch.databinding.FragmentSearchBinding
import com.example.foodsearch.domain.models.RecipeSummary
import com.example.foodsearch.presentation.search.adapter.OnRecipeClickListener
import com.example.foodsearch.presentation.search.adapter.RecipeAdapter
import com.example.foodsearch.utils.debounce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(), OnRecipeClickListener {


    private val viewModel: SearchViewModel by viewModels()
    private lateinit var binding: FragmentSearchBinding
    private lateinit var pbs: ProgressBar
    private var lastState: List<RecipeSummary> = emptyList()
    private lateinit var txtForSearch: String
    private lateinit var searchDebounce: (String) -> Unit
    private var oldText: CharSequence = ""
    private var ab: ActionBar? =
        null // добавили переменную для ActionBar, будем показывать счетчик упражнений
    private var isRandomSeachComplete=false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding = FragmentSearchBinding.inflate(layoutInflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        observeRecipeSearchResults()



if (!isRandomSeachComplete){
observeRandomRecipes()
    isRandomSeachComplete=true
}





        ab =
            (activity as AppCompatActivity).supportActionBar


pbs = binding.pbs
        textChangeListener()
        searchDebounce =
            debounce(2000L, viewLifecycleOwner.lifecycleScope, true) { txtForSearch ->
           observeRecipeSearchResultsFlow(txtForSearch)
            }

        binding.inputEditText.setOnFocusChangeListener { _, hasFocus ->

            observeRecipeSearchResults()

            // Наблюдаем сразу за обоими источниками данных

        }

    }


private fun textChangeListener()=with(binding){
        inputEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


                if (!p0.isNullOrEmpty()) {
                    // инициализ переменную таск в текст ватчере, иначе происходит вылет
                    txtForSearch = p0.toString()
                    if (oldText == txtForSearch) {
                        inputEditText.requestFocus()
                        /*
                        передаю фокус на эдит текст, чтобы при возврате на экран если зашел посмотерть песню,
                        не приходилось выбирать строку чтобы отобразить результаты поиска, а сразу перебирать
                        уже песни в RecyclerView
                         */

                    }
                    else if (oldText != txtForSearch && txtForSearch.isNotEmpty()) {
                        oldText = txtForSearch
                        /*
                        C помощью текст ватчера проверяю изменился ли текст после возвращения через popBackStack()
                        и выполняю поисковый запрос только при наличии изменений ( убрал неприятный прогресс бар при возврате на экран -
                        - появлялся на пару секунд выполняя повторный запрос)
                         */
                        searchDebounce(txtForSearch)




                    }
                }


            }

        })
    }
            private fun observeRecipeSearchResults() {
                viewModel.getLiveData.observe(viewLifecycleOwner) { newState ->
                    when (newState){
                        is SearchScreenState.Loading -> {
                            binding.rcView.makeGone()
                            pbs.makeVisible()

                        }

                        is SearchScreenState.ErrorNoEnternet -> {
                            if (newState.message == "Exception") {
binding.im404.makeVisible()
binding.tvNothingToShow.makeVisible()
                                pbs.makeGone()
                            }
                        }

                        is SearchScreenState.ErrorNotFound -> {
                            if (newState.message == "retry") {
                                binding.im404.makeVisible()
                                binding.tvNothingToShow.makeVisible()

                                pbs.makeGone()
                            } else if (newState.message == null) {
                                pbs.makeGone()
                                binding.im404.makeVisible()
                                binding.tvNothingToShow.makeVisible()

                            }
                        }


                        is SearchScreenState.SearchResults -> {
                            pbs.makeGone()
                            binding.im404.makeGone()
                            binding.tvNothingToShow.makeGone()

                            binding.rcView.makeVisible()


                            val recipeToDisplay = newState.data
                            if (recipeToDisplay != null) {
                                lastState = recipeToDisplay
                            }
                            recipeToDisplay?.let { displayRecipes(it) }

                        }
                    }





                }
            }

    private  fun observeRecipeSearchResultsFlow(string: String) = lifecycleScope.launch {
        viewModel.searchRecipes(string).collectLatest { pagingData ->
            binding.rcView.layoutManager = LinearLayoutManager(requireContext())
            binding.rcView.adapter = RecipeAdapter(this@SearchFragment, requireContext()).also { adapter ->
                adapter.submitData(lifecycle, pagingData)
            }
        }
    }


    private  fun observeRandomRecipes() = lifecycleScope.launch {
        viewModel.getRandomRecipes().collectLatest { pagingData ->
            binding.rcView.layoutManager = LinearLayoutManager(requireContext())
            binding.rcView.adapter = RecipeAdapter(this@SearchFragment, requireContext()).also { adapter ->
                adapter.submitData(lifecycle, pagingData)
            }
        }

    }

            private fun displayRecipes(recipeSummaries: List<RecipeSummary>) = with(binding) {

        rcView.layoutManager = LinearLayoutManager(requireContext())
        rcView.adapter = RecipeAdapter( this@SearchFragment, requireContext())
        rcView.makeVisible()
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

    override fun onRecipeClicker(recipeSummary: RecipeSummary) {

      findNavController().navigate(R.id.action_searchFragment_to_detailsRecipe, bundleOf("id" to (recipeSummary.id)))

    }


}








