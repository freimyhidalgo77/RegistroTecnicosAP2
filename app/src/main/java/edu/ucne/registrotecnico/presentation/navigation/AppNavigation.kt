package edu.ucne.registrotecnico.presentation.navigation

import HomeScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import edu.ucne.registrotecnico.data.local.database.TecnicoDb
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnico.presentation.prioridad.PrioridadDeleteScreen
import edu.ucne.registrotecnico.presentation.prioridad.PrioridadEditScreen
import edu.ucne.registrotecnico.presentation.prioridad.PrioridadListScreen
import edu.ucne.registrotecnico.presentation.prioridad.PrioridadScreen
import edu.ucne.registrotecnico.presentation.prioridad.savePrioridad
import edu.ucne.registrotecnico.presentation.tecnico.TecnicoDeleteScreen
import edu.ucne.registrotecnico.presentation.tecnico.TecnicoListScreen
import edu.ucne.registrotecnico.presentation.tecnico.TecnicoEditScreen
import edu.ucne.registrotecnico.presentation.tecnico.TecnicoScreen
import edu.ucne.registrotecnico.presentation.ticket.TicketDeleteScreen
import edu.ucne.registrotecnico.presentation.ticket.TicketEditScreen
import edu.ucne.registrotecnico.presentation.ticket.TicketListScreen
import edu.ucne.registrotecnico.presentation.ticket.TicketScreen
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(tecnicoDb: TecnicoDb) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "home") {

        composable("home") {
            HomeScreen(navController)
        }

        composable("tecnicos") {
            val tecnicoList = tecnicoDb.tecnicoDao().getAll().collectAsState(initial = emptyList())
            val coroutineScope = rememberCoroutineScope()

            TecnicoListScreen(
                tecnicoList = tecnicoList.value,
                tecnicoCreate = {
                    navController.navigate("crear_tecnico")
                },
                onEdit = { tecnico ->
                    navController.navigate(Screen.TecnicoEdit(tecnico.tecnicoId!!))
                },
                onDelete = { tecnico ->
                    coroutineScope.launch {
                        navController.navigate(Screen.TecnicoDelete(tecnico.tecnicoId!!))
                    }
                }
            )
        }

        composable("crear_tecnico") {
            TecnicoScreen(
                tecnicoId = null,
                tecnicoDb = tecnicoDb
            )
        }

        composable<Screen.TecnicoEdit> {
            val args = it.toRoute<Screen.TecnicoEdit>()
            TecnicoEditScreen(
                tecnicoId = args.tecnicoId,
                tecnicoDb = tecnicoDb,
                navController = navController,

            )
        }

        composable<Screen.TecnicoDelete> {
            val args = it.toRoute<Screen.TecnicoDelete>()
            TecnicoDeleteScreen (
                tecnicoId = args.tecnicoId,
                tecnicoDb = tecnicoDb,
                navController = navController
            )
        }

        composable("tickets") {
            val ticketList = tecnicoDb.ticketDao().getAll().collectAsState(initial = emptyList())
            val coroutineScope = rememberCoroutineScope()

            TicketListScreen(
                ticketList = ticketList.value,
                ticketCreate = {
                    navController.navigate("crear_ticket")
                },
                onEdit = { ticket ->
                    navController.navigate(Screen.TicketEdit(ticket.ticketId!!))
                },
                onDelete = { ticket ->
                    coroutineScope.launch {
                        navController.navigate(Screen.TicketDelete(ticket.ticketId!!))
                    }
                }
            )
        }

        composable("crear_ticket") {
            TicketScreen(
                ticketId = null,
                ticketDb = tecnicoDb
            )
        }

        composable<Screen.TicketEdit> {
            val args = it.toRoute<Screen.TicketEdit>()
            TicketEditScreen(
                ticketId = args.ticketId,
                ticketDb = tecnicoDb,
                navController = navController,

                )
        }

        composable<Screen.TicketDelete> {
            val args = it.toRoute<Screen.TicketDelete>()
            TicketDeleteScreen(
                ticketId = args.ticketId,
                ticketDb = tecnicoDb,
                navController = navController,

                )
        }

        composable("prioridades") {
            val prioridadesList = tecnicoDb.prioridadDao().getAll().collectAsState(initial = emptyList())
            val coroutineScope = rememberCoroutineScope()

            PrioridadListScreen (
                prioridadList = prioridadesList.value,
                prioridadCreate = {
                    navController.navigate("crear_prioridad")
                },
                onEdit = { prioridad ->
                    navController.navigate(Screen.PrioridadEdit(prioridad.prioridadId!!))
                },
                onDelete = { prioridad ->
                    coroutineScope.launch {
                        navController.navigate(Screen.PrioridadDelete(prioridad.prioridadId!!))
                    }
                }
            )
        }

        composable("crear_prioridad") {
            PrioridadScreen (
                prioridadId = null,
                prioridadDb = tecnicoDb
            )
        }

        composable<Screen.PrioridadEdit> {
            val args = it.toRoute<Screen.PrioridadEdit>()
            PrioridadEditScreen(
                prioridadId = args.prioridadId,
                prioridadDb = tecnicoDb,
                navController = navController,

                )
        }

        composable<Screen.PrioridadDelete> {
            val args = it.toRoute<Screen.PrioridadDelete>()
            PrioridadDeleteScreen(
                prioridadId = args.prioridadId,
                prioridadDb = tecnicoDb,
                navController = navController,

                )
        }
    }
}
