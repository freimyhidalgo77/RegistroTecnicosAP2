import edu.ucne.registrotecnico.data.remote.retenciones.RetencionManajerApi


import edu.ucne.registrotecnico.data.remote.dto.RetencionDTO
import javax.inject.Inject

class DataSource @Inject constructor(
    private val retencionesManagerApi: RetencionManajerApi
){
    suspend fun getRetenciones() = retencionesManagerApi.getRetenciones()

    suspend fun getRetencion(id: Int) = retencionesManagerApi.getRetencion(id)

    suspend fun saveRetencion(retencionDto: RetencionDTO) = retencionesManagerApi.saveRetencion(retencionDto)

    suspend fun actualizarRetencion(id: Int, retencionDto: RetencionDTO) = retencionesManagerApi.actualizarRetencion(id, retencionDto)

    suspend fun deleteRetencion(id: Int) = retencionesManagerApi.deleteRetencion(id)

}