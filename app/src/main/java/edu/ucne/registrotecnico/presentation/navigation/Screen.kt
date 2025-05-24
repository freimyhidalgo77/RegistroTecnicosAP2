package edu.ucne.registrotecnico.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    object Home : Screen


    @Serializable
    data class Tecnico(val tecnicoId: Int) : Screen

    @Serializable
    object TecnicoList : Screen

    @Serializable
    data class TecnicoEdit(val tecnicoId: Int) : Screen

    @Serializable
    data class TecnicoDelete(val tecnicoId: Int) : Screen



    @Serializable
    data class Ticket(val ticketId: Int) : Screen

    @Serializable
    object TicketsList : Screen

    @Serializable
    data class TicketEdit(val ticketId: Int) : Screen

    @Serializable
    data class TicketDelete(val ticketId: Int) : Screen


    @Serializable
    data class Prioridad(val prioridadId: Int) : Screen

    @Serializable
    object PrioridadList : Screen

    @Serializable
    data class PrioridadEdit(val prioridadId: Int) : Screen

    @Serializable
    data class PrioridadDelete(val prioridadId: Int) : Screen


    @Serializable
    data class Mensaje(val mensajeId: Int) : Screen

    @Serializable
    object MensajeList : Screen

    @Serializable
    data class sendMessage(val mensajeId: Int) : Screen


}