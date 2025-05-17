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
import edu.ucne.registrotecnico.presentation.prioridad.PrioridadListScreen
import edu.ucne.registrotecnico.presentation.prioridad.PrioridadScreen
import edu.ucne.registrotecnico.presentation.tecnico.TecnicoListScreen
import edu.ucne.registrotecnico.presentation.tecnico.TecnicoEditScreen
import edu.ucne.registrotecnico.presentation.tecnico.TecnicoScreen
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
                        tecnicoDb.tecnicoDao().delete(tecnico)
                    }
                }
            )
        }

        //Navegar a la pantalla de Tecnico
        composable("crear_tecnico") {
            TecnicoScreen(
                tecnicoId = null,
                tecnicoDb = tecnicoDb
            )
        }

        //Navegar a la pantalla de Ticket
        composable("crear_ticket") {
            TicketScreen(
                ticketId = null,
                ticketDb = tecnicoDb
            )
        }

        //Navegar a la pantalla de Prioridades
        composable("crear_prioridad") {
            PrioridadScreen (
                prioridadId = null,
                prioridadDb = tecnicoDb
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
            TecnicoEditScreen(
                tecnicoId = args.tecnicoId,
                tecnicoDb = tecnicoDb,
                navController = navController,

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
                    navController.navigate(Screen.TecnicoEdit(ticket.ticketId!!))
                },
                onDelete = { ticket ->
                    coroutineScope.launch {
                        tecnicoDb.ticketDao().delete(ticket)
                    }
                }
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
                        tecnicoDb.prioridadDao().delete(prioridad)
                    }
                }
            )
        }




    }
}
