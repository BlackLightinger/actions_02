package com.example.data.repository

import com.example.data.datasource.local.service.ISearchesDaoService
import com.example.domain.model.Search
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlinx.datetime.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertFailsWith
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;


class SearchBuilder {
    private var id: UUID? = null
    private var userId: UUID = UUID.randomUUID()
    private var query: String = "default query"
    private var createdAt: LocalDate = LocalDate.parse("2024-01-01")

    fun withId(id: UUID?): SearchBuilder {
        this.id = id
        return this
    }

    fun withUserId(userId: UUID): SearchBuilder {
        this.userId = userId
        return this
    }

    fun withQuery(query: String): SearchBuilder {
        this.query = query
        return this
    }

    fun withCreatedAt(createdAt: LocalDate): SearchBuilder {
        this.createdAt = createdAt
        return this
    }

    fun build(): Search {
        return Search(id, userId, query, createdAt)
    }
}

@Execution(ExecutionMode.CONCURRENT)
class SearchRepositoryTest {
    private val searchesDaoService = mockk<ISearchesDaoService>()
    private lateinit var searchRepository: SearchRepository

    @BeforeEach
    fun setUp() {
        searchRepository = SearchRepository(searchesDaoService)
    }

    @Test
    fun `insert should return UUID when search is successfully inserted`() = runBlocking {
        val search = SearchBuilder().build()
        val generatedUUID = UUID.randomUUID()

        coEvery { searchesDaoService.insert(any()) } returns generatedUUID

        val result = searchRepository.insert(search)

        assertEquals(generatedUUID, result)
        coVerify { searchesDaoService.insert(search) }
    }

    @Test
    fun `insert should throw exception when dao fails`() = runBlocking {
        val search = SearchBuilder().build()

        coEvery { searchesDaoService.insert(any()) } throws RuntimeException("Database error")

        assertFailsWith<RuntimeException>("Database error") {
            searchRepository.insert(search)
        }
        coVerify { searchesDaoService.insert(search) }
    }

    @Test
    fun `insertAll should return list of UUIDs when searches are successfully inserted`() = runBlocking {
        val searches = listOf(
            SearchBuilder().withUserId(UUID.randomUUID()).build(),
            SearchBuilder().withUserId(UUID.randomUUID()).build()
        )
        val generatedUUIDs = listOf(UUID.randomUUID(), UUID.randomUUID())

        coEvery { searchesDaoService.insertAll(any()) } returns generatedUUIDs

        val result = searchRepository.insertAll(searches)

        assertEquals(generatedUUIDs, result)
        coVerify { searchesDaoService.insertAll(searches) }
    }

    @Test
    fun `read by UUID should return search when found`() = runBlocking {
        val search = SearchBuilder().withId(UUID.randomUUID()).build()
        val searchId = search.id!!

        coEvery { searchesDaoService.read(searchId) } returns search

        val result = searchRepository.read(searchId)

        assertNotNull(result)
        assertEquals(search, result)
        coVerify { searchesDaoService.read(searchId) }
    }

    @Test
    fun `read by UUID should return null when search is not found`() = runBlocking {
        val searchId = UUID.randomUUID()

        coEvery { searchesDaoService.read(searchId) } returns null

        val result = searchRepository.read(searchId)

        assertNull(result)
        coVerify { searchesDaoService.read(searchId) }
    }

    @Test
    fun `readAll should return list of searches`() = runBlocking {
        val searches = listOf(
            SearchBuilder().withUserId(UUID.randomUUID()).build(),
            SearchBuilder().withUserId(UUID.randomUUID()).build()
        )

        coEvery { searchesDaoService.readAll() } returns searches

        val result = searchRepository.readAll()

        assertEquals(searches, result)
        coVerify { searchesDaoService.readAll() }
    }

    @Test
    fun `delete should call dao to delete search by UUID`() = runBlocking {
        val searchId = UUID.randomUUID()

        coEvery { searchesDaoService.delete(searchId) } just Runs

        searchRepository.delete(searchId)

        coVerify { searchesDaoService.delete(searchId) }
    }

    @Test
    fun `deleteAll should call dao to delete all searches`() = runBlocking {
        coEvery { searchesDaoService.deleteAll() } just Runs

        searchRepository.deleteAll()

        coVerify { searchesDaoService.deleteAll() }
    }
}
