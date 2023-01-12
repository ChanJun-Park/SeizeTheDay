package com.jingom.seizetheday.presentation.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ListThanksScreen(
	viewModel: ListThanksViewModel = hiltViewModel()
) {
	val state = viewModel.thanksRecords.collectAsState().value

	ListThanksDashboardScreen(
		state = state
	)
}