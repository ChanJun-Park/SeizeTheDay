package com.jingom.seizetheday.core.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jingom.seizetheday.R

@Composable
fun SimpleToolBar(
	modifier: Modifier = Modifier,
	hasNavigationButton: Boolean = false,
	hasOptionMenuButton: Boolean = false,
	title: String = "",
	onNavigateBackClick: () -> Unit = {},
	onOptionMenuClick: () -> Unit = {}
) {
	SimpleToolbarScaffold(
		modifier = modifier,
		navigationButton = {
			OptionalNavigateBackButton(hasNavigationButton, onNavigateBackClick)
		},
		title = {
			Text(text = title)
		},
		optionMenu = {
			OptionalMenuButton(hasOptionMenuButton, onOptionMenuClick)
		}
	)
}

@Composable
private fun OptionalNavigateBackButton(hasNavigationButton: Boolean, onNavigateBackClick: () -> Unit) {
	if (hasNavigationButton) {
		NavigateBackButton(
			modifier = Modifier.size(48.dp),
			onClick = onNavigateBackClick
		)
	}
}

@Composable
private fun OptionalMenuButton(hasOptionMenuButton: Boolean, onOptionMenuClick: () -> Unit) {
	if (hasOptionMenuButton) {
		MenuButton(
			modifier = Modifier.size(48.dp),
			onClick = onOptionMenuClick
		)
	}
}

@Preview
@Composable
fun SimpleToolBarPreview() {
	SimpleToolBar(
		modifier = Modifier
			.fillMaxWidth()
			.height(60.dp),
		hasNavigationButton = true,
		hasOptionMenuButton = true,
		title = "test title"
	)
}

@Composable
fun SimpleToolbarScaffold(
	modifier: Modifier = Modifier,
	navigationButton: @Composable () -> Unit = {},
	title: @Composable () -> Unit = {},
	optionMenu: @Composable () -> Unit = {}
) {
	Box(modifier = modifier) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween,
			modifier = Modifier.matchParentSize()
		) {
			navigationButton()
			optionMenu()
		}
		Box(modifier = Modifier.align(Alignment.Center)) {
			title()
		}
	}

}

@Composable
fun NavigateBackButton(
	modifier: Modifier = Modifier,
	contentDescription: String? = "뒤로가기",
	onClick: () -> Unit = {}
) {
	IconButton(
		onClick = onClick,
		modifier = modifier.requiredSize(48.dp)
	) {
		Icon(
			painter = painterResource(id = R.drawable.ic_navigate_before),
			tint = MaterialTheme.colors.primary,
			contentDescription = contentDescription
		)
	}
}

@Preview
@Composable
fun NavigateBackButtonPreview() {
	NavigateBackButton(
		modifier = Modifier.size(48.dp)
	)
}

@Composable
fun MenuButton(
	modifier: Modifier = Modifier,
	contentDescription: String? = "더보기",
	onClick: () -> Unit = {}
) {
	IconButton(
		onClick = onClick,
		modifier = modifier.requiredSize(48.dp)
	) {
		Icon(
			painter = painterResource(id = R.drawable.ic_more_horiz),
			tint = MaterialTheme.colors.primary,
			contentDescription = contentDescription
		)
	}
}

@Preview
@Composable
fun MenuButtonPreview() {
	MenuButton(
		modifier = Modifier.size(48.dp)
	)
}
