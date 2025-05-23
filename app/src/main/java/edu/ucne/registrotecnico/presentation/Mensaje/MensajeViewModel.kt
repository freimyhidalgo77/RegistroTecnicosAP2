package edu.ucne.registrotecnico.presentation.Mensaje

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnico.data.local.entities.MensajeEntity
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnico.data.local.entities.TicketEntity
import edu.ucne.registrotecnico.data.repository.MensajeRepository
import edu.ucne.registrotecnico.data.repository.TecnicosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject



data class MensajeUiState(
    val descripcion: String = "",
    val fecha: Date? = null
)

@HiltViewModel
class MensajeViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(MensajeUiState())
    val uiState: StateFlow<MensajeUiState> = _uiState

    private val _mensajes = MutableStateFlow<List<MensajeEntity>>(emptyList())
    val mensajes: StateFlow<List<MensajeEntity>> = _mensajes

    fun onDescripcionChange(nuevo: String) {
        _uiState.value = _uiState.value.copy(descripcion = nuevo, fecha = Date())
    }

    fun save(mensaje: MensajeEntity) {
        _mensajes.value = _mensajes.value + mensaje
        _uiState.value = MensajeUiState()
    }
}
