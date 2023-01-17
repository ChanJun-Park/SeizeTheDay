package com.jingom.seizetheday.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
	primary = Red100Dark,
	primaryVariant = RedA100Dark,
	secondary = Purple100Dark,
	secondaryVariant = PurpleA100Dark,
)

private val LightColorPalette = lightColors(
	primary = Red100,
	primaryVariant = RedA100,
	secondary = Purple100,
	secondaryVariant = PurpleA100,
	background = Red50,
	onPrimary = Color.Black,

	/* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun SeizeTheDayTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
	val colors = if (darkTheme) {
		DarkColorPalette
	} else {
		LightColorPalette
	}

	MaterialTheme(
		colors = colors,
		typography = Typography,
		shapes = Shapes,
		content = content
	)
}