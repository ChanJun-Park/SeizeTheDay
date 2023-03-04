package com.jingom.seizetheday.core.ui

import com.jingom.seizetheday.MainCoroutineRule
import com.jingom.seizetheday.domain.usecase.GetMediaImageAlbumsUseCase
import com.jingom.seizetheday.domain.usecase.GetMediaImagesUseCase
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
@Suppress("NonAsciiCharacters")
class LocalImagePickerViewModelTest {

	@get:Rule
	var mainCoroutineRule = MainCoroutineRule()

	private lateinit var localImagePickerViewModel: LocalImagePickerViewModel

	private lateinit var getMediaImages: GetMediaImagesUseCase
	private lateinit var getMediaImageAlbums: GetMediaImageAlbumsUseCase

	@Before
	fun setup() {
		getMediaImages = mockk(relaxed = true)
		getMediaImageAlbums = mockk(relaxed = true)

		localImagePickerViewModel = LocalImagePickerViewModel(
			getMediaImages = getMediaImages,
			getMediaImageAlbums = getMediaImageAlbums
		)
	}

	@After
	fun tearDown() {
		unmockkAll()
	}
}