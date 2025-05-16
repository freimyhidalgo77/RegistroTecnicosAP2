package edu.ucne.registrotecnico.presentation.navigation

import HomeScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.ucne.registrotecnico.data.local.database.TecnicoDb
import edu.ucne.registrotecnico.presentation.tecnico.TecnicoScreen
import edu.ucne.registrotecnico.presentation.ticket.TicketScreen

@Composable
fun AppNavigation(tecnicoDb: TecnicoDb) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }

        composable("tecnicos") {

            TecnicoScreen(tecnicoId = null, tecnicoDb = tecnicoDb)
        }

        composable("tickets") {
            TicketScreen(ticketId = null, ticketDb = tecnicoDb)
        }
    }
}

