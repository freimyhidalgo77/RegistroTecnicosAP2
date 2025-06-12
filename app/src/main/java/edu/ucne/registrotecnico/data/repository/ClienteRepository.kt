package edu.ucne.registrotecnico.data.repository

import android.util.Log
import edu.ucne.registrotecnico.data.remote.DataSource
import edu.ucne.registrotecnico.data.remote.Resource
import edu.ucne.registrotecnico.data.remote.dto.ClienteDTO
import edu.ucne.registrotecnico.data.remote.dto.RetencionDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class ClienteRepository @Inject constructor(
    private val dataSource: DataSource
){
    fun getClientes(): Flow<Resource<List<ClienteDTO>>> = flow {
        try{
            emit(Resource.Loading())
            val clientes = dataSource.getClientes()
            emit(Resource.Success(clientes))
        }catch (e: HttpException){
            val errorMessage = e.response()?.errorBody()?.string() ?: e.message()
            Log.e("ClienteRepository", "HttpException: $errorMessage")
            emit(Resource.Error("Error de conexion $errorMessage"))
        }catch (e: Exception){
            Log.e("ClienteRepository", "Exception: ${e.message}")
            emit(Resource.Error("Error: ${e.message}"))

        }
    }
    suspend fun update(id: Int, clienteDto: ClienteDTO) =
        dataSource.actualizarClientes(id, clienteDto)

    suspend fun find(id: Int) = dataSource.getCliente(id)

    suspend fun save(clienteDto: ClienteDTO) = dataSource.saveCliente(clienteDto)

    suspend fun delete(id: Int) = dataSource.deleteCliente(id)
}