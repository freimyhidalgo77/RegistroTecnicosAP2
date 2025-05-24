package edu.ucne.registrotecnico.presentation.Mensaje

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnico.data.local.entities.MensajeEntity
import edu.ucne.registrotecnico.data.repository.MensajeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class MensajeUiState(
    val remitente: String = "",
    val descripcion: String = "",
    val fecha: Date? = null,
    val rol: String = "Operador"
)

@HiltViewModel
class MensajeViewModel @Inject constructor(
    private val mensajeRepository: MensajeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MensajeUiState())
    val uiState: StateFlow<MensajeUiState> = _uiState.asStateFlow()

    private val _mensajes = MutableStateFlow<List<MensajeEntity>>(emptyList())
    val mensajes: StateFlow<List<MensajeEntity>> = _mensajes.asStateFlow()

    init {
        getMensajes()
    }

    private fun getMensajes() {
        viewModelScope.launch {
            mensajeRepository.getAll().collect {
                _mensajes.value = it
            }
        }
    }

    fun onDescripcionChange(nuevo: String) {
        _uiState.value = _uiState.value.copy(descripcion = nuevo, fecha = Date())
    }

    fun onRemitenteChange(nuevo: String) {
        _uiState.value = _uiState.value.copy(remitente = nuevo)
    }

    fun save() {
        val mensaje = MensajeEntity(
            remitente = _uiState.value.remitente,
            descripcion = _uiState.value.descripcion,
            fecha = _uiState.value.fecha ?: Date(),
            rol = _uiState.value.rol
        )
        viewModelScope.launch {
            mensajeRepository.save(mensaje)
            _uiState.value = MensajeUiState()
        }
    }

    fun deleteMensaje(mensaje: MensajeEntity) {
        viewModelScope.launch {
            mensajeRepository.delete(mensaje)
            getMensajes()
        }
    }

    fun onRolChange(newRol: String) {
        _uiState.update { it.copy(rol = newRol) }
    }



}
