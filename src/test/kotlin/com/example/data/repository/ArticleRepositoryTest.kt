package com.example.data.repository

import com.example.data.datasource.local.service.IArticlesDaoService
import com.example.domain.model.Article
import io.mockk.*
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;


class ArticleBuilder {
    private var id: UUID? = null
    private var title: String = "Default Title"
    private var body: String = "Default Body"
    private var authorId: UUID = UUID.randomUUID()
    private var createdAt: LocalDate = LocalDate.parse("2023-01-01")
    private var updatedAt: LocalDate = LocalDate.parse("2023-01-01")

    fun withId(id: UUID?): ArticleBuilder {
        this.id = id
        return this
    }

    fun withTitle(title: String): ArticleBuilder {
        this.title = title
        return this
    }

    fun withBody(body: String): ArticleBuilder {
        this.body = body
        return this
    }

    fun withAuthorId(authorId: UUID): ArticleBuilder {
        this.authorId = authorId
        return this
    }

    fun withCreatedAt(createdAt: LocalDate): ArticleBuilder {
        this.createdAt = createdAt
        return this
    }

    fun withUpdatedAt(updatedAt: LocalDate): ArticleBuilder {
        this.updatedAt = updatedAt
        return this
    }

    fun build(): Article {
        return Article(id, title, body, authorId, createdAt, updatedAt)
    }
}

@Execution(ExecutionMode.CONCURRENT)
class ArticlesRepositoryTest {
    private val articlesDaoService = mockk<IArticlesDaoService>()
    private lateinit var articlesRepository: ArticleRepository

    @BeforeEach
    fun setUp() {
        articlesRepository = ArticleRepository(articlesDaoService)
    }

    @Test
    fun `insert should return UUID when article is successfully inserted`() = runBlocking {
        val article = ArticleBuilder().withTitle("Kotlin Guide").build()
        val generatedUUID = UUID.randomUUID()

        coEvery { articlesDaoService.insert(any()) } returns generatedUUID

        val result = articlesRepository.insert(article)

        assertEquals(generatedUUID, result)
        coVerify { articlesDaoService.insert(article) }
    }

    @Test
    fun `insert should throw exception when dao fails`() = runBlocking {
        val article = ArticleBuilder().withTitle("Kotlin Guide").build()

        coEvery { articlesDaoService.insert(any()) } throws RuntimeException("Database error")

        assertFailsWith<RuntimeException>("Database error") {
            articlesRepository.insert(article)
        }
        coVerify { articlesDaoService.insert(article) }
    }

    @Test
    fun `read by UUID should return article when found`() = runBlocking {
        val article = ArticleBuilder().withId(UUID.randomUUID()).build()

        coEvery { articlesDaoService.read(article.id!!) } returns article

        val result = articlesRepository.read(article.id!!)

        assertNotNull(result)
        assertEquals(article, result)
        coVerify { articlesDaoService.read(article.id!!) }
    }

    @Test
    fun `read by UUID should return null when article is not found`() = runBlocking {
        val articleId = UUID.randomUUID()

        coEvery { articlesDaoService.read(articleId) } returns null

        val result = articlesRepository.read(articleId)

        assertNull(result)
        coVerify { articlesDaoService.read(articleId) }
    }

    @Test
    fun `readAll should return list of articles`() = runBlocking {
        val articles = listOf(
            ArticleBuilder().withTitle("Kotlin Basics").build(),
            ArticleBuilder().withTitle("Advanced Kotlin").build()
        )

        coEvery { articlesDaoService.readAll() } returns articles

        val result = articlesRepository.readAll()

        assertEquals(articles, result)
        coVerify { articlesDaoService.readAll() }
    }

    @Test
    fun `readAll should return empty list when no articles are found`() = runBlocking {
        coEvery { articlesDaoService.readAll() } returns emptyList()

        val result = articlesRepository.readAll()

        assert(result.isEmpty())
        coVerify { articlesDaoService.readAll() }
    }
}
