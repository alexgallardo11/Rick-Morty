package com.example.rickmorty.di

import com.example.rickmorty.domain.usecase.GetCharactersPagedUseCase
import com.example.rickmorty.domain.usecase.GetCharactersPagedUseCaseImpl
import com.example.rickmorty.domain.usecase.GetCharacterDetailUseCase
import com.example.rickmorty.domain.usecase.GetCharacterDetailUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {
    @Binds
    abstract fun bindGetCharactersPagedUseCase(
        impl: GetCharactersPagedUseCaseImpl
    ): GetCharactersPagedUseCase

    @Binds
    abstract fun bindGetCharacterDetailUseCase(
        impl: GetCharacterDetailUseCaseImpl
    ): GetCharacterDetailUseCase
}
