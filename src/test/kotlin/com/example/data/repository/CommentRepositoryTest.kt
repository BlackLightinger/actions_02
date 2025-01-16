package com.example.data.repository

import com.example.data.datasource.local.service.ICommentsDaoService
import com.example.domain.model.Comment
import kotlinx.coroutines.runBlocking
import io.mockk.*
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertFailsWith
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;


class CommentBuilder {
    private var id: UUID? = null
    private var textId: UUID = UUID.randomUUID()
    private var userId: UUID = UUID.randomUUID()
    private var body: String = "Default Comment Body"
    private var createdAt: LocalDate = LocalDate(2025, 1, 1)

    fun withId(id: UUID?): CommentBuilder {
        this.id = id
        return this
    }

    fun withTextId(textId: UUID): CommentBuilder {
        this.textId = textId
        return this
    }

    fun withUserId(userId: UUID): CommentBuilder {
        this.userId = userId
        return this
    }

    fun withBody(body: String): CommentBuilder {
        this.body = body
        return this
    }

    fun withCreatedAt(createdAt: LocalDate): CommentBuilder {
        this.createdAt = createdAt
        return this
    }

    fun build(): Comment {
        return Comment(id, textId, userId, body, createdAt)
    }
}

@Execution(ExecutionMode.CONCURRENT)
class CommentsRepositoryTest {
    private val commentsDaoService = mockk<ICommentsDaoService>()
    private lateinit var commentsRepository: CommentRepository

    @BeforeEach
    fun setUp() {
        commentsRepository = CommentRepository(commentsDaoService)
    }

    @Test
    fun `insert should return UUID when comment is successfully inserted`() = runBlocking {
        val comment = CommentBuilder().withBody("Test Comment").build()
        val generatedUUID = UUID.randomUUID()

        coEvery { commentsDaoService.insert(any()) } returns generatedUUID

        val result = commentsRepository.insert(comment)

        assertEquals(generatedUUID, result)
        coVerify { commentsDaoService.insert(comment) }
    }

    @Test
    fun `insert should throw exception when dao fails`() = runBlocking {
        val comment = CommentBuilder().withBody("Test Comment").build()

        coEvery { commentsDaoService.insert(any()) } throws RuntimeException("Database error")

        assertFailsWith<RuntimeException>("Database error") {
            commentsRepository.insert(comment)
        }
        coVerify { commentsDaoService.insert(comment) }
    }

    @Test
    fun `insertAll should return list of UUIDs when comments are successfully inserted`() = runBlocking {
        val comments = listOf(
            CommentBuilder().withBody("Comment 1").build(),
            CommentBuilder().withBody("Comment 2").build()
        )
        val generatedUUIDs = listOf(UUID.randomUUID(), UUID.randomUUID())

        coEvery { commentsDaoService.insertAll(any()) } returns generatedUUIDs

        val result = commentsRepository.insertAll(comments)

        assertEquals(generatedUUIDs, result)
        coVerify { commentsDaoService.insertAll(comments) }
    }

    @Test
    fun `read by UUID should return comment when found`() = runBlocking {
        val comment = CommentBuilder().withId(UUID.randomUUID()).withBody("Existing Comment").build()
        val commentId = comment.id!!

        coEvery { commentsDaoService.read(commentId) } returns comment

        val result = commentsRepository.read(commentId)

        assertNotNull(result)
        assertEquals(comment, result)
        coVerify { commentsDaoService.read(commentId) }
    }

    @Test
    fun `read by UUID should return null when comment is not found`() = runBlocking {
        val commentId = UUID.randomUUID()

        coEvery { commentsDaoService.read(commentId) } returns null

        val result = commentsRepository.read(commentId)

        assertNull(result)
        coVerify { commentsDaoService.read(commentId) }
    }

    @Test
    fun `readAll should return list of comments`() = runBlocking {
        val comments = listOf(
            CommentBuilder().withBody("Comment 1").build(),
            CommentBuilder().withBody("Comment 2").build()
        )

        coEvery { commentsDaoService.readAll() } returns comments

        val result = commentsRepository.readAll()

        assertEquals(comments, result)
        coVerify { commentsDaoService.readAll() }
    }

    @Test
    fun `delete should call dao to delete comment by UUID`() = runBlocking {
        val commentId = UUID.randomUUID()

        coEvery { commentsDaoService.delete(commentId) } just Runs

        commentsRepository.delete(commentId)

        coVerify { commentsDaoService.delete(commentId) }
    }

    @Test
    fun `deleteAll should call dao to delete all comments`() = runBlocking {
        coEvery { commentsDaoService.deleteAll() } just Runs

        commentsRepository.deleteAll()

        coVerify { commentsDaoService.deleteAll() }
    }
}
