package com.example.foodsearch.presentation.search

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodsearch.R
import com.example.foodsearch.databinding.FragmentSearchBinding
import com.example.foodsearch.domain.models.Recipe
import com.example.foodsearch.utils.debounce
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {


    private val viewModel: SearchViewModel by viewModels()
    private lateinit var binding: FragmentSearchBinding
    private lateinit var pbs: ProgressBar
    private var lastState: List<Recipe> = emptyList()
    private lateinit var txtForSearch: String
    private lateinit var searchDebounce: (String) -> Unit
    private var oldText: CharSequence = ""
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
pbs = binding.pbs
        textChangeListener()
        searchDebounce =
            debounce(2000L, viewLifecycleOwner.lifecycleScope, true) { txtForSearch ->
                viewModel.searchRecipes(txtForSearch)
            }

        binding.inputEditText.setOnFocusChangeListener { _, hasFocus ->

            observeRecipeSearchResults(hasFocus)


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


//                    phForNothingToShow.makeGone()

//                    msgTopTxt.makeGone()
//                    msgBotTxt.makeGone()
//                    buttonNoInternet.makeGone()

                    }
                }


            }

        })
    }
            private fun observeRecipeSearchResults(hasFocus: Boolean) {
                viewModel.getLiveData.observe(viewLifecycleOwner) { newState ->

                    when (newState) {
                        is SearchScreenState.Loading -> {
                            pbs.makeVisible()
                        }

                        is SearchScreenState.ErrorNoEnternet -> {
                            if (newState.message == "Exception") {
                                pbs.makeGone()
                            }
                        }

                        is SearchScreenState.ErrorNotFound -> {
                            if (newState.message == "retry") {
                                pbs.makeGone()
                            } else if (newState.message == null) {
                                pbs.makeGone()
                            }
                        }


                        is SearchScreenState.SearchResults -> {
                            pbs.makeGone()
                            val recipeToDisplay = newState.data
                            if (recipeToDisplay != null) {
                                lastState = recipeToDisplay
                            }
                            recipeToDisplay?.let { displayRecipes(it) }

                        }
                    }





                }
            }


            private fun displayRecipes(recipes: List<Recipe>) = with(binding) {
                id.text = recipes.size.toString()
//
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        recyclerView.adapter = TrackAdapter(tracks, this)
//        recyclerView.makeVisible()
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



    }








