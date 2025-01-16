package com.example.route.mapper

import com.example.route.model.*
import com.example.domain.model.*
import kotlinx.datetime.LocalDate

fun ArticleRouteModel.toDomain(): Article = Article(
    title = title,
    body = body,
    authorId = authorId,
    createdAt = LocalDate.parse(createdAt),
    updatedAt = LocalDate.parse(updatedAt)
)

fun CommentRouteModel.toDomain(): Comment = Comment(
    textId = textId,
    userId = userId,
    body = body,
    createdAt = LocalDate.parse(createdAt)
)

fun MetadataRouteModel.toDomain(): Metadata = Metadata(
    textId = textId,
    key = key,
    value = value
)

fun RevisionRouteModel.toDomain(): Revision = Revision(
    textId = textId,
    authorId = authorId,
    createdAt = LocalDate.parse(createdAt)
)

fun SearchRouteModel.toDomain(): Search = Search(
    userId = userId,
    query = query,
    createdAt = LocalDate.parse(createdAt)
)

fun SearchResultRouteModel.toDomain(): SearchResult = SearchResult(
    searchId = searchId,
    textId = textId,
    relevance = relevance
)

fun TagRouteModel.toDomain(): Tag = Tag(
    name = name
)

fun TextTagRouteModel.toDomain(): TextTag = TextTag(
    textId = textId,
    tagId = tagId
)

fun UserRouteModel.toDomain(): User = User(
    username = username,
    email = email,
    passwordHash = passwordHash,
    role = role,
    registrationDate = LocalDate.parse(registrationDate),
    lastLogin = LocalDate.parse(lastLogin)
)
