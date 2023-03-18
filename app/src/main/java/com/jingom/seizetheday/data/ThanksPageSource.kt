package com.jingom.seizetheday.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jingom.seizetheday.data.db.dao.ThanksRecordEntityDao
import com.jingom.seizetheday.data.db.model.ThanksRecordEntity

class ThanksPageSource(
	private val thanksRecordEntityDao: ThanksRecordEntityDao,
	private val startThanksId: Long?,
	private val pageConfigSize: Int = 15,
) : PagingSource<Int, ThanksRecordEntity>() {

	private var isInitialized: Boolean = false
	private var totalRecordCount: Int = 0
	private var startTargetThanksIdIndex: Int? = null
	private var totalPageCount: Int = 0
	private var startPageIndex: Int = 0

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ThanksRecordEntity> {
		if (isInitialized.not()) {
			init()
		}

		val currentPage = params.key ?: startPageIndex

		return try {
			val result = loadCurrentPageData(currentPage)

			val prevKey = if (currentPage == 0) null else currentPage - 1
			val prevPageSize = if (currentPage == 0) 0 else pageConfigSize

			val nextKey = if (currentPage == totalPageCount - 1) null else currentPage + 1
			val nextPageSize = if (currentPage == totalPageCount - 1) {
				0
			} else {
				getPageSize(currentPage + 1)
			}

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

	private fun init() {
		initTotalRecordCount()
		initStartTargetThanksIdRowIndex()
		initTotalPageCount()
		initStartPageIndex()
	}

	private fun initTotalRecordCount() {
		totalRecordCount = thanksRecordEntityDao.selectThanksRecordCount()
	}

	private fun initStartTargetThanksIdRowIndex() {
		val startTargetId = startThanksId ?: return
		val ids = thanksRecordEntityDao.selectThanksIdsInDateOrder()

		var targetIdIndex: Int? = ids.indexOf(startTargetId)
		if (targetIdIndex == -1) {
			targetIdIndex = null
		}

		startTargetThanksIdIndex = targetIdIndex
	}

	private fun initTotalPageCount() {
		var pageCount = totalRecordCount / pageConfigSize

		if (totalRecordCount % pageConfigSize != 0) {
			pageCount += 1
		}

		totalPageCount = pageCount
	}

	private fun initStartPageIndex() {
		startPageIndex = startTargetThanksIdIndex?.let {
			(it / pageConfigSize)
		} ?: 0
	}

	private suspend fun loadCurrentPageData(
		currentPage: Int
	) = thanksRecordEntityDao.getThanksRecordEntities(
		offset = (currentPage - 1) * pageConfigSize,
		perPageSize = getPageSize(currentPage)
	)

	private fun getPageSize(currentPage: Int) = if (currentPage == totalPageCount - 1) {
		if (totalRecordCount % pageConfigSize == 0) {
			pageConfigSize
		} else {
			totalRecordCount % pageConfigSize
		}
	} else {
		pageConfigSize
	}

	override fun getRefreshKey(state: PagingState<Int, ThanksRecordEntity>): Int? {
		return state.anchorPosition?.let {
			state.closestPageToPosition(it)?.prevKey?.plus(1)
				?: state.closestPageToPosition(it)?.nextKey?.minus(1)
		}
	}
}