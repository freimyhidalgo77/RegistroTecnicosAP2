package edu.ucne.registrotecnico.presentation.tecnico

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import edu.ucne.registrotecnico.data.local.database.TecnicoDb
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedButton
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.ArrowBack


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TecnicoEditScreen(tecnicoId: Int, tecnicoDb: TecnicoDb, navController: NavController) {
    var nombre by remember { mutableStateOf("") }
    var sueldo by remember { mutableStateOf(0.0) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(tecnicoId) {
        val tecnico = tecnicoDb.tecnicoDao().find(tecnicoId)
        tecnico?.let {
            nombre = it.nombre
            sueldo = it.sueldo
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Modificar técnico",
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
                label = { Text("Nombre del técnico") },
                value = nombre,
                onValueChange = { nombre = it },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                label = { Text("Sueldo del técnico") },
                value = sueldo.toString(),
                onValueChange = {
                    val newValue = it.toDoubleOrNull()
                    if (newValue != null) {
                        sueldo = newValue
                    }
                },
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
                        if (nombre.isBlank()) {
                            errorMessage = "El nombre no puede estar vacío"
                            return@OutlinedButton
                        }
                        if (sueldo <= 0.0) {
                            errorMessage = "Sueldo inválido"
                            return@OutlinedButton
                        }

                        scope.launch {
                            val tecnico = TecnicoEntity(tecnicoId = tecnicoId, nombre = nombre, sueldo = sueldo)
                            tecnicoDb.tecnicoDao().save(tecnico)
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