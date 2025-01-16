package com.example.di

import com.example.data.repository.*
import com.example.domain.repository.*
import org.koin.dsl.module

val repositoryModule = module {
    factory<IArticlesRepository> { ArticleRepository(get()) }
    factory<IMetadataRepository> { MetadataRepository(get()) }
    factory<IRevisionsRepository> { RevisionRepository(get()) }
    factory<ISearchesRepository> { SearchRepository(get()) }
    factory<ISearchResultsRepository> { SearchResultRepository(get()) }
    factory<ITagRepository> { TagRepository(get()) }
    factory<ITextTagsRepository> { TextTagsRepository(get()) }
    factory<IUsersRepository> { UserRepository(get()) }
}