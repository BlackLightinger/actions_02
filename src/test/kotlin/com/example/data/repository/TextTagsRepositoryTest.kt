package com.example.data.repository

import com.example.data.datasource.local.service.ITextTagsDaoService
import com.example.domain.model.TextTag
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;


class TextTagBuilder {
    private var textId: UUID = UUID.randomUUID()
    private var tagId: UUID = UUID.randomUUID()

    fun withTextId(textId: UUID): TextTagBuilder {
        this.textId = textId
        return this
    }

    fun withTagId(tagId: UUID): TextTagBuilder {
        this.tagId = tagId
        return this
    }

    fun build(): TextTag {
        return TextTag(textId, tagId)
    }
}

@Execution(ExecutionMode.CONCURRENT)
class TextTagsRepositoryTest {
    private val textTagsDaoService = mockk<ITextTagsDaoService>()
    private lateinit var textTagsRepository: TextTagsRepository

    @BeforeEach
    fun setUp() {
        textTagsRepository = TextTagsRepository(textTagsDaoService)
    }

    @Test
    fun `insert should return UUID when TextTag is successfully inserted`() = runBlocking {
        val textTag = TextTagBuilder().build()
        val generatedUUID = UUID.randomUUID()

        coEvery { textTagsDaoService.insert(any()) } returns generatedUUID

        val result = textTagsRepository.insert(textTag)

        assertEquals(generatedUUID, result)
        coVerify { textTagsDaoService.insert(textTag) }
    }

    @Test
    fun `insertAll should return list of UUIDs when TextTags are successfully inserted`() = runBlocking {
        val textTags = listOf(
            TextTagBuilder().withTextId(UUID.randomUUID()).withTagId(UUID.randomUUID()).build(),
            TextTagBuilder().withTextId(UUID.randomUUID()).withTagId(UUID.randomUUID()).build()
        )
        val generatedUUIDs = listOf(UUID.randomUUID(), UUID.randomUUID())

        coEvery { textTagsDaoService.insertAll(any()) } returns generatedUUIDs

        val result = textTagsRepository.insertAll(textTags)

        assertEquals(generatedUUIDs, result)
        coVerify { textTagsDaoService.insertAll(textTags) }
    }

    @Test
    fun `read by UUID should return TextTag when found`() = runBlocking {
        val textTag = TextTagBuilder().build()
        val textTagId = UUID.randomUUID()

        coEvery { textTagsDaoService.read(textTagId) } returns textTag

        val result = textTagsRepository.read(textTagId)

        assertNotNull(result)
        assertEquals(textTag, result)
        coVerify { textTagsDaoService.read(textTagId) }
    }

    @Test
    fun `read by UUID should return null when TextTag is not found`() = runBlocking {
        val textTagId = UUID.randomUUID()

        coEvery { textTagsDaoService.read(textTagId) } returns null

        val result = textTagsRepository.read(textTagId)

        assertNull(result)
        coVerify { textTagsDaoService.read(textTagId) }
    }

    @Test
    fun `readAll should return list of TextTags`() = runBlocking {
        val textTags = listOf(
            TextTagBuilder().withTextId(UUID.randomUUID()).withTagId(UUID.randomUUID()).build(),
            TextTagBuilder().withTextId(UUID.randomUUID()).withTagId(UUID.randomUUID()).build()
        )

        coEvery { textTagsDaoService.readAll() } returns textTags

        val result = textTagsRepository.readAll()

        assertEquals(textTags, result)
        coVerify { textTagsDaoService.readAll() }
    }

    @Test
    fun `delete should call dao to delete TextTag by UUID`() = runBlocking {
        val textTagId = UUID.randomUUID()

        coEvery { textTagsDaoService.delete(textTagId) } just Runs

        textTagsRepository.delete(textTagId)

        coVerify { textTagsDaoService.delete(textTagId) }
    }

    @Test
    fun `deleteAll should call dao to delete all TextTags`() = runBlocking {
        coEvery { textTagsDaoService.deleteAll() } just Runs

        textTagsRepository.deleteAll()

        coVerify { textTagsDaoService.deleteAll() }
    }
}
