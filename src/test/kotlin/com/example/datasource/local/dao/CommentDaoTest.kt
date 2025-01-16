package com.example.datasource.local.dao

import com.example.datasource.local.entity.CommentEntity
import com.example.datasource.local.table.CommentsTable
import com.example.datasource.local.table.ArticlesTable
import com.example.datasource.local.table.UsersTable
import com.example.domain.model.Article
import com.example.domain.model.Comment
import com.example.domain.model.User
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import java.util.*
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;


@Execution(ExecutionMode.CONCURRENT)
class CommentsDaoTest {

    private val dbConnect = Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        user = "root",
        driver = "org.h2.Driver",
        password = ""
    )

    private lateinit var commentsDao: CommentsDao
    private lateinit var articlesDao: ArticlesDao
    private lateinit var userDao: UsersDao

    private lateinit var userId: UUID
    private lateinit var articleId: UUID

    @BeforeTest
    fun setUp() {
        transaction {
            // Создаем таблицы
            SchemaUtils.create(UsersTable, ArticlesTable, CommentsTable)

            // Создаем тестового пользователя
            val user = User(
                username = "testUser",
                email = "testuser@example.com",
                passwordHash = "hashedpassword",
                role = "user",
                registrationDate = LocalDate(2023, 1, 1),
                lastLogin = LocalDate(2023, 1, 1)
            )
            userId = UsersTable.insertAndGetId {
                it[username] = user.username
                it[email] = user.email
                it[passwordHash] = user.passwordHash
                it[role] = user.role
                it[registrationDate] = user.registrationDate
                it[lastLogin] = user.lastLogin
            }.value

            // Создаем тестовую статью
            val article = Article(
                title = "Test Article",
                body = "This is a test article",
                authorId = userId,
                createdAt = LocalDate(2023, 1, 1),
                updatedAt = LocalDate(2023, 1, 1)
            )
            articleId = ArticlesTable.insertAndGetId {
                it[title] = article.title
                it[body] = article.body
                it[authorId] = article.authorId
                it[createdAt] = article.createdAt
                it[updatedAt] = article.updatedAt
            }.value
        }

        commentsDao = object : CommentsDao() {}
        articlesDao = object : ArticlesDao() {}
        userDao = object : UsersDao() {}
    }

    @AfterTest
    fun tearDown() {
        transaction {
            // Удаляем записи
            CommentsTable.deleteAll()
            ArticlesTable.deleteAll()
            UsersTable.deleteAll()
        }
    }

    @Test
    fun testInsertComment() = runBlocking {
        val comment = CommentEntity(
            textId = articleId,
            userId = userId,
            body = "This is a test comment",
            createdAt = LocalDate(2023, 1, 1)
        )

        val commentId = transaction { commentsDao.insert(comment) }
        val insertedComment = transaction { commentsDao.read(commentId) }

        assertNotNull(insertedComment)
        assertEquals(comment.body, insertedComment?.body)
        assertEquals(comment.textId, insertedComment?.textId)
        assertEquals(comment.userId, insertedComment?.userId)
    }

    @Test
    fun testBatchInsertComments() = runBlocking {
        val commentList = listOf(
            CommentEntity(
                textId = articleId,
                userId = userId,
                body = "First comment",
                createdAt = LocalDate(2023, 1, 1)
            ),
            CommentEntity(
                textId = articleId,
                userId = userId,
                body = "Second comment",
                createdAt = LocalDate(2023, 1, 1)
            )
        )

        val commentIds = transaction { commentsDao.batchInsert(commentList) }
        assertEquals(2, commentIds.size)

        val insertedComments = commentIds.map { transaction { commentsDao.read(it) } }

        assertTrue(insertedComments.all { it != null })
        assertEquals("First comment", insertedComments[0]?.body)
        assertEquals("Second comment", insertedComments[1]?.body)
    }

    @Test
    fun testReadAllComments() = runBlocking {
        val comment1 = CommentEntity(
            textId = articleId,
            userId = userId,
            body = "First comment",
            createdAt = LocalDate(2023, 1, 1)
        )
        val comment2 = CommentEntity(
            textId = articleId,
            userId = userId,
            body = "Second comment",
            createdAt = LocalDate(2023, 1, 1)
        )

        transaction {
            commentsDao.insert(comment1)
            commentsDao.insert(comment2)
        }

        val allComments = transaction { commentsDao.readAll() }

        assertTrue(allComments.size >= 2)
        assertTrue(allComments.any { it.body == "First comment" })
        assertTrue(allComments.any { it.body == "Second comment" })
    }
}
