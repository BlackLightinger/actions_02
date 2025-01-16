package com.example.datasource.local.dao

import com.example.datasource.local.entity.UserEntity
import com.example.datasource.local.table.UsersTable
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import java.util.*
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;


@Execution(ExecutionMode.CONCURRENT)
class UsersDaoTest {

    private val dbConnect = Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        user = "root",
        driver = "org.h2.Driver",
        password = ""
    )

    private lateinit var usersDao: UsersDao

    @BeforeTest
    fun setUp() {
        transaction {
            SchemaUtils.create(UsersTable)
        }

        usersDao = object : UsersDao() {}
    }

    @AfterTest
    fun tearDown() {
        transaction {
            // Удаляем записи
            UsersTable.deleteAll()
        }
    }

    @Test
    fun testInsertUser() = runBlocking {
        val user = UserEntity(
            username = "testUser",
            email = "testuser@example.com",
            passwordHash = "hashedpassword",
            role = "user",
            registrationDate = LocalDate(2023, 1, 1),
            lastLogin = LocalDate(2023, 1, 1)
        )

        val userId = transaction { usersDao.insert(user) }
        val insertedUser = transaction { usersDao.read(userId) }

        assertNotNull(insertedUser)
        assertEquals(user.username, insertedUser?.username)
        assertEquals(user.email, insertedUser?.email)
        assertEquals(user.passwordHash, insertedUser?.passwordHash)
        assertEquals(user.role, insertedUser?.role)
    }

    @Test
    fun testReadUserByEmail() = runBlocking {
        val user = UserEntity(
            username = "testUser",
            email = "testuser@example.com",
            passwordHash = "hashedpassword",
            role = "user",
            registrationDate = LocalDate(2023, 1, 1),
            lastLogin = LocalDate(2023, 1, 1)
        )

        transaction { usersDao.insert(user) }

        val fetchedUser = transaction { usersDao.read(user.email) }

        assertNotNull(fetchedUser)
        assertEquals(user.username, fetchedUser?.username)
        assertEquals(user.passwordHash, fetchedUser?.passwordHash)
    }

    @Test
    fun testUpdateUser() = runBlocking {
        val user = UserEntity(
            username = "testUser",
            email = "testuser@example.com",
            passwordHash = "hashedpassword",
            role = "user",
            registrationDate = LocalDate(2023, 1, 1),
            lastLogin = LocalDate(2023, 1, 1)
        )

        val userId = transaction { usersDao.insert(user) }

        val updatedUser = user.copy(
            username = "updatedUser",
            email = "updateduser@example.com"
        )

        transaction { usersDao.update(userId, updatedUser) }

        val fetchedUser = transaction { usersDao.read(userId) }

        assertNotNull(fetchedUser)
        assertEquals("updatedUser", fetchedUser?.username)
        assertEquals("updateduser@example.com", fetchedUser?.email)
    }

    @Test
    fun testDeleteUser() = runBlocking {
        val user = UserEntity(
            username = "testUser",
            email = "testuser@example.com",
            passwordHash = "hashedpassword",
            role = "user",
            registrationDate = LocalDate(2023, 1, 1),
            lastLogin = LocalDate(2023, 1, 1)
        )

        val userId = transaction { usersDao.insert(user) }

        transaction { usersDao.delete(userId) }

        val deletedUser = transaction { usersDao.read(userId) }

        assertNull(deletedUser)
    }

    @Test
    fun testBatchInsertUsers() = runBlocking {
        val users = listOf(
            UserEntity(
                username = "user1",
                email = "user1@example.com",
                passwordHash = "hashedpassword1",
                role = "user",
                registrationDate = LocalDate(2023, 1, 1),
                lastLogin = LocalDate(2023, 1, 1)
            ),
            UserEntity(
                username = "user2",
                email = "user2@example.com",
                passwordHash = "hashedpassword2",
                role = "admin",
                registrationDate = LocalDate(2023, 1, 2),
                lastLogin = LocalDate(2023, 1, 2)
            )
        )

        val userIds = transaction { usersDao.batchInsert(users) }

        assertEquals(2, userIds.size)

        val fetchedUsers = transaction { usersDao.readAll() }

        assertEquals(2, fetchedUsers.size)
        assertTrue(fetchedUsers.any { it.username == "user1" })
        assertTrue(fetchedUsers.any { it.username == "user2" })
    }
}
