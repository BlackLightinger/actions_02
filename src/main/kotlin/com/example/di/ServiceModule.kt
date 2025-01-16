package com.example.di

import com.example.data.datasource.local.service.*
import com.example.datasource.local.dao.UsersDao
import com.example.datasource.local.service.*
import org.koin.dsl.module


val serviceModule = module {
    factory<IArticlesDaoService> { ArticlesDaoService(get()) }
    factory<IMetadataDaoService> { MetadataDaoService(get()) }
    factory<IRevisionsDaoService> { RevisionsDaoService(get()) }
    factory<ISearchesDaoService> { SearchesDaoService(get()) }
    factory<ISearchResultsDaoService> { SearchResultsDaoService(get()) }
    factory<ITagsDaoService> { TagsDaoService(get()) }
    factory<ITextTagsDaoService> { TextTagsDaoService(get()) }
    factory<IUsersDaoService> { UsersDaoService(get()) }
}