package com.example.datasource.local.dao

import com.example.datasource.local.entity.RevisionEntity
import com.example.datasource.local.table.RevisionsTable
import com.example.datasource.local.table.ArticlesTable
import com.example.datasource.local.table.UsersTable
import com.example.domain.model.Article
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
class RevisionsDaoTest {

    private val dbConnect = Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        user = "root",
        driver = "org.h2.Driver",
        password = ""
    )

    private lateinit var revisionsDao: RevisionsDao
    private lateinit var articlesDao: ArticlesDao
    private lateinit var usersDao: UsersDao

    private lateinit var userId: UUID
    private lateinit var textId: UUID

    @BeforeTest
    fun setUp() {
        transaction {
            // Создаем таблицы
            SchemaUtils.create(UsersTable, ArticlesTable, RevisionsTable)

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
            textId = ArticlesTable.insertAndGetId {
                it[title] = article.title
                it[body] = article.body
                it[authorId] = article.authorId
                it[createdAt] = article.createdAt
                it[updatedAt] = article.updatedAt
            }.value
        }

        revisionsDao = object : RevisionsDao() {}
        articlesDao = object : ArticlesDao() {}
        usersDao = object : UsersDao() {}
    }

    @AfterTest
    fun tearDown() {
        transaction {
            // Удаляем записи
            RevisionsTable.deleteAll()
            ArticlesTable.deleteAll()
            UsersTable.deleteAll()
        }
    }

    @Test
    fun testInsertRevision() = runBlocking {
        val revision = RevisionEntity(
            textId = textId,
            authorId = userId,
            createdAt = LocalDate(2023, 1, 1)
        )

        val revisionId = transaction { revisionsDao.insert(revision) }
        val insertedRevision = transaction { revisionsDao.read(revisionId) }

        assertNotNull(insertedRevision)
        assertEquals(revision.textId, insertedRevision?.textId)
        assertEquals(revision.authorId, insertedRevision?.authorId)
    }

    @Test
    fun testBatchInsertRevisions() = runBlocking {
        val revisionsList = listOf(
            RevisionEntity(
                textId = textId,
                authorId = userId,
                createdAt = LocalDate(2023, 1, 1)
            ),
            RevisionEntity(
                textId = textId,
                authorId = userId,
                createdAt = LocalDate(2023, 1, 2)
            )
        )

        val revisionIds = transaction { revisionsDao.batchInsert(revisionsList) }
        assertEquals(2, revisionIds.size)

        val insertedRevisions = revisionIds.map { transaction { revisionsDao.read(it) } }

        assertTrue(insertedRevisions.all { it != null })
        assertEquals(LocalDate(2023, 1, 1), insertedRevisions[0]?.createdAt)
        assertEquals(LocalDate(2023, 1, 2), insertedRevisions[1]?.createdAt)
    }

    @Test
    fun testReadAllRevisions() = runBlocking {
        val revision1 = RevisionEntity(
            textId = textId,
            authorId = userId,
            createdAt = LocalDate(2023, 1, 1)
        )
        val revision2 = RevisionEntity(
            textId = textId,
            authorId = userId,
            createdAt = LocalDate(2023, 1, 2)
        )

        transaction {
            revisionsDao.insert(revision1)
            revisionsDao.insert(revision2)
        }

        val allRevisions = transaction { revisionsDao.readAll() }

        assertTrue(allRevisions.size >= 2)
        assertTrue(allRevisions.any { it.createdAt == LocalDate(2023, 1, 1) })
        assertTrue(allRevisions.any { it.createdAt == LocalDate(2023, 1, 2) })
    }
}
