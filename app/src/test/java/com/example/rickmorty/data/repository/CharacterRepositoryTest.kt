package com.example.rickmorty.data.repository

import com.example.rickmorty.data.api.RickAndMortyApiService
import com.example.rickmorty.data.local.AppDatabase
import com.example.rickmorty.data.local.dao.CharacterDao
import com.example.rickmorty.data.local.entity.CharacterEntity
import com.example.rickmorty.data.local.entity.LocationEntity
import com.example.rickmorty.data.local.entity.OriginEntity
import com.example.rickmorty.data.mapper.toDomain
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class CharacterRepositoryTest {
    private lateinit var apiService: RickAndMortyApiService
    private lateinit var db: AppDatabase
    private lateinit var characterDao: CharacterDao
    private lateinit var repository: CharacterRepository

    companion object {
        private const val RICK_ID = 1
        private const val RICK_NAME = "Rick Sanchez"
        private const val MORTY_ID = 2
        private const val MORTY_NAME = "Morty Smith"
        private const val SUMMER_ID = 3
        private const val SUMMER_NAME = "Summer Smith"
        private const val UNKNOWN_ID = 4
        
        private const val STATUS_ALIVE = "Alive"
        private const val SPECIES_HUMAN = "Human"
        private const val GENDER_MALE = "Male"
        private const val GENDER_FEMALE = "Female"
    }
    
    private lateinit var rickEntity: CharacterEntity
    private lateinit var mortyEntity: CharacterEntity
    private lateinit var summerEntity: CharacterEntity

    @Before
    fun setup() {
        apiService = mockk()
        db = mockk()
        characterDao = mockk()
        
        every { db.characterDao() } returns characterDao
        
        repository = CharacterRepository(apiService, db)
        
        rickEntity = createCharacterEntity(
            id = RICK_ID,
            name = RICK_NAME,
            status = STATUS_ALIVE,
            species = SPECIES_HUMAN,
            gender = GENDER_MALE
        )
        
        mortyEntity = createCharacterEntity(
            id = MORTY_ID,
            name = MORTY_NAME,
            status = STATUS_ALIVE,
            species = SPECIES_HUMAN,
            gender = GENDER_MALE
        )
        
        summerEntity = createCharacterEntity(
            id = SUMMER_ID,
            name = SUMMER_NAME,
            status = STATUS_ALIVE,
            species = SPECIES_HUMAN,
            gender = GENDER_FEMALE
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    private fun createCharacterEntity(
        id: Int = 1,
        name: String = "Rick Sanchez",
        status: String = "Alive",
        species: String = "Human",
        type: String = "",
        gender: String = "Male",
        originName: String = "Earth (C-137)",
        originUrl: String = "",
        locationName: String = "Citadel of Ricks",
        locationUrl: String = "",
        image: String = "url_to_image",
        episode: List<String> = listOf("episode1", "episode2"),
        url: String = "character_url",
        created: String = "timestamp"
    ): CharacterEntity {
        val originEntity = OriginEntity(
            name = originName,
            url = originUrl
        )
        
        val locationEntity = LocationEntity(
            name = locationName,
            url = locationUrl
        )
        
        return CharacterEntity(
            id = id,
            name = name,
            status = status,
            species = species,
            type = type,
            gender = gender,
            origin = originEntity,
            location = locationEntity,
            image = image,
            episode = episode,
            url = url,
            created = created
        )
    }

    @Test
    fun `getCharacterDetail returns character from API and caches it`() = runTest {
        val expectedCharacter = rickEntity.toDomain()
        
        coEvery { apiService.getCharacterDetail(RICK_ID) } returns mockk()
        coEvery { characterDao.insertAll(any()) } just Runs
        
        val spyRepository = spyk(repository)
        
        coEvery { spyRepository.getCharacterDetail(RICK_ID) } coAnswers {
            characterDao.insertAll(listOf(rickEntity))
            expectedCharacter
        }
        
        val result = spyRepository.getCharacterDetail(RICK_ID)
        
        assertNotNull(result)
        assertEquals(RICK_ID, result?.id)
        assertEquals(RICK_NAME, result?.name)
        assertEquals(STATUS_ALIVE, result?.status)
        assertEquals(SPECIES_HUMAN, result?.species)
        assertEquals(GENDER_MALE, result?.gender)
        
        coVerify { characterDao.insertAll(listOf(rickEntity)) }
    }

    @Test
    fun `getCharacterDetail returns cached character on IOException`() = runTest {
        coEvery { apiService.getCharacterDetail(MORTY_ID) } throws java.io.IOException()
        coEvery { characterDao.getCharacterById(MORTY_ID) } returns mortyEntity
        
        val result = repository.getCharacterDetail(MORTY_ID)
        
        assertNotNull(result)
        assertEquals(MORTY_ID, result?.id)
        assertEquals(MORTY_NAME, result?.name)
        assertEquals(STATUS_ALIVE, result?.status)
        assertEquals(SPECIES_HUMAN, result?.species)
        
        coVerify { apiService.getCharacterDetail(MORTY_ID) }
        coVerify { characterDao.getCharacterById(MORTY_ID) }
    }

    @Test
    fun `getCharacterDetail returns cached character on generic Exception`() = runTest {
        coEvery { apiService.getCharacterDetail(SUMMER_ID) } throws RuntimeException("Test exception")
        coEvery { characterDao.getCharacterById(SUMMER_ID) } returns summerEntity
        
        val result = repository.getCharacterDetail(SUMMER_ID)
        
        assertNotNull(result)
        assertEquals(SUMMER_ID, result?.id)
        assertEquals(SUMMER_NAME, result?.name)
        assertEquals(GENDER_FEMALE, result?.gender)
        assertEquals(SPECIES_HUMAN, result?.species)
        
        coVerify { apiService.getCharacterDetail(SUMMER_ID) }
        coVerify { characterDao.getCharacterById(SUMMER_ID) }
    }

    @Test
    fun `getCharacterDetail returns null if not found anywhere`() = runTest {
        coEvery { apiService.getCharacterDetail(UNKNOWN_ID) } throws java.io.IOException()
        coEvery { characterDao.getCharacterById(UNKNOWN_ID) } returns null
        
        val result = repository.getCharacterDetail(UNKNOWN_ID)
        
        assertNull(result)
        
        coVerify { apiService.getCharacterDetail(UNKNOWN_ID) }
        coVerify { characterDao.getCharacterById(UNKNOWN_ID) }
    }
}