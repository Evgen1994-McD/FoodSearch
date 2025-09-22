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
import androidx.compose.runtime.snapshotFlow
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodsearch.R
import com.example.foodsearch.databinding.FragmentSearchBinding
import com.example.foodsearch.domain.models.RecipeSummary
import com.example.foodsearch.presentation.book.BookFragment
import com.example.foodsearch.presentation.search.adapter.OnRecipeClickListener
import com.example.foodsearch.presentation.search.adapter.RecipeAdapter
import com.example.foodsearch.utils.debounce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(), OnRecipeClickListener {

    companion object {
        fun newInstance() = SearchFragment()

    }

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var binding: FragmentSearchBinding
    private lateinit var pbs: ProgressBar
    private lateinit var txtForSearch: String
    private lateinit var searchDebounce: (String) -> Unit
    private var oldText: CharSequence = ""
    private var ab: ActionBar? =
        null // добавили переменную для ActionBar, будем показывать счетчик упражнений
    private var isRandomSeachComplete = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeRecipeSearchResults()
        if (!isRandomSeachComplete) {
            viewModel.getRandomRecipes()
            isRandomSeachComplete = true
        }
        ab =
            (activity as AppCompatActivity).supportActionBar
        pbs = binding.pbs
        textChangeListener()
        searchDebounce =
            debounce(2000L, viewLifecycleOwner.lifecycleScope, true) { txtForSearch ->
                viewModel.searchRecipes(txtForSearch)
            }
        binding.inputEditText.setOnFocusChangeListener { _, hasFocus ->
            observeRecipeSearchResults()
        }
    }

    private fun textChangeListener() = with(binding) {
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
                    } else if (oldText != txtForSearch && txtForSearch.isNotEmpty()) {
                        oldText = txtForSearch

                        searchDebounce(txtForSearch)
                    }
                }
            }

        })
    }

    private fun observeRecipeSearchResults() {
        viewModel.getLiveData.observe(viewLifecycleOwner) { newState ->
            when (newState) {
                is SearchScreenState.Loading -> {
                    binding.rcView.makeGone()
                    pbs.makeVisible()

                }

                is SearchScreenState.ErrorNotFound -> {
                    binding.im404.makeVisible()
                    binding.tvNothingToShow.makeVisible()
                    pbs.makeGone()
                }

                is SearchScreenState.SearchResults -> {
                    pbs.makeGone()
                    binding.im404.makeGone()
                    binding.tvNothingToShow.makeGone()

                    binding.rcView.makeVisible()
                    observeRecipeSearchResultsFlow(newState.data)

                }
            }
        }
    }

    private fun observeRecipeSearchResultsFlow(data: PagingData<RecipeSummary>) {
        binding.rcView.layoutManager = LinearLayoutManager(requireContext())
        binding.rcView.adapter =
            RecipeAdapter(this@SearchFragment, requireContext()).also { adapter ->
                adapter.submitData(lifecycle, data)

                // Запускаем корутин для наблюдения за состоянием загрузки
                lifecycleScope.launch {
                    adapter.loadStateFlow.collectLatest { loadStates ->
                        // Определяем условия для отображения элементов
                        when {
                            // Пока идут запросы на обновление данных
                            loadStates.refresh is LoadState.Loading -> {
                                pbs.makeVisible()
                                binding.im404.makeGone()
                                binding.tvNothingToShow.makeGone()
                                binding.rcView.makeGone()
                            }

                            // Все данные получены, проверяем их наличие
                            loadStates.refresh is LoadState.NotLoading -> {
                                pbs.makeGone()
                                if (adapter.itemCount > 0) {
                                    // Есть данные, отображаем список
                                    binding.im404.makeGone()
                                    binding.tvNothingToShow.makeGone()
                                    binding.rcView.makeVisible()
                                } else {
                                    // Нет данных, показываем уведомление
                                    binding.im404.makeVisible()
                                    binding.tvNothingToShow.makeVisible()
                                    binding.rcView.makeGone()
                                }
                            }

                            // Возникла ошибка
                            loadStates.refresh is LoadState.Error -> {
                                viewModel.getRecipeFromDb(binding.inputEditText.text.toString())

                                pbs.makeGone()
                                binding.im404.makeVisible()
                                binding.tvNothingToShow.makeVisible()
                                binding.rcView.makeGone()
                            }
                        }
                    }
                }
            }
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

        findNavController().navigate(
            R.id.action_searchFragment_to_detailsRecipe,
            bundleOf("id" to (recipeSummary.id))
        )

    }


}








