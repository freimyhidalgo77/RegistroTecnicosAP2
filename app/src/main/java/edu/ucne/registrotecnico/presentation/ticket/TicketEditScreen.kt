package edu.ucne.registrotecnico.presentation.ticket

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
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnico.data.local.entities.TicketEntity
import kotlinx.coroutines.launch

import java.text.SimpleDateFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketEditScreen(ticketId: Int, ticketDb: TecnicoDb, navController: NavController) {
    var fecha by remember { mutableStateOf("") }
    var prioridad by remember { mutableStateOf("") }
    var cliente by remember { mutableStateOf("") }
    var asunto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var tecnico by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val prioridadList by ticketDb.prioridadDao().getAll().collectAsState(initial = emptyList())
    val tecnicoList by ticketDb.tecnicoDao().getAll().collectAsState(initial = emptyList())

    var expandedPrioridad by remember { mutableStateOf(false) }
    var expandedTecnico by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(ticketId) {
        val ticket = ticketDb.ticketDao().find(ticketId)
        ticket?.let {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            fecha = it.fecha?.let { fechaDate -> sdf.format(fechaDate) } ?: ""
            prioridad = it.prioridadId.toString()
            cliente = it.cliente
            asunto = it.asunto
            descripcion = it.descripcion
            tecnico = it.tecnicoId.toString()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Modificar ticket",
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
        )  {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    OutlinedTextField(
                        label = { Text("Fecha") },
                        value = fecha,
                        onValueChange = { fecha = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    ExposedDropdownMenuBox(
                        expanded = expandedPrioridad,
                        onExpandedChange = { expandedPrioridad = !expandedPrioridad }
                    ) {
                        TextField(
                            readOnly = true,
                            value = prioridadList.find { it.prioridadId.toString() == prioridad }?.descripcion ?: "",
                            onValueChange = {},
                            label = { Text("Seleccione una Prioridad") },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedPrioridad,
                            onDismissRequest = { expandedPrioridad = false }
                        ) {
                            prioridadList.forEach {
                                DropdownMenuItem(
                                    text = { Text(it.descripcion) },
                                    onClick = {
                                        prioridad = it.prioridadId.toString()
                                        expandedPrioridad = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        label = { Text("Nombre del cliente") },
                        value = cliente,
                        onValueChange = { cliente = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        label = { Text("Asunto") },
                        value = asunto,
                        onValueChange = { asunto = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        label = { Text("Descripcion") },
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    ExposedDropdownMenuBox(
                        expanded = expandedTecnico,
                        onExpandedChange = { expandedTecnico = !expandedTecnico }
                    ) {
                        TextField(
                            readOnly = true,
                            value = tecnicoList.find { it.tecnicoId.toString() == tecnico }?.nombre ?: "",
                            onValueChange = {},
                            label = { Text("Seleccione un Tecnico") },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedTecnico,
                            onDismissRequest = { expandedTecnico = false }
                        ) {
                            tecnicoList.forEach {
                                DropdownMenuItem(
                                    text = { Text(it.nombre) },
                                    onClick = {
                                        tecnico = it.tecnicoId.toString()
                                        expandedTecnico = false
                                    }
                                )
                            }
                        }
                    }

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

                                if (fecha.isBlank() || cliente.isBlank() || asunto.isBlank()) {
                                    errorMessage = "Todos los campos deben estar completos"
                                    return@OutlinedButton
                                }

                                val prioridadId = prioridad.toIntOrNull()
                                val tecnicoId = tecnico.toIntOrNull()

                                if (prioridadId == null || tecnicoId == null) {
                                    errorMessage = "Prioridad y tecnico deben ser validos"
                                    return@OutlinedButton
                                }

                                scope.launch {
                                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                    val fechaDate = sdf.parse(fecha)

                                    val ticket = TicketEntity(
                                        ticketId = ticketId,
                                        fecha = fechaDate,
                                        prioridadId = prioridadId,
                                        cliente = cliente,
                                        asunto = asunto,
                                        descripcion = descripcion,
                                        tecnicoId = tecnicoId
                                    )
                                    ticketDb.ticketDao().save(ticket)
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
                            onClick = { navController.popBackStack() },
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
    }
}

