package edu.ucne.registrotecnico.presentation.retencion

import edu.ucne.registrotecnico.data.remote.dto.RetencionDTO

data class RetencionUIState(
    val retencionId: Int = 0,
    val descripcion: String = "",
    val monto: Double = 0.0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val retenciones: List<RetencionDTO> = emptyList()
)
