package edu.ucne.registrotecnico.presentation.navigation

import HomeScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.ucne.registrotecnico.data.local.database.TecnicoDb
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnico.presentation.tecnico.TecnicoListScreen
import edu.ucne.registrotecnico.presentation.tecnico.TecnicoScreen
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
                onEdit = {},
                onDelete = { tecnico ->
                    coroutineScope.launch {
                        tecnicoDb.tecnicoDao().delete(tecnico)
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


        composable("tickets") {
            TicketScreen(ticketId = null, ticketDb = tecnicoDb)
        }
    }
}
