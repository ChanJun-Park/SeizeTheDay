package com.jingom.seizetheday.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jingom.seizetheday.presentation.list.ListThanksScreen
import com.jingom.seizetheday.presentation.ui.theme.SeizeTheDayTheme
import com.jingom.seizetheday.presentation.write.Route
import com.jingom.seizetheday.presentation.write.WritingThanksScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			SeizeTheDayTheme {
				MainScreen()
			}
		}
	}
}

@Composable
private fun MainScreen() {
	val navController = rememberNavController()

	NavHost(
		navController = navController,
		startDestination = Route.WRITING_THANKS_SCREEN
	) {
		composable(Route.LIST_THANKS_SCREEN) {
			ListThanksScreen()
		}

		composable(Route.WRITING_THANKS_SCREEN) {
			WritingThanksScreen(
				onWritingCancel = navController::navigateToListThanksScreen,
				onWritingDone = navController::navigateToListThanksScreen,
			)
		}
	}
}

private fun NavHostController.navigateToListThanksScreen() = navigate(
	route = Route.LIST_THANKS_SCREEN,
	navOptions = NavOptions.Builder()
		.setPopUpTo(
			route = Route.WRITING_THANKS_SCREEN,
			inclusive = true
		)
		.build()
)
