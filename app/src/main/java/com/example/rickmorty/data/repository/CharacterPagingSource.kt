package com.example.rickmorty.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickmorty.data.api.RickAndMortyApiService
import com.example.rickmorty.data.mapper.toDomain
import com.example.rickmorty.domain.model.Character

class CharacterPagingSource(
    private val apiService: RickAndMortyApiService,
    private val name: String? = null,
    private val status: String? = null,
    private val species: String? = null,
    private val gender: String? = null
) : PagingSource<Int, Character>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        val page = params.key ?: 1
        return try {
            val response = apiService.getCharacters(
                page = page,
                name = name,
                status = status,
                species = species,
                gender = gender
            )
            val characters = response.results.map { it.toDomain() }
            val nextKey = if (response.info.next != null) page + 1 else null
            val prevKey = if (page == 1) null else page - 1
            LoadResult.Page(
                data = characters,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
