package edu.ucne.registrotecnico.presentation.ticket

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.ucne.registrotecnico.data.local.database.TecnicoDb
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnico.data.local.entities.TicketEntity
import edu.ucne.registrotecnico.presentation.tecnico.TecnicoListScreen
import edu.ucne.registrotecnico.presentation.tecnico.saveTecnico
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.launch

@Composable
fun TicketScreen(ticketDb: TecnicoDb) {
    var fecha by remember { mutableStateOf("") }
    var prioridad by remember { mutableStateOf(Int.toString()) }
    var cliente by remember { mutableStateOf("") }
    var asunto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var tecnico by remember { mutableStateOf(Int.toString()) }
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
        )
        {
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
                        value = prioridad.toString(),
                        onValueChange = { prioridad = it },

                    )

                    OutlinedTextField(
                        label = { Text("Nombre del cliente") },
                        value = cliente.toString(),
                        onValueChange = { cliente = it },

                        )

                    OutlinedTextField(
                        label = { Text("Asunto") },
                        value = asunto.toString(),
                        onValueChange = { asunto = it },

                        )

                    OutlinedTextField(
                        label = { Text("Descripcion") },
                        value = descripcion.toString(),
                        onValueChange = { descripcion = it },

                        )

                    OutlinedTextField(
                        label = { Text("Tecnico") },
                        value = tecnico.toString(),
                        onValueChange = { tecnico = it },

                        )

                    Spacer(modifier = Modifier.padding(2.dp))

                    errorMessage?.let {
                        Text(text = it, color = Color.Red)
                    }

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = {
                                fecha = ""
                                prioridad = Int.toString()
                                cliente = ""
                                asunto = ""
                                descripcion = ""
                                tecnico = Int.toString()
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
                                    errorMessage = "La fecha no puede estar vacia."
                                    return@OutlinedButton
                                }

                                if (prioridad.isBlank()) {
                                    errorMessage = "Ingrese la prioridad."
                                    return@OutlinedButton
                                }

                                if (cliente.isBlank()) {
                                    errorMessage = "El nombre del cliente no puede estar vacio."
                                    return@OutlinedButton
                                }

                                if (asunto.isBlank()) {
                                    errorMessage = "El asunto no puede estar vacio."
                                    return@OutlinedButton
                                }

                                if (descripcion.isBlank()) {
                                    errorMessage = "La descripcion no puede estar vacia."
                                    return@OutlinedButton
                                }

                                if (tecnico.isBlank()) {
                                    errorMessage = "El tecnico no puede estar vacio."
                                    return@OutlinedButton
                                }

                                scope.launch {
                                    saveTicket(
                                        ticketDb,
                                        TicketEntity(
                                            ticketId = editando?.ticketId,
                                            fecha = fecha,
                                            prioridadId = prioridad.toIntOrNull(),
                                            cliente =  cliente,
                                            asunto = asunto,
                                            descripcion = descripcion,
                                            tecnicoId = tecnico.toInt()

                                        )
                                    )

                                    fecha = ""
                                    prioridad = ""
                                    cliente =  ""
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

            TicketListScreen(
                ticketList,
                onEdit = { ticket ->
                    fecha = ticket.fecha
                    prioridad = ticket.prioridadId.toString()
                    cliente = ticket.cliente
                    asunto = ticket.asunto
                    descripcion = ticket.descripcion
                    tecnico = ticket.tecnicoId.toString()
                    editando = ticket
                },
                onDelete = { ticket ->
                    scope.launch {
                        ticketDb.ticketDao().delete(ticket)
                    }
                }
            )
        }
    }
}
