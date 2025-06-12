package edu.ucne.registrotecnico.data.remote

import edu.ucne.registrotecnico.data.remote.dto.ClienteDTO
import edu.ucne.registrotecnico.data.remote.dto.RetencionDTO
import edu.ucne.registrotecnico.data.remote.retenciones.RetencionManajerApi
import javax.inject.Inject

class DataSource @Inject constructor(
    private val clientesManagerApi: ClienteManajerApi
){
    suspend fun getClientes() = clientesManagerApi.getClientes()

    suspend fun getCliente(id: Int) = clientesManagerApi.getClientes(id)

    suspend fun saveCliente(clienteDTO: ClienteDTO) = clientesManagerApi.saveClientes(clienteDTO)

    suspend fun actualizarClientes(id: Int, clienteDto: ClienteDTO) = clientesManagerApi.actualizarCliente(id, clienteDto)

    suspend fun deleteCliente(id: Int) = clientesManagerApi.deleteClientes( id)

}