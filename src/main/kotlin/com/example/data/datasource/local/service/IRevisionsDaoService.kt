package com.example.data.datasource.local.service

import com.example.domain.model.Revision
import java.util.*

interface IRevisionsDaoService {
    suspend fun insert(model: Revision): UUID
    suspend fun insertAll(modelList: List<Revision>): List<UUID>
    suspend fun read(id: UUID): Revision?
    suspend fun readAll(): List<Revision>
    suspend fun update(id: UUID, model: Revision)
    suspend fun updateAll(modelMap: Map<UUID, Revision>)
    suspend fun delete(id: UUID)
    suspend fun deleteAll()
}
