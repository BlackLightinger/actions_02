package com.example.datasource.local.mapper

import com.example.datasource.local.entity.*
import com.example.datasource.local.table.*
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toArticleEntity(): ArticleEntity = ArticleEntity(
    id = this@toArticleEntity[ArticlesTable.id].value,
    title = this@toArticleEntity[ArticlesTable.title],
    body = this@toArticleEntity[ArticlesTable.body],
    authorId = this@toArticleEntity[ArticlesTable.authorId].value,
    createdAt = this@toArticleEntity[ArticlesTable.createdAt],
    updatedAt = this@toArticleEntity[ArticlesTable.updatedAt],
    )

fun ResultRow.toCommentEntity(): CommentEntity = CommentEntity(
    id = this@toCommentEntity[CommentsTable.id].value,
    textId = this@toCommentEntity[CommentsTable.textId].value,
    userId = this@toCommentEntity[CommentsTable.userId].value,
    body = this@toCommentEntity[CommentsTable.body],
    createdAt = this@toCommentEntity[CommentsTable.createdAt],
    )


fun ResultRow.toMetadataEntity(): MetadataEntity = MetadataEntity(
    id = this@toMetadataEntity[MetadataTable.id].value,
    textId = this@toMetadataEntity[MetadataTable.textId].value,
    key = this@toMetadataEntity[MetadataTable.key],
    value = this@toMetadataEntity[MetadataTable.value],
)

fun ResultRow.toRevisionEntity(): RevisionEntity = RevisionEntity(
    id = this@toRevisionEntity[RevisionsTable.id].value,
    textId = this@toRevisionEntity[RevisionsTable.textId].value,
    authorId = this@toRevisionEntity[RevisionsTable.authorId].value,
    createdAt = this@toRevisionEntity[RevisionsTable.createdAt],
)

fun ResultRow.toSearchEntity(): SearchEntity = SearchEntity(
    id = this@toSearchEntity[SearchesTable.id].value,
    userId = this@toSearchEntity[SearchesTable.userId].value,
    query = this@toSearchEntity[SearchesTable.query],
    createdAt = this@toSearchEntity[SearchesTable.createdAt],
)

fun ResultRow.toSearchResultEntity(): SearchResultEntity = SearchResultEntity(
    id = this@toSearchResultEntity[SearchResultsTable.id].value,
    searchId = this@toSearchResultEntity[SearchResultsTable.searchId].value,
    textId = this@toSearchResultEntity[SearchResultsTable.textId].value,
    relevance = this@toSearchResultEntity[SearchResultsTable.relevance],
)

fun ResultRow.toTagEntity(): TagEntity = TagEntity(
    id = this@toTagEntity[TagsTable.id].value,
    name = this@toTagEntity[TagsTable.name],
)

fun ResultRow.toTextTagEntity(): TextTagEntity = TextTagEntity(
    textId = this@toTextTagEntity[TextTagsTable.textId].value,
    tagId = this@toTextTagEntity[TextTagsTable.tagId].value,
)

fun ResultRow.toUserEntity(): UserEntity = UserEntity(
    id = this@toUserEntity[UsersTable.id].value,
    username = this@toUserEntity[UsersTable.username],
    email = this@toUserEntity[UsersTable.email],
    passwordHash = this@toUserEntity[UsersTable.passwordHash],
    role = this@toUserEntity[UsersTable.role],
    registrationDate = this@toUserEntity[UsersTable.registrationDate],
    lastLogin = this@toUserEntity[UsersTable.lastLogin],
)