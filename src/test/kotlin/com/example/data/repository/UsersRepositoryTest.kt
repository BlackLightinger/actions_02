package com.example.data.repository

import com.example.data.datasource.local.service.IUsersDaoService
import com.example.domain.model.User
import io.mockk.*
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;


class UserBuilder {
    private var id: UUID? = null
    private var username: String = "default_username"
    private var email: String = "default@example.com"
    private var passwordHash: String = "hashed_password"
    private var role: String = "user"
    private var registrationDate: LocalDate = LocalDate.parse("2023-01-01")
    private var lastLogin: LocalDate = LocalDate.parse("2023-01-02")

    fun withId(id: UUID?): UserBuilder {
        this.id = id
        return this
    }

    fun withUsername(username: String): UserBuilder {
        this.username = username
        return this
    }

    fun withEmail(email: String): UserBuilder {
        this.email = email
        return this
    }

    fun withPasswordHash(passwordHash: String): UserBuilder {
        this.passwordHash = passwordHash
        return this
    }

    fun withRole(role: String): UserBuilder {
        this.role = role
        return this
    }

    fun withRegistrationDate(date: LocalDate): UserBuilder {
        this.registrationDate = date
        return this
    }

    fun withLastLogin(date: LocalDate): UserBuilder {
        this.lastLogin = date
        return this
    }

    fun build(): User {
        return User(id, username, email, passwordHash, role, registrationDate, lastLogin)
    }
}

@Execution(ExecutionMode.CONCURRENT)
class UserRepositoryTest {
    private val usersDaoService = mockk<IUsersDaoService>()
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setUp() {
        userRepository = UserRepository(usersDaoService)
    }

    @Test
    fun `insert should return UUID when User is successfully inserted`() = runBlocking {
        val user = UserBuilder().build()
        val generatedUUID = UUID.randomUUID()

        coEvery { usersDaoService.insert(any()) } returns generatedUUID

        val result = userRepository.insert(user)

        assertEquals(generatedUUID, result)
        coVerify { usersDaoService.insert(user) }
    }

    @Test
    fun `read by UUID should return User when found`() = runBlocking {
        val user = UserBuilder().build()
        val userId = UUID.randomUUID()

        coEvery { usersDaoService.read(userId) } returns user

        val result = userRepository.read(userId)

        assertNotNull(result)
        assertEquals(user, result)
        coVerify { usersDaoService.read(userId) }
    }

    @Test
    fun `read by email should return User when found`() = runBlocking {
        val user = UserBuilder().withEmail("test@example.com").build()
        val email = "test@example.com"

        coEvery { usersDaoService.read(email) } returns user

        val result = userRepository.read(email)

        assertNotNull(result)
        assertEquals(user, result)
        coVerify { usersDaoService.read(email) }
    }

    @Test
    fun `read by email and password should return User when credentials match`() = runBlocking {
        val user = UserBuilder().withEmail("test@example.com").withPasswordHash("hashed_password").build()
        val email = "test@example.com"
        val password = "hashed_password"

        coEvery { usersDaoService.read(email, password) } returns user

        val result = userRepository.read(email, password)

        assertNotNull(result)
        assertEquals(user, result)
        coVerify { usersDaoService.read(email, password) }
    }

    @Test
    fun `readAll should return list of Users`() = runBlocking {
        val users = listOf(
            UserBuilder().build(),
            UserBuilder().withUsername("another_user").withEmail("another@example.com").build()
        )

        coEvery { usersDaoService.readAll() } returns users

        val result = userRepository.readAll()

        assertEquals(users, result)
        coVerify { usersDaoService.readAll() }
    }

    @Test
    fun `delete should call dao to delete User by UUID`() = runBlocking {
        val userId = UUID.randomUUID()

        coEvery { usersDaoService.delete(userId) } just Runs

        userRepository.delete(userId)

        coVerify { usersDaoService.delete(userId) }
    }

    @Test
    fun `deleteAll should call dao to delete all Users`() = runBlocking {
        coEvery { usersDaoService.deleteAll() } just Runs

        userRepository.deleteAll()

        coVerify { usersDaoService.deleteAll() }
    }
}
