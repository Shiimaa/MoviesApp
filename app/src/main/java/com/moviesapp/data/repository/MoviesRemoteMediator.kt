package com.moviesapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.moviesapp.database.AppDatabase
import com.moviesapp.database.AppPreference
import com.moviesapp.database.entity.MovieEntity
import com.moviesapp.database.entity.RemoteKeys
import com.moviesapp.domain.api.MovieApi
import com.moviesapp.domain.response.toDbMovie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class MoviesRemoteMediator constructor(
    private val db: AppDatabase,
    private val sharedPreferences: AppPreference,
    private val apiService: MovieApi
) : RemoteMediator<Int, MovieEntity>() {
    private var lastPage = -1

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        val pageKeyData = getKeyPageData(loadType, state)
        val page = when (pageKeyData) {
            is MediatorResult.Success -> {
                return pageKeyData
            }

            else -> {
                pageKeyData as Int
            }
        }

        try {
            val response = apiService.getMoviesList(page)
            val endOfList = response.results.isEmpty() || lastPage == response.page
            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.remoteKeyDao().clearAll()
                    db.getMoviesDao().clearAll()
                }

                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfList) null else page + 1
                lastPage = response.page

                val keys = response.results.map {
                    RemoteKeys(it.id.toString(), prevKey, nextKey)
                }

                db.remoteKeyDao().insertRemote(keys)

                val res = response.results.map { remoteMovie ->

                    val isFavorite =
                        sharedPreferences.isItemInList(AppPreference.MOIVES_KEY, remoteMovie.id)

                    remoteMovie.toDbMovie(isFavorite)
                }

                db.getMoviesDao().insert(res)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfList)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }


    private suspend fun getKeyPageData(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRefreshRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                val prevKey = remoteKeys?.prevKey ?: MediatorResult.Success(
                    endOfPaginationReached = false
                )
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextKey ?: MediatorResult.Success(
                    endOfPaginationReached = true
                )
                nextKey
            }
        }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, MovieEntity>): RemoteKeys? {
        return withContext(Dispatchers.IO) {
            state.pages
                .firstOrNull { it.data.isNotEmpty() }
                ?.data?.firstOrNull()
                ?.let { movie -> db.remoteKeyDao().getRemoteKeys(movie.id.toString()) }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, MovieEntity>): RemoteKeys? {
        return withContext(Dispatchers.IO) {
            state.pages
                .lastOrNull { it.data.isNotEmpty() }
                ?.data?.lastOrNull()
                ?.let { movie -> db.remoteKeyDao().getRemoteKeys(movie.id.toString()) }
        }
    }

    private suspend fun getRefreshRemoteKey(state: PagingState<Int, MovieEntity>): RemoteKeys? {
        return withContext(Dispatchers.IO) {
            state.anchorPosition?.let { position ->
                state.closestItemToPosition(position)?.id?.let { repId ->
                    db.remoteKeyDao().getRemoteKeys(repId.toString())
                }
            }
        }
    }

}