package com.example.data.repository

import com.example.data.datasource.local.service.ITagsDaoService
import com.example.domain.model.Tag
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


class TagBuilder {
    private var id: UUID? = null
    private var name: String = "default_name"

    fun withId(id: UUID?): TagBuilder {
        this.id = id
        return this
    }

    fun withName(name: String): TagBuilder {
        this.name = name
        return this
    }

    fun build(): Tag {
        return Tag(id, name)
    }
}

@Execution(ExecutionMode.CONCURRENT)
class TagRepositoryTest {
    private val tagsDaoService = mockk<ITagsDaoService>()
    private lateinit var tagRepository: TagRepository

    @BeforeEach
    fun setUp() {
        tagRepository = TagRepository(tagsDaoService)
    }

    @Test
    fun `insert should return UUID when tag is successfully inserted`() = runBlocking {
        val tag = TagBuilder().build()
        val generatedUUID = UUID.randomUUID()

        coEvery { tagsDaoService.insert(any()) } returns generatedUUID

        val result = tagRepository.insert(tag)

        assertEquals(generatedUUID, result)
        coVerify { tagsDaoService.insert(tag) }
    }

    @Test
    fun `insertAll should return list of UUIDs when tags are successfully inserted`() = runBlocking {
        val tags = listOf(
            TagBuilder().withName("tag1").build(),
            TagBuilder().withName("tag2").build()
        )
        val generatedUUIDs = listOf(UUID.randomUUID(), UUID.randomUUID())

        coEvery { tagsDaoService.insertAll(any()) } returns generatedUUIDs

        val result = tagRepository.insertAll(tags)

        assertEquals(generatedUUIDs, result)
        coVerify { tagsDaoService.insertAll(tags) }
    }

    @Test
    fun `read by UUID should return tag when found`() = runBlocking {
        val tag = TagBuilder().withId(UUID.randomUUID()).withName("tag_name").build()
        val tagId = tag.id!!

        coEvery { tagsDaoService.read(tagId) } returns tag

        val result = tagRepository.read(tagId)

        assertNotNull(result)
        assertEquals(tag, result)
        coVerify { tagsDaoService.read(tagId) }
    }

    @Test
    fun `read by UUID should return null when tag is not found`() = runBlocking {
        val tagId = UUID.randomUUID()

        coEvery { tagsDaoService.read(tagId) } returns null

        val result = tagRepository.read(tagId)

        assertNull(result)
        coVerify { tagsDaoService.read(tagId) }
    }

    @Test
    fun `readAll should return list of tags`() = runBlocking {
        val tags = listOf(
            TagBuilder().withName("tag1").build(),
            TagBuilder().withName("tag2").build()
        )

        coEvery { tagsDaoService.readAll() } returns tags

        val result = tagRepository.readAll()

        assertEquals(tags, result)
        coVerify { tagsDaoService.readAll() }
    }

    @Test
    fun `update should call dao to update tag by UUID`() = runBlocking {
        val tagId = UUID.randomUUID()
        val tag = TagBuilder().withId(tagId).withName("updated_name").build()

        coEvery { tagsDaoService.update(tagId, tag) } just Runs

        tagRepository.update(tagId, tag)

        coVerify { tagsDaoService.update(tagId, tag) }
    }

    @Test
    fun `delete should call dao to delete tag by UUID`() = runBlocking {
        val tagId = UUID.randomUUID()

        coEvery { tagsDaoService.delete(tagId) } just Runs

        tagRepository.delete(tagId)

        coVerify { tagsDaoService.delete(tagId) }
    }

    @Test
    fun `deleteAll should call dao to delete all tags`() = runBlocking {
        coEvery { tagsDaoService.deleteAll() } just Runs

        tagRepository.deleteAll()

        coVerify { tagsDaoService.deleteAll() }
    }
}
