package com.example.datasource.local.mapper

import com.example.datasource.local.entity.*
import com.example.domain.model.*

fun Article.toEntity(): ArticleEntity = ArticleEntity(
    title = title,
    body = body,
    authorId = authorId,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun Comment.toEntity(): CommentEntity = CommentEntity(
    textId = textId,
    userId = userId,
    body = body,
    createdAt = createdAt,
)


fun Metadata.toEntity(): MetadataEntity = MetadataEntity(
    textId = textId,
    key = key,
    value = value,
)

fun Revision.toEntity(): RevisionEntity = RevisionEntity(
    textId = textId,
    authorId = authorId,
    createdAt = createdAt,
)

fun Search.toEntity(): SearchEntity = SearchEntity(
    userId = userId,
    query = query,
    createdAt = createdAt,
)

fun SearchResult.toEntity(): SearchResultEntity = SearchResultEntity(
    searchId = searchId,
    textId = textId,
    relevance = relevance,
)

fun Tag.toEntity(): TagEntity = TagEntity(
    name = name,
)

fun TextTag.toEntity(): TextTagEntity = TextTagEntity(
    textId = textId,
    tagId = tagId,
)

fun User.toEntity(): UserEntity = UserEntity(
    username = username,
    email = email,
    passwordHash = passwordHash,
    role = role,
    registrationDate = registrationDate,
    lastLogin = lastLogin,
)
