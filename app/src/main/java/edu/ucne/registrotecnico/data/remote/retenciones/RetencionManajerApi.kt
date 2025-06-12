package edu.ucne.registrotecnico.data.remote.retenciones

import edu.ucne.registrotecnico.data.remote.dto.RetencionDTO
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RetencionManajerApi {

    @GET("api/Retenciones")
    suspend fun getRetenciones(): List<RetencionDTO>

    @GET("api/Retenciones/{id}")
    suspend fun getRetencion(@Path("id")id: Int): RetencionDTO

    @POST("api/Retenciones")
    suspend fun saveRetencion(@Body retencionDto: RetencionDTO?): RetencionDTO

    @PUT("api/Retenciones/{id}")
    suspend fun actualizarRetencion(
        @Path("id") retencionId: Int,
        @Body articulo: RetencionDTO
    ): RetencionDTO

    @DELETE("api/Retenciones/{id}")
    suspend fun deleteRetencion(@Path("id") id: Int): ResponseBody

}