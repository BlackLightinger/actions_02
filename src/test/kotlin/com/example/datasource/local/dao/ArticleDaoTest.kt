package com.example.datasource.local.dao

import com.example.datasource.local.entity.ArticleEntity
import com.example.datasource.local.table.ArticlesTable
import com.example.datasource.local.table.UsersTable
import com.example.domain.model.User
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import kotlinx.datetime.LocalDate
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;


@Execution(ExecutionMode.CONCURRENT)
class ArticlesDaoTest {

    private val dbConnect = Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        user = "root",
        driver = "org.h2.Driver",
        password = ""
    )

    private lateinit var articlesDao: ArticlesDao
    private lateinit var testUserId: UUID

    @BeforeTest
    fun setUp() {
        transaction {
            // Создаём таблицы для статей и пользователей
            SchemaUtils.create(UsersTable, ArticlesTable)

            // Создаём тестового пользователя
            val testUser = User(
                username = "testuser",
                email = "test@example.com",
                passwordHash = "hashedpassword",
                role = "user",
                registrationDate = LocalDate(2025, 1, 15),
                lastLogin = LocalDate(2025, 1, 15)
            )
            testUserId = UsersTable.insertAndGetId {
                it[username] = testUser.username
                it[email] = testUser.email
                it[passwordHash] = testUser.passwordHash
                it[role] = testUser.role
                it[registrationDate] = testUser.registrationDate
                it[lastLogin] = testUser.lastLogin
            }.value
        }

        articlesDao = object : ArticlesDao() {}
    }

    @AfterTest
    fun tearDown() {
        transaction {
            // Удаляем все записи из таблиц
            ArticlesTable.deleteAll()
            UsersTable.deleteAll()
        }
    }

    @Test
    fun testInsert() = runBlocking {
        val article = ArticleEntity(
            title = "Test Article",
            body = "This is a test article.",
            authorId = testUserId,
            createdAt = LocalDate(2025, 1, 15),
            updatedAt = LocalDate(2025, 1, 15)
        )
        val articleId = transaction { articlesDao.insert(article) }

        val insertedArticle = transaction { articlesDao.read(articleId) }

        assertNotNull(insertedArticle)
        assertEquals(article.title, insertedArticle?.title)
        assertEquals(article.body, insertedArticle?.body)
        assertEquals(article.authorId, insertedArticle?.authorId)
        assertEquals(article.createdAt, insertedArticle?.createdAt)
        assertEquals(article.updatedAt, insertedArticle?.updatedAt)
    }

    @Test
    fun testBatchInsert() = runBlocking {
        val articles = listOf(
            ArticleEntity(
                title = "Article 1",
                body = "This is the first article.",
                authorId = testUserId,
                createdAt = LocalDate(2025, 1, 15),
                updatedAt = LocalDate(2025, 1, 15)
            ),
            ArticleEntity(
                title = "Article 2",
                body = "This is the second article.",
                authorId = testUserId,
                createdAt = LocalDate(2025, 1, 15),
                updatedAt = LocalDate(2025, 1, 15)
            )
        )

        val ids = transaction { articlesDao.batchInsert(articles) }

        assertEquals(2, ids.size)

        val insertedArticles = ids.map { transaction { articlesDao.read(it) } }

        assertTrue(insertedArticles.all { it != null })
        assertEquals("Article 1", insertedArticles[0]?.title)
        assertEquals("Article 2", insertedArticles[1]?.title)
    }

    @Test
    fun testReadAll() = runBlocking {
        val article1 = ArticleEntity(
            title = "Article 1",
            body = "This is the first article.",
            authorId = testUserId,
            createdAt = LocalDate(2025, 1, 15),
            updatedAt = LocalDate(2025, 1, 15)
        )
        val article2 = ArticleEntity(
            title = "Article 2",
            body = "This is the second article.",
            authorId = testUserId,
            createdAt = LocalDate(2025, 1, 15),
            updatedAt = LocalDate(2025, 1, 15)
        )

        transaction {
            articlesDao.insert(article1)
            articlesDao.insert(article2)
        }

        val allArticles = transaction { articlesDao.readAll() }

        assertTrue(allArticles.size >= 2)
        assertTrue(allArticles.any { it.title == "Article 1" })
        assertTrue(allArticles.any { it.title == "Article 2" })
    }
}
