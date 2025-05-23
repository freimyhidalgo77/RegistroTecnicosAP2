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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MensajeViewModel @Inject constructor(
    private val mensajeRepository: MensajeRepository,
    private val tecnicoRepository: TecnicosRepository

):ViewModel(){

    private val _uiState = MutableStateFlow(MensajeEntity(0, 0, "","", Date()))
    val uiState = _uiState.asStateFlow()


    fun save(mensaje:MensajeEntity) {
        viewModelScope.launch {
            mensajeRepository.save(mensaje)
        }
    }


    fun delete(mensaje: MensajeEntity) {
        viewModelScope.launch {
            mensajeRepository.delete(_uiState.value)
        }
    }

    fun find(id: Int) {
        viewModelScope.launch {
            val mensaje = mensajeRepository.find(id)
            if (mensaje != null) {
                _uiState.value = mensaje
            }
        }



        data class UiState(
            val ticket: TicketEntity = TicketEntity(0, "", 0, "", "", "", 0),
            val mensajes: List<MensajeEntity> = emptyList(),
            val nuevoMensaje: String = "",
            val mensajeError: String? = null,
            val mensajeExito: String? = null,
            val remitente: String = ""
        )

    }

    fun onDescripcionChange(newDescripcion: String) {
        _uiState.value = _uiState.value.copy(descripcion = newDescripcion)
    }




}