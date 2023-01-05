object ProjectConfig {
    const val appId = "com.jingom.seizetheday"
    const val compileSdk = 33
    const val minSdk = 23
    const val targetSdk = 33

    const val versionMajor = 0
    const val versionMinor = 0
    const val versionPatch = 0
    const val versionBuild = 1

    const val versionName = "$versionMajor.$versionMinor.$versionPatch"
    const val versionFullName = "$versionName.$versionBuild"
    const val versionCode = versionMajor * 1000000 + versionMinor * 10000 + versionPatch * 100 + versionBuild
}