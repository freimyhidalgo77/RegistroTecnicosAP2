package edu.ucne.registrotecnico.presentation.ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnico.data.local.entities.MensajeEntity
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnico.data.local.entities.TicketEntity
import edu.ucne.registrotecnico.data.repository.TecnicosRepository
import edu.ucne.registrotecnico.data.repository.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val ticketRepository: TicketRepository,

): ViewModel() {

    private val _uiState = MutableStateFlow(TicketEntity(0,"",0,"","","",0))
    val uiState = _uiState.asStateFlow()

    fun saveTicket(ticket: TicketEntity) {
        viewModelScope.launch {
            ticketRepository.save(ticket)
        }
    }

    fun deleteTicket(ticket: TicketEntity) {
        viewModelScope.launch {
            ticketRepository.delete(_uiState.value)
        }
    }

    fun find(id: Int) {
        viewModelScope.launch {
            val ticket = ticketRepository.find(id)
            if (ticket != null) {
                _uiState.value = ticket
            }
        }
    }



}