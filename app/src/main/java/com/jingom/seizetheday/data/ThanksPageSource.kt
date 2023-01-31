package com.jingom.seizetheday.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jingom.seizetheday.data.dao.ThanksRecordEntityDao
import com.jingom.seizetheday.data.model.ThanksRecordEntity

class ThanksPageSource(
	private val thanksRecordEntityDao: ThanksRecordEntityDao
): PagingSource<Int, ThanksRecordEntity>() {
	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ThanksRecordEntity> {
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

	override fun getRefreshKey(state: PagingState<Int, ThanksRecordEntity>): Int? {
		return state.anchorPosition?.let {
			state.closestPageToPosition(it)?.prevKey?.plus(1)
				?: state.closestPageToPosition(it)?.nextKey?.minus(1)
		}
	}
}