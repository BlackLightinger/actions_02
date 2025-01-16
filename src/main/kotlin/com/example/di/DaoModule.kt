package com.example.di

import com.example.datasource.local.dao.*
import org.koin.dsl.module

val daoModule = module {
    factory<ArticlesDao> { object : ArticlesDao() {} }
    factory<MetadataDao> { object : MetadataDao() {} }
    factory<RevisionsDao> { object : RevisionsDao() {} }
    factory<SearchesDao> { object : SearchesDao() {} }
    factory<SearchResultsDao> { object : SearchResultsDao() {} }
    factory<TagsDao> { object : TagsDao() {} }
    factory<TextTagsDao> { object : TextTagsDao() {} }
    factory<UsersDao> { object : UsersDao() {} }
}