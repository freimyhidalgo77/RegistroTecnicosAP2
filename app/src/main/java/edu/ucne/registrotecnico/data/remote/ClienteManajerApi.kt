package edu.ucne.registrotecnico.data.remote

import edu.ucne.registrotecnico.data.remote.dto.ClienteDTO
import edu.ucne.registrotecnico.data.remote.dto.RetencionDTO
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ClienteManajerApi {

    @GET("api/Clientes")
    suspend fun getClientes(): List<ClienteDTO>

    @GET("api/Clientes/{id}")
    suspend fun getClientes(@Path("id")id: Int): ClienteDTO

    @POST("api/Clientes")
    suspend fun saveClientes(@Body clienteDTO: ClienteDTO): ClienteDTO

    @PUT("api/Clientes/{id}")
    suspend fun actualizarCliente(
        @Path("id") clienteId: Int,
        @Body cliente: ClienteDTO
    ): ClienteDTO

    @DELETE("api/Clientes/{id}")
    suspend fun deleteClientes(@Path("id") id: Int): ResponseBody


}