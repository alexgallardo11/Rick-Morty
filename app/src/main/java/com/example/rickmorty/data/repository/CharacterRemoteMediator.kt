package com.example.rickmorty.data.repository

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.rickmorty.data.api.RickAndMortyApiService
import com.example.rickmorty.data.local.AppDatabase
import com.example.rickmorty.data.local.dao.CharacterDao
import com.example.rickmorty.data.local.entity.CharacterEntity
import com.example.rickmorty.data.mapper.toEntity
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
    private val apiService: RickAndMortyApiService,
    private val database: AppDatabase,
    private val name: String? = null,
    private val status: String? = null,
    private val species: String? = null,
    private val gender: String? = null
) : RemoteMediator<Int, CharacterEntity>() {

    private val characterDao: CharacterDao = database.characterDao()

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) 1 else (lastItem.id / state.config.pageSize) + 1
                }
            }

            val response = apiService.getCharacters(
                page = page,
                name = name,
                status = status,
                species = species,
                gender = gender
            )
            val entities = response.results.map { it.toEntity() }
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    characterDao.clearAll()
                }
                characterDao.insertAll(entities)
            }
            val endOfPagination = response.info.next == null
            MediatorResult.Success(endOfPaginationReached = endOfPagination)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
