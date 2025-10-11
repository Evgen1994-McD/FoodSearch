package com.example.foodsearch.presentation.book

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.foodsearch.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookScreen(
    viewModel: BookFragmentViewModel = hiltViewModel()
) {
    val currentTabPosition by viewModel.currentTabPosition.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(
        initialPage = currentTabPosition,
        pageCount = { 2 }
    )
    
    LaunchedEffect(pagerState.currentPage) {
        viewModel.setCurrentTabPosition(pagerState.currentPage)
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Tab Row
        TabRow(
            selectedTabIndex = pagerState.currentPage
        ) {
            Tab(
                selected = pagerState.currentPage == 0,
                text = {
                    Text(
                        text = stringResource(R.string.favorite),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            )
            Tab(
                selected = pagerState.currentPage == 1,
                text = {
                    Text(
                        text = stringResource(R.string.book_menu),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            )
        }
        
        // Content
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> FavoriteScreen()
                1 -> SavedScreen()
            }
        }
    }
}

@Composable
fun FavoriteScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Favorite Recipes",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Composable
fun SavedScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Saved Recipes",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}