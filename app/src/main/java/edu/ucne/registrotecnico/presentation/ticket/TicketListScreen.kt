package edu.ucne.registrotecnico.presentation.ticket

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.ucne.registrotecnico.data.local.database.TecnicoDb
import edu.ucne.registrotecnico.data.local.entities.MensajeEntity
import edu.ucne.registrotecnico.data.local.entities.TicketEntity

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TicketListScreen(
    ticketList: List<TicketEntity>,
    ticketCreate: () -> Unit,
    onEdit: (TicketEntity) -> Unit,
    onDelete: (TicketEntity) -> Unit,
    onVerMensajes: (TicketEntity) -> Unit

) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Lista de Tickets",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF2196F3)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = ticketCreate,
                modifier = Modifier.padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir Ticket")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
            ) {
                items(ticketList) { ticket ->
                    TicketRow(ticket, onEdit, onDelete, onVerMensajes)
                }
            }
        }
    }
}

@Composable
fun TicketRow(
    ticket: TicketEntity,
    onEdit: (TicketEntity) -> Unit,
    onDelete: (TicketEntity) -> Unit,
    onVerMensajes: (TicketEntity) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(5f),
                verticalArrangement = Arrangement.Center
            ) {
                Text("Ticket ID: ${ticket.ticketId ?: "N/A"}", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))
                Text("Fecha: ${ticket.fecha}", fontSize = 14.sp)
                Text("Prioridad ID: ${ticket.prioridadId}", fontSize = 14.sp)
                Text("Cliente: ${ticket.cliente}", fontSize = 14.sp)
                Text("Asunto: ${ticket.asunto}", fontSize = 14.sp)
                Text("Descripcion: ${ticket.descripcion}", fontSize = 14.sp)
                Text("Tecnico ID: ${ticket.tecnicoId}", fontSize = 14.sp)
            }

            Box(modifier = Modifier.weight(1f)) {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "Más opciones")
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                ) {
                    DropdownMenuItem(
                        text = { Text("Mensajes") },
                        leadingIcon = { Icon(Icons.Filled.MailOutline, contentDescription = null) },
                        onClick = {
                            expanded = false
                            onVerMensajes(ticket)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Editar") },
                        leadingIcon = { Icon(Icons.Filled.Edit, contentDescription = null) },
                        onClick = {
                            expanded = false
                            onEdit(ticket)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Eliminar") },
                        leadingIcon = { Icon(Icons.Filled.Delete, contentDescription = null) },
                        onClick = {
                            expanded = false
                            onDelete(ticket)
                        }
                    )
                }
            }
        }
    }
}


suspend fun saveTicket(ticketDb: TecnicoDb, ticket: TicketEntity) {
    ticketDb.ticketDao().save(ticket)
}
