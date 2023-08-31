@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.org.jetbrains.kotlin.android)
}

android {
	namespace = "com.jingom.seizetheday"
	compileSdk = 33

	defaultConfig {
		applicationId = "com.jingom.seizetheday"
		minSdk = 30
		targetSdk = 33
		versionCode = 1
		versionName = "1.0"
		vectorDrawables {
			useSupportLibrary = true
		}

	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
	compileOptions {
		// Flag to enable support for the new language APIs
		isCoreLibraryDesugaringEnabled = true

		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
	kotlinOptions {
		jvmTarget = "1.8"
	}
	buildFeatures {
		compose = true
	}
	composeOptions {
		kotlinCompilerExtensionVersion = "1.4.3"
	}
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
}

dependencies {
	coreLibraryDesugaring(libs.desugar)

	implementation(libs.androidx.core.ktx)
	implementation(libs.play.services.wearable)
	implementation(libs.percentlayout)
	implementation(libs.legacy.support.v4)
	implementation(libs.recyclerview)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.compose.ui)
	implementation(libs.androidx.compose.ui.tooling.preview)
	implementation(libs.compose.material)
	implementation(libs.compose.foundation)
	implementation(libs.androidx.lifecycle.runtime)
	implementation(libs.androidx.activity.compose)
	implementation(libs.tiles)
	implementation(libs.tiles.material)
	implementation(libs.horologist.compose.tools)
	implementation(libs.horologist.tiles)
	implementation(libs.watchface.complications.data.source.ktx)
	androidTestImplementation(platform(libs.androidx.compose.bom))
	androidTestImplementation(libs.androidx.compose.ui.test.junit4)
	debugImplementation(libs.androidx.compose.ui.tooling)
	debugImplementation(libs.androidx.compose.ui.test.manifest)
}