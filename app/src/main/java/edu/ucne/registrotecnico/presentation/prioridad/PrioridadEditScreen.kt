package edu.ucne.registrotecnico.presentation.prioridad

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.ucne.registrotecnico.data.local.database.TecnicoDb
import edu.ucne.registrotecnico.data.local.entities.PrioridadEntity
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioridadEditScreen(prioridadId: Int, prioridadDb: TecnicoDb, navController: NavController) {
    var descripcion by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(prioridadId) {
        val prioridad = prioridadDb.prioridadDao().find(prioridadId)
        prioridad?.let {
            descripcion = it.descripcion
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Modificar prioridad",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            OutlinedTextField(
                label = { Text("Descripcion") },
                value = descripcion,
                onValueChange = { descripcion = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            errorMessage?.let {
                Text(text = it, color = Color.Red)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = {
                        if (descripcion.isBlank()) {
                            errorMessage = "El descripcion no puede estar vacia"
                            return@OutlinedButton
                        }

                        scope.launch {
                            val prioridad = PrioridadEntity(prioridadId = prioridadId, descripcion = descripcion)
                            prioridadDb.prioridadDao().save(prioridad)
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Guardar cambios")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Actualizar")
                }

                OutlinedButton(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Volver")
                }
            }
        }
    }
}