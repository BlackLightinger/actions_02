package com.example.datasource.local.dao

import com.example.datasource.local.entity.MetadataEntity
import com.example.datasource.local.table.MetadataTable
import com.example.datasource.local.table.ArticlesTable
import com.example.datasource.local.table.UsersTable
import com.example.domain.model.Article
import com.example.domain.model.Metadata
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
class MetadataDaoTest {

    private val dbConnect = Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        user = "root",
        driver = "org.h2.Driver",
        password = ""
    )

    private lateinit var metadataDao: MetadataDao
    private lateinit var articlesDao: ArticlesDao
    private lateinit var userDao: UsersDao

    private lateinit var userId: UUID
    private lateinit var articleId: UUID

    @BeforeTest
    fun setUp() {
        transaction {
            // Создаем таблицы
            SchemaUtils.create(UsersTable, ArticlesTable, MetadataTable)

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

        metadataDao = object : MetadataDao() {}
        articlesDao = object : ArticlesDao() {}
        userDao = object : UsersDao() {}
    }

    @AfterTest
    fun tearDown() {
        transaction {
            // Удаляем записи
            MetadataTable.deleteAll()
            ArticlesTable.deleteAll()
            UsersTable.deleteAll()
        }
    }

    @Test
    fun testInsertMetadata() = runBlocking {
        val metadata = MetadataEntity(
            textId = articleId,
            key = "author",
            value = "testUser"
        )

        val metadataId = transaction { metadataDao.insert(metadata) }
        val insertedMetadata = transaction { metadataDao.read(metadataId) }

        assertNotNull(insertedMetadata)
        assertEquals(metadata.key, insertedMetadata?.key)
        assertEquals(metadata.value, insertedMetadata?.value)
        assertEquals(metadata.textId, insertedMetadata?.textId)
    }

    @Test
    fun testBatchInsertMetadata() = runBlocking {
        val metadataList = listOf(
            MetadataEntity(
                textId = articleId,
                key = "author",
                value = "testUser"
            ),
            MetadataEntity(
                textId = articleId,
                key = "publisher",
                value = "testPublisher"
            )
        )

        val metadataIds = transaction { metadataDao.batchInsert(metadataList) }
        assertEquals(2, metadataIds.size)

        val insertedMetadataList = metadataIds.map { transaction { metadataDao.read(it) } }

        assertTrue(insertedMetadataList.all { it != null })
        assertEquals("author", insertedMetadataList[0]?.key)
        assertEquals("publisher", insertedMetadataList[1]?.key)
    }

    @Test
    fun testReadAllMetadata() = runBlocking {
        val metadata1 = MetadataEntity(
            textId = articleId,
            key = "author",
            value = "testUser"
        )
        val metadata2 = MetadataEntity(
            textId = articleId,
            key = "publisher",
            value = "testPublisher"
        )

        transaction {
            metadataDao.insert(metadata1)
            metadataDao.insert(metadata2)
        }

        val allMetadata = transaction { metadataDao.readAll() }

        assertTrue(allMetadata.size >= 2)
        assertTrue(allMetadata.any { it.key == "author" })
        assertTrue(allMetadata.any { it.key == "publisher" })
    }
}
