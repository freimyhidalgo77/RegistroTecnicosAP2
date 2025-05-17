package edu.ucne.registrotecnico.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    object Home : Screen

    @Serializable
    object TecnicoList : Screen

    @Serializable
    data class Tecnico(val tecnicoId: Int) : Screen

    @Serializable
    object TicketsList : Screen

    @Serializable
    data class Ticket(val ticketId: Int) : Screen

    @Serializable
    data class TecnicoEdit(val tecnicoId: Int) : Screen
}