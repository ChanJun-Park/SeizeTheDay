object Compose {
    const val composeBomVersion = "2023.01.00"
    const val composeCompilerVersion = "1.4.0"

    const val composeBom = "androidx.compose:compose-bom:$composeBomVersion"

    const val material = "androidx.compose.material:material"
    const val ui = "androidx.compose.ui:ui"
    const val uiTooling = "androidx.compose.ui:ui-tooling"
    const val uiToolingPreview = "androidx.compose.ui:ui-tooling-preview"
    const val testManifest = "androidx.compose.ui:ui-test-manifest"
    const val runtime = "androidx.compose.runtime:runtime"
    const val composeUiTest = "androidx.compose.ui:ui-test-junit4"

    private const val navigationVersion = "2.4.0-beta02"
    const val navigation = "androidx.navigation:navigation-compose:$navigationVersion"

    private const val hiltNavigationComposeVersion = "1.0.0-beta01"
    const val hiltNavigationCompose = "androidx.hilt:hilt-navigation-compose:$hiltNavigationComposeVersion"

    private const val activityComposeVersion = "1.4.0"
    const val activityCompose = "androidx.activity:activity-compose:$activityComposeVersion"

    private const val lifecycleVersion = "2.4.0"
    const val viewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion"

    private const val customViewVersion = "1.2.0-alpha01"
    const val composePreviewCustomView = "androidx.customview:customview:$customViewVersion"

    private const val customViewVersionPoolingContainer = "1.0.0-alpha01"
    const val composePreviewCustomViewPoolingContainer = "androidx.customview:customview-poolingcontainer:$customViewVersionPoolingContainer"
}
