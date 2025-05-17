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

                    OutlinedTextField(
                        label = { Text("Prioridad ID") },
                        value = prioridad,
                        onValueChange = { prioridad = it },
                        modifier = Modifier.fillMaxWidth()
                    )

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

                    OutlinedTextField(
                        label = { Text("Técnico ID") },
                        value = tecnico,
                        onValueChange = { tecnico = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.padding(2.dp))

                    errorMessage?.let {
                        Text(text = it, color = Color.Red)
                    }

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = {
                                fecha = ""
                                prioridad = ""
                                cliente = ""
                                asunto = ""
                                descripcion = ""
                                tecnico = ""
                                errorMessage = null
                                editando = null
                            }
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Nuevo")
                            Text("Nuevo")
                        }

                        OutlinedButton(
                            onClick = {
                                if (fecha.isBlank()) {
                                    errorMessage = "La fecha no puede estar vacía."
                                    return@OutlinedButton
                                }

                                if (prioridad.isBlank()) {
                                    errorMessage = "Ingrese la prioridad."
                                    return@OutlinedButton
                                }

                                if (cliente.isBlank()) {
                                    errorMessage = "El nombre del cliente no puede estar vacío."
                                    return@OutlinedButton
                                }

                                if (asunto.isBlank()) {
                                    errorMessage = "El asunto no puede estar vacío."
                                    return@OutlinedButton
                                }

                                if (descripcion.isBlank()) {
                                    errorMessage = "La descripción no puede estar vacía."
                                    return@OutlinedButton
                                }

                                if (tecnico.isBlank()) {
                                    errorMessage = "El técnico no puede estar vacío."
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
                            }
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Guardar")
                            Text("Guardar")
                        }
                    }
                }
            }

        }
    }
}




