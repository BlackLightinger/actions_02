package com.example.data.repository

import com.example.data.datasource.local.service.IRevisionsDaoService
import com.example.domain.model.Revision
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


class RevisionBuilder {
    private var id: UUID? = null
    private var textId: UUID = UUID.randomUUID()
    private var authorId: UUID = UUID.randomUUID()
    private var createdAt: LocalDate = LocalDate.parse("2024-01-01")

    fun withId(id: UUID?): RevisionBuilder {
        this.id = id
        return this
    }

    fun withTextId(textId: UUID): RevisionBuilder {
        this.textId = textId
        return this
    }

    fun withAuthorId(authorId: UUID): RevisionBuilder {
        this.authorId = authorId
        return this
    }

    fun withCreatedAt(createdAt: LocalDate): RevisionBuilder {
        this.createdAt = createdAt
        return this
    }

    fun build(): Revision {
        return Revision(id, textId, authorId, createdAt)
    }
}

@Execution(ExecutionMode.CONCURRENT)
class RevisionRepositoryTest {
    private val revisionsDaoService = mockk<IRevisionsDaoService>()
    private lateinit var revisionRepository: RevisionRepository

    @BeforeEach
    fun setUp() {
        revisionRepository = RevisionRepository(revisionsDaoService)
    }

    @Test
    fun `insert should return UUID when revision is successfully inserted`() = runBlocking {
        val revision = RevisionBuilder().build()
        val generatedUUID = UUID.randomUUID()

        coEvery { revisionsDaoService.insert(any()) } returns generatedUUID

        val result = revisionRepository.insert(revision)

        assertEquals(generatedUUID, result)
        coVerify { revisionsDaoService.insert(revision) }
    }

    @Test
    fun `insert should throw exception when dao fails`() = runBlocking {
        val revision = RevisionBuilder().build()

        coEvery { revisionsDaoService.insert(any()) } throws RuntimeException("Database error")

        assertFailsWith<RuntimeException>("Database error") {
            revisionRepository.insert(revision)
        }
        coVerify { revisionsDaoService.insert(revision) }
    }

    @Test
    fun `insertAll should return list of UUIDs when revisions are successfully inserted`() = runBlocking {
        val revisions = listOf(
            RevisionBuilder().withTextId(UUID.randomUUID()).build(),
            RevisionBuilder().withTextId(UUID.randomUUID()).build()
        )
        val generatedUUIDs = listOf(UUID.randomUUID(), UUID.randomUUID())

        coEvery { revisionsDaoService.insertAll(any()) } returns generatedUUIDs

        val result = revisionRepository.insertAll(revisions)

        assertEquals(generatedUUIDs, result)
        coVerify { revisionsDaoService.insertAll(revisions) }
    }

    @Test
    fun `read by UUID should return revision when found`() = runBlocking {
        val revision = RevisionBuilder().withId(UUID.randomUUID()).build()
        val revisionId = revision.id!!

        coEvery { revisionsDaoService.read(revisionId) } returns revision

        val result = revisionRepository.read(revisionId)

        assertNotNull(result)
        assertEquals(revision, result)
        coVerify { revisionsDaoService.read(revisionId) }
    }

    @Test
    fun `read by UUID should return null when revision is not found`() = runBlocking {
        val revisionId = UUID.randomUUID()

        coEvery { revisionsDaoService.read(revisionId) } returns null

        val result = revisionRepository.read(revisionId)

        assertNull(result)
        coVerify { revisionsDaoService.read(revisionId) }
    }

    @Test
    fun `readAll should return list of revisions`() = runBlocking {
        val revisions = listOf(
            RevisionBuilder().withTextId(UUID.randomUUID()).build(),
            RevisionBuilder().withTextId(UUID.randomUUID()).build()
        )

        coEvery { revisionsDaoService.readAll() } returns revisions

        val result = revisionRepository.readAll()

        assertEquals(revisions, result)
        coVerify { revisionsDaoService.readAll() }
    }

    @Test
    fun `delete should call dao to delete revision by UUID`() = runBlocking {
        val revisionId = UUID.randomUUID()

        coEvery { revisionsDaoService.delete(revisionId) } just Runs

        revisionRepository.delete(revisionId)

        coVerify { revisionsDaoService.delete(revisionId) }
    }

    @Test
    fun `deleteAll should call dao to delete all revisions`() = runBlocking {
        coEvery { revisionsDaoService.deleteAll() } just Runs

        revisionRepository.deleteAll()

        coVerify { revisionsDaoService.deleteAll() }
    }
}
