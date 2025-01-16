package com.example.datasource.local.dao

import com.example.datasource.local.entity.SearchEntity
import com.example.datasource.local.table.SearchesTable
import com.example.datasource.local.table.UsersTable
import com.example.domain.model.User
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import java.util.*
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;


@Execution(ExecutionMode.CONCURRENT)
class SearchesDaoTest {

    private val dbConnect = Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        user = "root",
        driver = "org.h2.Driver",
        password = ""
    )

    private lateinit var searchesDao: SearchesDao
    private lateinit var userId: UUID

    @Before
    fun setUp() {
        transaction {
            // Создаем таблицы
            SchemaUtils.create(UsersTable, SearchesTable)

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
        }

        searchesDao = object : SearchesDao() {}
    }

    @After
    fun tearDown() {
        transaction {
            // Удаляем записи
            SearchesTable.deleteAll()
            UsersTable.deleteAll()
        }
    }

    @Test
    fun testInsertSearch() = runBlocking {
        val search = SearchEntity(
            userId = userId,
            query = "test query",
            createdAt = LocalDate(2023, 1, 1)
        )

        val searchId = transaction { searchesDao.insert(search) }
        val insertedSearch = transaction { searchesDao.read(searchId) }

        assertNotNull(insertedSearch)
        assertEquals(search.userId, insertedSearch?.userId)
        assertEquals(search.query, insertedSearch?.query)
    }

    @Test
    fun testBatchInsertSearches() = runBlocking {
        val searchList = listOf(
            SearchEntity(
                userId = userId,
                query = "query1",
                createdAt = LocalDate(2023, 1, 1)
            ),
            SearchEntity(
                userId = userId,
                query = "query2",
                createdAt = LocalDate(2023, 1, 2)
            )
        )

        val searchIds = transaction { searchesDao.batchInsert(searchList) }
        assertEquals(2, searchIds.size)

        val insertedSearches = searchIds.map { transaction { searchesDao.read(it) } }

        assertTrue(insertedSearches.all { it != null })
        assertEquals("query1", insertedSearches[0]?.query)
        assertEquals("query2", insertedSearches[1]?.query)
    }

    @Test
    fun testReadAllSearches() = runBlocking {
        val search1 = SearchEntity(
            userId = userId,
            query = "query1",
            createdAt = LocalDate(2023, 1, 1)
        )
        val search2 = SearchEntity(
            userId = userId,
            query = "query2",
            createdAt = LocalDate(2023, 1, 2)
        )

        transaction {
            searchesDao.insert(search1)
            searchesDao.insert(search2)
        }

        val allSearches = transaction { searchesDao.readAll() }

        assertTrue(allSearches.size >= 2)
        assertTrue(allSearches.any { it.query == "query1" })
        assertTrue(allSearches.any { it.query == "query2" })
    }
}
