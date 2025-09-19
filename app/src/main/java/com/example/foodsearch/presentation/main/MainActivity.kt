package com.example.foodsearch.presentation.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.foodsearch.R
import com.example.foodsearch.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val viewModel:MainViewModel by viewModels()
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController:NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar = binding.materialToolbar
        setSupportActionBar(toolbar)




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController =  navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.searchFragment -> {
                    supportActionBar?.title = getString(R.string.greetings)
                    binding.materialToolbar.navigationIcon = null

                }




            }


        }
    }

    //
    private  fun navigateAndClearOldFragments(destinationId: Int): Boolean {
        // Чистим стек навигации и переходим на указанный пункт
        navController.popBackStack()
        navController.navigate(destinationId)
        return true
    }



    override fun onDestroy() {
        super.onDestroy()

    }

}