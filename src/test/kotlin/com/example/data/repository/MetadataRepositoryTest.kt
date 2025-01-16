package com.example.data.repository

import com.example.data.datasource.local.service.IMetadataDaoService
import com.example.domain.model.Metadata
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


class MetadataBuilder {
    private var id: UUID? = null
    private var textId: UUID = UUID.randomUUID()
    private var key: String = "defaultKey"
    private var value: String = "defaultValue"

    fun withId(id: UUID?): MetadataBuilder {
        this.id = id
        return this
    }

    fun withTextId(textId: UUID): MetadataBuilder {
        this.textId = textId
        return this
    }

    fun withKey(key: String): MetadataBuilder {
        this.key = key
        return this
    }

    fun withValue(value: String): MetadataBuilder {
        this.value = value
        return this
    }

    fun build(): Metadata {
        return Metadata(id, textId, key, value)
    }
}

@Execution(ExecutionMode.CONCURRENT)
class MetadataRepositoryTest {
    private val metadataDaoService = mockk<IMetadataDaoService>()
    private lateinit var metadataRepository: MetadataRepository

    @BeforeEach
    fun setUp() {
        metadataRepository = MetadataRepository(metadataDaoService)
    }

    @Test
    fun `insert should return UUID when metadata is successfully inserted`() = runBlocking {
        val metadata = MetadataBuilder().withKey("TestKey").withValue("TestValue").build()
        val generatedUUID = UUID.randomUUID()

        coEvery { metadataDaoService.insert(any()) } returns generatedUUID

        val result = metadataRepository.insert(metadata)

        assertEquals(generatedUUID, result)
        coVerify { metadataDaoService.insert(metadata) }
    }

    @Test
    fun `insert should throw exception when dao fails`() = runBlocking {
        val metadata = MetadataBuilder().withKey("TestKey").withValue("TestValue").build()

        coEvery { metadataDaoService.insert(any()) } throws RuntimeException("Database error")

        assertFailsWith<RuntimeException>("Database error") {
            metadataRepository.insert(metadata)
        }
        coVerify { metadataDaoService.insert(metadata) }
    }

    @Test
    fun `insertAll should return list of UUIDs when metadata entries are successfully inserted`() = runBlocking {
        val metadataList = listOf(
            MetadataBuilder().withKey("Key1").withValue("Value1").build(),
            MetadataBuilder().withKey("Key2").withValue("Value2").build()
        )
        val generatedUUIDs = listOf(UUID.randomUUID(), UUID.randomUUID())

        coEvery { metadataDaoService.insertAll(any()) } returns generatedUUIDs

        val result = metadataRepository.insertAll(metadataList)

        assertEquals(generatedUUIDs, result)
        coVerify { metadataDaoService.insertAll(metadataList) }
    }

    @Test
    fun `read by UUID should return metadata when found`() = runBlocking {
        val metadata = MetadataBuilder().withId(UUID.randomUUID()).withKey("ExistingKey").withValue("ExistingValue").build()
        val metadataId = metadata.id!!

        coEvery { metadataDaoService.read(metadataId) } returns metadata

        val result = metadataRepository.read(metadataId)

        assertNotNull(result)
        assertEquals(metadata, result)
        coVerify { metadataDaoService.read(metadataId) }
    }

    @Test
    fun `read by UUID should return null when metadata is not found`() = runBlocking {
        val metadataId = UUID.randomUUID()

        coEvery { metadataDaoService.read(metadataId) } returns null

        val result = metadataRepository.read(metadataId)

        assertNull(result)
        coVerify { metadataDaoService.read(metadataId) }
    }

    @Test
    fun `readAll should return list of metadata`() = runBlocking {
        val metadataList = listOf(
            MetadataBuilder().withKey("Key1").withValue("Value1").build(),
            MetadataBuilder().withKey("Key2").withValue("Value2").build()
        )

        coEvery { metadataDaoService.readAll() } returns metadataList

        val result = metadataRepository.readAll()

        assertEquals(metadataList, result)
        coVerify { metadataDaoService.readAll() }
    }

    @Test
    fun `delete should call dao to delete metadata by UUID`() = runBlocking {
        val metadataId = UUID.randomUUID()

        coEvery { metadataDaoService.delete(metadataId) } just Runs

        metadataRepository.delete(metadataId)

        coVerify { metadataDaoService.delete(metadataId) }
    }

    @Test
    fun `deleteAll should call dao to delete all metadata`() = runBlocking {
        coEvery { metadataDaoService.deleteAll() } just Runs

        metadataRepository.deleteAll()

        coVerify { metadataDaoService.deleteAll() }
    }
}
