package com.example.datasource.local.mapper

import com.example.datasource.local.entity.*
import com.example.domain.model.*

fun ArticleEntity.toDomain(): Article = Article(
    title = title,
    body = body,
    authorId = authorId,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun CommentEntity.toDomain(): Comment = Comment(
    textId = textId,
    userId = userId,
    body = body,
    createdAt = createdAt
)

fun MetadataEntity.toDomain(): Metadata = Metadata(
    textId = textId,
    key = key,
    value = value
)

fun RevisionEntity.toDomain(): Revision = Revision(
    textId = textId,
    authorId = authorId,
    createdAt = createdAt
)

fun SearchEntity.toDomain(): Search = Search(
    userId = userId,
    query = query,
    createdAt = createdAt
)

fun SearchResultEntity.toDomain(): SearchResult = SearchResult(
    searchId = searchId,
    textId = textId,
    relevance = relevance
)

fun TagEntity.toDomain(): Tag = Tag(
    name = name
)

fun TextTagEntity.toDomain(): TextTag = TextTag(
    textId = textId,
    tagId = tagId
)

fun UserEntity.toDomain(): User = User(
    username = username,
    email = email,
    passwordHash = passwordHash,
    role = role,
    registrationDate = registrationDate,
    lastLogin = lastLogin
)

