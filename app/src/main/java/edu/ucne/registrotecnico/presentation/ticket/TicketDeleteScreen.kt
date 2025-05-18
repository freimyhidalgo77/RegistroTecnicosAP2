package edu.ucne.registrotecnico.presentation.ticket

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import edu.ucne.registrotecnico.data.local.database.TecnicoDb
import edu.ucne.registrotecnico.data.local.entities.TicketEntity
import kotlinx.coroutines.launch

@Composable
fun TicketDeleteScreen(
    ticketId: Int,
    ticketDb: TecnicoDb,
    navController: NavController
) {
    val ticket = remember { mutableStateOf<TicketEntity?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(ticketId) {
        ticket.value = ticketDb.ticketDao().find(ticketId)
    }

    if (ticket.value == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Cargando datos de la prioridad...")
        }
        return
    }


    val currentTicket = ticket.value!!

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Ticket ID: ${currentTicket.ticketId}",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = "Fecha: ${currentTicket.fecha}",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                )
                Text(
                    text = "Prioridad Id: ${currentTicket.prioridadId}",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                )
                Text(
                    text = "Cliente: ${currentTicket.cliente}",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                )
                Text(
                    text = "Asunto: ${currentTicket.asunto}",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                )
                Text(
                    text = "Descripcion: ${currentTicket.descripcion}",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                )
                Text(
                    text = "Tecnico ID: ${currentTicket.tecnicoId}",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                )
            }
        }

        Text(
            text = "¿Estas seguro que deseas eliminar el ticket ${currentTicket.ticketId}?",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        Row {
            Button(onClick = {
                navController.popBackStack()
            }) {
                Text("Cancelar")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        ticketDb.ticketDao().delete(currentTicket)
                        navController.popBackStack()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Eliminar", color = Color.White)
            }
        }
    }
}
