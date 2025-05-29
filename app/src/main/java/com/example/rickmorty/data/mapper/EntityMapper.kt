package com.example.rickmorty.data.mapper

import com.example.rickmorty.data.local.entity.CharacterEntity
import com.example.rickmorty.data.local.entity.InfoEntity
import com.example.rickmorty.data.local.entity.LocationEntity
import com.example.rickmorty.data.local.entity.OriginEntity
import com.example.rickmorty.data.model.*
import com.example.rickmorty.domain.model.*


fun CharacterDto.toEntity(): CharacterEntity = CharacterEntity(
    id = id,
    name = name,
    status = status,
    species = species,
    type = type,
    gender = gender,
    origin = origin.toEntity(),
    location = location.toEntity(),
    image = image,
    episode = episode,
    url = url,
    created = created
)

fun OriginDto.toEntity(): OriginEntity = OriginEntity(
    name = name,
    url = url
)

fun LocationDto.toEntity(): LocationEntity = LocationEntity(
    name = name,
    url = url
)

fun InfoDto.toEntity(): InfoEntity = InfoEntity(
    count = count,
    pages = pages,
    next = next,
    prev = prev
)


fun CharacterEntity.toDomain(): Character = Character(
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

fun OriginEntity.toDomain(): Origin = Origin(
    name = name,
    url = url
)

fun LocationEntity.toDomain(): Location = Location(
    name = name,
    url = url
)

fun InfoEntity.toDomain(): Info = Info(
    count = count,
    pages = pages,
    next = next,
    prev = prev
)
