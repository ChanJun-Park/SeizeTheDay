package com.jingom.seizetheday.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jingom.seizetheday.data.db.dao.ThanksRecordEntityDao
import com.jingom.seizetheday.data.db.model.toDomainModel
import com.jingom.seizetheday.domain.model.ThanksRecordWithImages

class ThanksRecordWithImagesPagingSource(
	private val thanksRecordEntityDao: ThanksRecordEntityDao,
	private val pageConfigSize: Int = 15
) : PagingSource<Int, ThanksRecordWithImages>() {

	private var isInitialized: Boolean = false
	private var totalRecordCount: Int = 0
	private var totalPageCount: Int = 0

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ThanksRecordWithImages> {
		if (isInitialized.not()) {
			init()
		}

		val currentPage = params.key ?: 0

		return try {
			val result = loadCurrentPageData(currentPage)

			val prevKey = if (currentPage == 0) null else currentPage - 1
			val prevPageSize = if (currentPage == 0) 0 else pageConfigSize

			val nextKey = if (currentPage == totalPageCount - 1) null else currentPage + 1
			val nextPageSize = if (currentPage == totalPageCount - 1) 0 else pageConfigSize

			LoadResult.Page(
				data = result,
				prevKey = prevKey,
				nextKey = nextKey,
				itemsBefore = prevPageSize,
				itemsAfter = nextPageSize
			)
		} catch (e: Exception) {
			LoadResult.Error(e)
		}
	}

	private suspend fun init() {
		initTotalRecordCount()
		initTotalPageCount()
	}

	private suspend fun initTotalRecordCount() {
		totalRecordCount = thanksRecordEntityDao.selectThanksRecordCount()
	}

	private fun initTotalPageCount() {
		var pageCount = totalRecordCount / pageConfigSize

		if (totalRecordCount % pageConfigSize != 0) {
			pageCount += 1
		}

		totalPageCount = pageCount
	}

	private suspend fun loadCurrentPageData(currentPage: Int): List<ThanksRecordWithImages> {
		return thanksRecordEntityDao.getThanksRecordWithAttachedImageEntities(
			offset = (currentPage - 1).coerceAtLeast(0) * pageConfigSize,
			perPageSize = pageConfigSize
		).map { it.toDomainModel() }
	}

	override fun getRefreshKey(state: PagingState<Int, ThanksRecordWithImages>): Int? {
		return state.anchorPosition?.let {
			state.closestPageToPosition(it)?.prevKey?.plus(1)
				?: state.closestPageToPosition(it)?.nextKey?.minus(1)
		}
	}
}