package edu.ucne.registrotecnico.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.registrotecnico.data.local.database.TecnicoDb
import edu.ucne.registrotecnico.presentation.tecnico.TecnicoListScreen
import edu.ucne.registrotecnico.presentation.tecnico.TecnicoScreen
import edu.ucne.registrotecnico.presentation.ticket.TicketListScreen
import edu.ucne.registrotecnico.presentation.ticket.TicketScreen

@Composable
fun TikcetNavHost(
    navHostController: NavHostController,
    ticketDb: TecnicoDb
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.TicketsList
    ) {
        composable<Screen.TicketsList> {
            TicketListScreen(
                ticketList = emptyList(),
                onEdit = { ticket ->
                    ticket.ticketId?.let { id ->
                        navHostController.navigate(Screen.Ticket(id))
                    }
                },
                onDelete = { }
            )
        }

        composable<Screen.Ticket> { backStack ->
            val ticketId = backStack.toRoute<Screen.Ticket>().ticketId
            TicketScreen(ticketId, ticketDb)
        }
    }
}

