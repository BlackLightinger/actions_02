package com.example.data.repository

import com.example.data.datasource.local.service.ISearchResultsDaoService
import com.example.domain.model.SearchResult
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertFailsWith
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;


class SearchResultBuilder {
    private var id: UUID? = null
    private var searchId: UUID = UUID.randomUUID()
    private var textId: UUID = UUID.randomUUID()
    private var relevance: Double = 0.5

    fun withId(id: UUID?): SearchResultBuilder {
        this.id = id
        return this
    }

    fun withSearchId(searchId: UUID): SearchResultBuilder {
        this.searchId = searchId
        return this
    }

    fun withTextId(textId: UUID): SearchResultBuilder {
        this.textId = textId
        return this
    }

    fun withRelevance(relevance: Double): SearchResultBuilder {
        this.relevance = relevance
        return this
    }

    fun build(): SearchResult {
        return SearchResult(id, searchId, textId, relevance)
    }
}

@Execution(ExecutionMode.CONCURRENT)
class SearchResultRepositoryTest {
    private val searchResultsDaoService = mockk<ISearchResultsDaoService>()
    private lateinit var searchResultRepository: SearchResultRepository

    @BeforeEach
    fun setUp() {
        searchResultRepository = SearchResultRepository(searchResultsDaoService)
    }

    @Test
    fun `insert should return UUID when search result is successfully inserted`() = runBlocking {
        val searchResult = SearchResultBuilder().build()
        val generatedUUID = UUID.randomUUID()

        coEvery { searchResultsDaoService.insert(any()) } returns generatedUUID

        val result = searchResultRepository.insert(searchResult)

        assertEquals(generatedUUID, result)
        coVerify { searchResultsDaoService.insert(searchResult) }
    }

    @Test
    fun `insertAll should return list of UUIDs when search results are successfully inserted`() = runBlocking {
        val searchResults = listOf(
            SearchResultBuilder().build(),
            SearchResultBuilder().withRelevance(0.8).build()
        )
        val generatedUUIDs = listOf(UUID.randomUUID(), UUID.randomUUID())

        coEvery { searchResultsDaoService.insertAll(any()) } returns generatedUUIDs

        val result = searchResultRepository.insertAll(searchResults)

        assertEquals(generatedUUIDs, result)
        coVerify { searchResultsDaoService.insertAll(searchResults) }
    }

    @Test
    fun `read by UUID should return search result when found`() = runBlocking {
        val searchResult = SearchResultBuilder().withId(UUID.randomUUID()).build()
        val searchResultId = searchResult.id!!

        coEvery { searchResultsDaoService.read(searchResultId) } returns searchResult

        val result = searchResultRepository.read(searchResultId)

        assertNotNull(result)
        assertEquals(searchResult, result)
        coVerify { searchResultsDaoService.read(searchResultId) }
    }

    @Test
    fun `read by UUID should return null when search result is not found`() = runBlocking {
        val searchResultId = UUID.randomUUID()

        coEvery { searchResultsDaoService.read(searchResultId) } returns null

        val result = searchResultRepository.read(searchResultId)

        assertNull(result)
        coVerify { searchResultsDaoService.read(searchResultId) }
    }

    @Test
    fun `readAll should return list of search results`() = runBlocking {
        val searchResults = listOf(
            SearchResultBuilder().build(),
            SearchResultBuilder().withRelevance(0.8).build()
        )

        coEvery { searchResultsDaoService.readAll() } returns searchResults

        val result = searchResultRepository.readAll()

        assertEquals(searchResults, result)
        coVerify { searchResultsDaoService.readAll() }
    }

    @Test
    fun `update should call dao to update search result by UUID`() = runBlocking {
        val searchResultId = UUID.randomUUID()
        val searchResult = SearchResultBuilder().withId(searchResultId).build()

        coEvery { searchResultsDaoService.update(searchResultId, searchResult) } just Runs

        searchResultRepository.update(searchResultId, searchResult)

        coVerify { searchResultsDaoService.update(searchResultId, searchResult) }
    }

    @Test
    fun `delete should call dao to delete search result by UUID`() = runBlocking {
        val searchResultId = UUID.randomUUID()

        coEvery { searchResultsDaoService.delete(searchResultId) } just Runs

        searchResultRepository.delete(searchResultId)

        coVerify { searchResultsDaoService.delete(searchResultId) }
    }

    @Test
    fun `deleteAll should call dao to delete all search results`() = runBlocking {
        coEvery { searchResultsDaoService.deleteAll() } just Runs

        searchResultRepository.deleteAll()

        coVerify { searchResultsDaoService.deleteAll() }
    }
}
