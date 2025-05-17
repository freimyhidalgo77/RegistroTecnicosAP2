package edu.ucne.registrotecnico.data.repository

import edu.ucne.registrotecnico.data.local.dao.PrioridadDao
import edu.ucne.registrotecnico.data.local.dao.TecnicoDao
import edu.ucne.registrotecnico.data.local.entities.PrioridadEntity
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import kotlinx.coroutines.flow.Flow

class PrioridadRepository(

    private val dao: PrioridadDao
) {
    suspend fun save(prioridadEntity: PrioridadEntity) = dao.save(prioridadEntity)

    suspend fun find(id: Int): PrioridadEntity? = dao.find(id)

    suspend fun delete(prioridad: PrioridadEntity) = dao.delete(prioridad)

    fun getAll(): Flow<List<PrioridadEntity>> = dao.getAll()
}
