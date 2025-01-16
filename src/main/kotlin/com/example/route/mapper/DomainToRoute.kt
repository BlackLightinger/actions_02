package com.example.route.mapper

import com.example.route.model.*
import com.example.domain.model.*

fun Article.toRoute(): ArticleRouteModel = ArticleRouteModel(
    id = id,
    title = title,
    body = body,
    authorId = authorId,
    createdAt = createdAt.toString(),
    updatedAt = updatedAt.toString()
)

fun Comment.toRoute(): CommentRouteModel = CommentRouteModel(
    id = id,
    textId = textId,
    userId = userId,
    body = body,
    createdAt = createdAt.toString()
)

fun Metadata.toRoute(): MetadataRouteModel = MetadataRouteModel(
    id = id,
    textId = textId,
    key = key,
    value = value
)

fun Revision.toRoute(): RevisionRouteModel = RevisionRouteModel(
    id = id,
    textId = textId,
    authorId = authorId,
    createdAt = createdAt.toString()
)

fun Search.toRoute(): SearchRouteModel = SearchRouteModel(
    id = id,
    userId = userId,
    query = query,
    createdAt = createdAt.toString()
)

fun SearchResult.toRoute(): SearchResultRouteModel = SearchResultRouteModel(
    id = id,
    searchId = searchId,
    textId = textId,
    relevance = relevance
)

fun Tag.toRoute(): TagRouteModel = TagRouteModel(
    id = id,
    name = name
)

fun TextTag.toRoute(): TextTagRouteModel = TextTagRouteModel(
    textId = textId,
    tagId = tagId
)

fun User.toRoute(): UserRouteModel = UserRouteModel(
    id = id,
    username = username,
    email = email,
    passwordHash = passwordHash,
    role = role,
    registrationDate = registrationDate.toString(),
    lastLogin = lastLogin.toString()
)
