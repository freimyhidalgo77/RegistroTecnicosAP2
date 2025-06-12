package edu.ucne.registrotecnico.data.repository

import edu.ucne.registrotecnico.data.local.dao.TecnicoDao
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TecnicoRepository  @Inject constructor(

    private val dao: TecnicoDao
) {
    suspend fun save(tecnico: TecnicoEntity) = dao.save(tecnico)

    suspend fun find(id: Int): TecnicoEntity? = dao.find(id)

    suspend fun delete(tecnico: TecnicoEntity) = dao.delete(tecnico)

    fun getAll(): Flow<List<TecnicoEntity>> = dao.getAll()
}