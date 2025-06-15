package edu.ucne.registrotecnico.presentation.retencion

sealed interface RetencionesEvent {

    data class RetecionChange(val retencionId: Int):RetencionesEvent
    data class DescripcionChange(val descripcion: String): RetencionesEvent
    data class MontoChange(val monto: Double): RetencionesEvent
    data object GetRetencion:RetencionesEvent
    data object PostRRetencion: RetencionesEvent
    data object ChangeErrorMessageDescripcion: RetencionesEvent
    data object ChangeErrorMessageMonto: RetencionesEvent
    data object NuevaRetencion: RetencionesEvent
    object ResetSuccessMessage : RetencionesEvent

}