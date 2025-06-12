package edu.ucne.registrotecnico.data.remote

import edu.ucne.registrotecnico.data.remote.dto.ClienteDTO
import javax.inject.Inject

class ClienteDataSource @Inject constructor(
    private val clientesManagerApi: ClienteManajerApi
){
    suspend fun getClientes() = clientesManagerApi.getClientes()

    suspend fun getCliente(id: Int) = clientesManagerApi.getClientes(id)

    suspend fun saveCliente(clienteDTO: ClienteDTO) = clientesManagerApi.saveClientes(clienteDTO)

    suspend fun actualizarClientes(id: Int, clienteDto: ClienteDTO) = clientesManagerApi.actualizarCliente(id, clienteDto)

    suspend fun deleteCliente(id: Int) = clientesManagerApi.deleteClientes( id)

}