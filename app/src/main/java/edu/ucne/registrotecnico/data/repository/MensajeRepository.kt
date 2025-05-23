package edu.ucne.registrotecnico.data.repository

import edu.ucne.registrotecnico.data.local.dao.MensajeDao
import edu.ucne.registrotecnico.data.local.entities.MensajeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MensajeRepository @Inject constructor(private var dao: MensajeDao) {


    suspend fun save(mensaje: MensajeEntity) = dao.save(mensaje)

    suspend fun find(id: Int): MensajeEntity? = dao.find(id)

    suspend fun delete(mensaje: MensajeEntity) = dao.delete(mensaje)

    fun getAll(): Flow<List<MensajeEntity>> = dao.getAll()

}

