package edu.ucne.registrotecnico.presentation.ticket

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.ucne.registrotecnico.data.local.database.TecnicoDb
import edu.ucne.registrotecnico.data.local.entities.TicketEntity
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketScreen(ticketId: Int?, ticketDb: TecnicoDb) {
    var fecha by remember { mutableStateOf("") }
    var prioridad by remember { mutableStateOf("") }
    var cliente by remember { mutableStateOf("") }
    var asunto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var tecnico by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var editando by remember { mutableStateOf<TicketEntity?>(null) }

    val scope = rememberCoroutineScope()
    val ticketList by ticketDb.ticketDao().getAll().collectAsState(initial = emptyList())
    val prioridadList by ticketDb.prioridadDao().getAll().collectAsState(initial = emptyList())
    val tecnicoList by ticketDb.tecnicoDao().getAll().collectAsState(initial = emptyList())

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
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

                    // Dropdown de Prioridad
                    var expandedPrioridad by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expandedPrioridad,
                        onExpandedChange = { expandedPrioridad = !expandedPrioridad }
                    ) {
                        TextField(
                            readOnly = true,
                            value = prioridad,
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
                        label = { Text("Descripción") },
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Dropdown de Tecnico
                    var expandedTecnico by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expandedTecnico,
                        onExpandedChange = { expandedTecnico = !expandedTecnico }
                    ) {
                        TextField(
                            readOnly = true,
                            value = tecnico,
                            onValueChange = {},
                            label = { Text("Seleccione un Técnico") },
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

                    Spacer(modifier = Modifier.padding(2.dp))

                    errorMessage?.let {
                        Text(text = it, color = Color.Red)
                    }

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(onClick = {
                            fecha = ""
                            prioridad = ""
                            cliente = ""
                            asunto = ""
                            descripcion = ""
                            tecnico = ""
                            errorMessage = null
                            editando = null
                        }) {
                            Icon(Icons.Default.Add, contentDescription = "Nuevo")
                            Text("Nuevo")
                        }

                        OutlinedButton(onClick = {
                            if (fecha.isBlank() || prioridad.isBlank() || cliente.isBlank() ||
                                asunto.isBlank() || descripcion.isBlank() || tecnico.isBlank()
                            ) {
                                errorMessage = "Todos los campos son obligatorios"
                                return@OutlinedButton
                            }

                            scope.launch {
                                saveTicket(
                                    ticketDb,
                                    TicketEntity(
                                        ticketId = editando?.ticketId,
                                        fecha = fecha,
                                        prioridadId = prioridad.toIntOrNull(),
                                        cliente = cliente,
                                        asunto = asunto,
                                        descripcion = descripcion,
                                        tecnicoId = tecnico.toIntOrNull() ?: 0
                                    )
                                )
                                fecha = ""
                                prioridad = ""
                                cliente = ""
                                asunto = ""
                                descripcion = ""
                                tecnico = ""
                                errorMessage = null
                                editando = null
                            }
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Guardar")
                            Text("Guardar")
                        }
                    }
                }
            }
        }
    }
}





