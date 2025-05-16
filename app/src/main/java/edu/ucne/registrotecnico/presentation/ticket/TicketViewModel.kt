package edu.ucne.registrotecnico.presentation.ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnico.data.local.entities.TicketEntity
import edu.ucne.registrotecnico.data.repository.TecnicosRepository
import edu.ucne.registrotecnico.data.repository.TicketRepository
import kotlinx.coroutines.launch

class TicketViewModel(
    private val ticketRepository: TicketViewModel
): ViewModel() {
    fun saveTicket(ticket: TicketEntity) {
        viewModelScope.launch {
            ticketRepository.saveTicket(ticket)
        }
    }

}