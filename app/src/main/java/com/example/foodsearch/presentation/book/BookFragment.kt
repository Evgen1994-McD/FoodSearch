package com.example.foodsearch.presentation.book

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.foodsearch.R
import com.example.foodsearch.databinding.FragmentBookBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


class BookFragment : Fragment() {
    private lateinit var binding: FragmentBookBinding
    private val viewModel: BookFragmentViewModel by viewModels()
    private var currentPagePosition: Int = 0
    private lateinit var vpAdapter: VpAdapter

    companion object {
        const val savedPageKey = "savedPage"


        fun newInstance() = BookFragment()


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeCurrentTabs()

        val tabs = binding.tabLayout
        val pager = binding.viewpager
        val vpAdapter = VpAdapter(this)

        if (pager.adapter == null) {
            pager.adapter = vpAdapter
        }

        pager.currentItem = currentPagePosition
        tabs.selectTab(tabs.getTabAt(currentPagePosition))


        // Ждем окончания раскладки UI
        view.postDelayed({
            // Здесь гарантированно запустится после полного рендеринга

            pager.currentItem = currentPagePosition
            tabs.selectTab(tabs.getTabAt(currentPagePosition))

        }, 0) // Небольшая задержка для гарантии полноты отображения


        // Оповещаем ViewPager2 о наших страницах
        setupTabsAndPager(pager, tabs)

        // Уведомляем TabLayout о выбранной вкладке
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                pager.currentItem = tab.position
                viewModel.setCurrentTabPosition(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        // Callback для изменения страницы
        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tabs.selectTab(tabs.getTabAt(position))
            }
        })

        // Ограничиваем кэширование страниц
        pager.offscreenPageLimit = 1

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(savedPageKey, currentPagePosition)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            currentPagePosition = it.getInt(savedPageKey, 0)
            binding.viewpager.currentItem = currentPagePosition
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(currentPagePosition))


        }
    }

    private fun setupTabsAndPager(pager: ViewPager2, tabs: TabLayout) {
        TabLayoutMediator(tabs, pager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorite)
                1 -> tab.text = getString(R.string.book_menu)
            }
        }.attach()
    }

    private fun observeCurrentTabs() {
        viewModel.currentTabPosition.observe(viewLifecycleOwner) { position ->
            currentPagePosition = position


        }
    }
}