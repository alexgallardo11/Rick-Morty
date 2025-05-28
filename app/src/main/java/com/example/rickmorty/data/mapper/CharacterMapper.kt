package com.example.rickmorty.data.mapper

import com.example.rickmorty.data.model.*
import com.example.rickmorty.domain.model.Character
import com.example.rickmorty.domain.model.CharacterList
import com.example.rickmorty.domain.model.Info
import com.example.rickmorty.domain.model.Location
import com.example.rickmorty.domain.model.Origin

fun CharacterDto.toDomain(): Character = Character(
    id = id,
    name = name,
    status = status,
    species = species,
    type = type,
    gender = gender,
    origin = origin.toDomain(),
    location = location.toDomain(),
    image = image,
    episode = episode,
    url = url,
    created = created
)

fun OriginDto.toDomain(): Origin = Origin(
    name = name,
    url = url
)

fun LocationDto.toDomain(): Location = Location(
    name = name,
    url = url
)

fun InfoDto.toDomain(): Info = Info(
    count = count,
    pages = pages,
    next = next,
    prev = prev
)

fun CharacterResponseDto.toDomain(): CharacterList = CharacterList(
    info = info.toDomain(),
    results = results.map { it.toDomain() }
)
