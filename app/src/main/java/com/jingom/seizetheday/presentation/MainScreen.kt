package com.jingom.seizetheday.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jingom.seizetheday.presentation.list.ListThanksScreen
import com.jingom.seizetheday.presentation.write.WritingThanksScreen

@Composable
fun MainScreen() {
	val navController = rememberNavController()

	NavHost(
		navController = navController,
		startDestination = Route.WRITING_THANKS_SCREEN
	) {
		composable(Route.LIST_THANKS_SCREEN) {
			ListThanksScreen(
				onNewThanksClick = navController::navigateFromListThanksToWritingThanks
			)
		}

		composable(Route.WRITING_THANKS_SCREEN) {
			WritingThanksScreen(
				onWritingCancel = navController::navigateFromWritingThanksToListThanks,
				onWritingDone = navController::navigateFromWritingThanksToListThanks,
			)
		}
	}
}

private fun NavHostController.navigateFromWritingThanksToListThanks() = navigate(
	route = Route.LIST_THANKS_SCREEN,
	navOptions = NavOptions.Builder()
		.setPopUpTo(
			route = Route.WRITING_THANKS_SCREEN,
			inclusive = true
		)
		.build()
)

private fun NavHostController.navigateFromListThanksToWritingThanks() = navigate(
	route = Route.WRITING_THANKS_SCREEN,
	navOptions = NavOptions.Builder()
		.setPopUpTo(
			route = Route.LIST_THANKS_SCREEN,
			inclusive = true
		)
		.build()
)
