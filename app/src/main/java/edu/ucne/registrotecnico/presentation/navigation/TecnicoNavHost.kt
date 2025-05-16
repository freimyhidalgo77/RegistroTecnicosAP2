package edu.ucne.registrotecnico.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.registrotecnico.data.local.database.TecnicoDb
import edu.ucne.registrotecnico.presentation.tecnico.TecnicoListScreen
import edu.ucne.registrotecnico.presentation.tecnico.TecnicoScreen


@Composable
fun TecnicosNavHost(
    navHostController: NavHostController,
    tecnicoDb: TecnicoDb
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.TecnicoList
    ) {
        composable<Screen.TecnicoList> {
            TecnicoListScreen(
                tecnicoList = emptyList(),
                onEdit = { tecnico ->
                    tecnico.tecnicoId?.let { id ->
                        navHostController.navigate(Screen.Tecnico(id))
                    }
                },
                onDelete = { }
            )
        }

        composable<Screen.Tecnico> { backStack ->
            val tecnicoId = backStack.toRoute<Screen.Tecnico>().tecnicoId
            TecnicoScreen(tecnicoId, tecnicoDb)
        }
    }
}

