package edu.ucne.registrotecnico.data.repository

import DataSource
import android.util.Log
import edu.ucne.registrotecnico.data.remote.Resource
import edu.ucne.registrotecnico.data.remote.dto.RetencionDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class RetencionRepository @Inject constructor(
    private val dataSource: DataSource
){
    fun getRetenciones(): Flow<Resource<List<RetencionDTO>>> = flow {
        try{
            emit(Resource.Loading())
            val retenciones = dataSource.getRetenciones()
            emit(Resource.Success(retenciones))
        }catch (e: HttpException){
            val errorMessage = e.response()?.errorBody()?.string() ?: e.message()
            Log.e("RetencionRepository", "HttpException: $errorMessage")
            emit(Resource.Error("Error de conexion $errorMessage"))
        }catch (e: Exception){
            Log.e("RetencionRepository", "Exception: ${e.message}")
            emit(Resource.Error("Error: ${e.message}"))

        }
    }
    suspend fun update(id: Int, retencionDto: RetencionDTO) =
        dataSource.actualizarRetencion(id, retencionDto)

    suspend fun find(id: Int) = dataSource.getRetencion(id)

    suspend fun save(retencionDto: RetencionDTO) = dataSource.saveRetencion(retencionDto)

    suspend fun delete(id: Int) = dataSource.deleteRetencion(id)
}