package com.jingom.seizetheday.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jingom.seizetheday.data.dao.ThanksRecordEntityDao
import com.jingom.seizetheday.data.model.ThanksRecordEntity

class ThanksPageSource(
	private val thanksRecordEntityDao: ThanksRecordEntityDao,
	private val startThanksId: Int?
): PagingSource<Int, ThanksRecordEntity>() {
	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ThanksRecordEntity> {
		return if (startThanksId == null) {
			loadFromStart(params)
		} else {
			loadFromStartThanksId(params)
		}
	}

	private suspend fun loadFromStart(params: LoadParams<Int>): LoadResult<Int, ThanksRecordEntity> {
		val currentPage = params.key ?: 1

		return try {
			val result = thanksRecordEntityDao.getThanksRecordEntities(
				offset = (currentPage - 1) * params.loadSize,
				perPageSize = params.loadSize
			)

			LoadResult.Page(
				data = result,
				prevKey = if (currentPage == 1) null else currentPage - 1,
				nextKey = if (result.isEmpty()) null else currentPage + 1
			)
		} catch (e: Exception) {
			LoadResult.Error(e)
		}
	}

	private suspend fun loadFromStartThanksId(params: LoadParams<Int>): LoadResult<Int, ThanksRecordEntity> {
		val targetId = startThanksId ?: throw IllegalStateException("loadFromStartThanksId must be called with non null startThanksId")

		val ids = thanksRecordEntityDao.getThanksRecordEntityIds()
		var targetIdRowIndex = ids.indexOf(targetId)
		if (targetIdRowIndex == -1) {
			targetIdRowIndex = 0
		}

		val startPage = (targetIdRowIndex / params.loadSize) + 1
		val currentPage = params.key ?: startPage

		val offset = (targetIdRowIndex + (currentPage - startPage) * params.loadSize).coerceAtLeast(0)

		return try {
			val result = thanksRecordEntityDao.getThanksRecordEntities(
				offset = offset,
				perPageSize = if (offset == 0) targetIdRowIndex else params.loadSize
			)

			LoadResult.Page(
				data = result,
				prevKey = if ((currentPage == 1 && ((targetIdRowIndex % params.loadSize) == 0)) || currentPage == 0) null else currentPage - 1,
				nextKey = if (result.isEmpty()) null else currentPage + 1
			)
		} catch (e: Exception) {
			LoadResult.Error(e)
		}
	}

	override fun getRefreshKey(state: PagingState<Int, ThanksRecordEntity>): Int? {
		return state.anchorPosition?.let {
			state.closestPageToPosition(it)?.prevKey?.plus(1)
				?: state.closestPageToPosition(it)?.nextKey?.minus(1)
		}
	}
}