package com.example.rickmorty.data

import io.ktor.http.*
import com.example.rickmorty.data.model.CharacterResponseDto
import com.example.rickmorty.data.model.CharacterDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse

class RickAndMortyApiService(private val client: HttpClient) {
    private val baseUrl = "https://rickandmortyapi.com/api"

    suspend fun getCharacters(
        page: Int = 1,
        name: String? = null,
        status: String? = null,
        species: String? = null,
        gender: String? = null
    ): CharacterResponseDto {
        val response: HttpResponse = client.get("$baseUrl/character") {
            parameter("page", page)
            name?.let { parameter("name", it) }
            status?.let { parameter("status", it) }
            species?.let { parameter("species", it) }
            gender?.let { parameter("gender", it) }
        }
        return response.body()
    }

    suspend fun getCharacterDetail(id: Int): CharacterDto {
        val response: HttpResponse = client.get("$baseUrl/character/$id")
        return response.body()
    }
}
