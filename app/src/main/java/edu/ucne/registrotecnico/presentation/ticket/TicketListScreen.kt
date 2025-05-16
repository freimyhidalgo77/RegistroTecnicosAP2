package edu.ucne.registrotecnico.presentation.ticket

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.ucne.registrotecnico.data.local.database.TecnicoDb
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnico.data.local.entities.TicketEntity

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TicketListScreen(
    ticketList: List<TicketEntity>,
    onEdit: (TicketEntity) -> Unit,
    onDelete: (TicketEntity) -> Unit
) {


    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(16.dp))

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(ticketList) { ticket ->
                TicketRow(ticket, onEdit, onDelete)
            }
        }
    }
}

@Composable
fun TicketRow(
    ticket: TicketEntity,
    onEdit: (TicketEntity) -> Unit,
    onDelete: (TicketEntity) -> Unit
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = ticket.ticketId?.toString() ?: "N/A"
        )
        Text(
            modifier = Modifier.weight(2f),
            text = ticket.fecha,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            modifier = Modifier.weight(1f),
            text = ticket.prioridadId.toString() ?: "N/A"
        )
        Text(
            modifier = Modifier.weight(1f),
            text = ticket.cliente
        )
        Text(
            modifier = Modifier.weight(1f),
            text = ticket.asunto
        )
        Text(
            modifier = Modifier.weight(1f),
            text = ticket.descripcion
        )
        Text(
            modifier = Modifier.weight(1f),
            text = ticket.tecnicoId.toString()
        )

        Text(
                modifier = Modifier.weight(1f),
        text = ticket.prioridadId.toString() ?: "N/A"
        )


        IconButton(onClick = { onEdit(ticket) }) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Editar ticket",
                tint = Color.Blue
            )
        }
        IconButton(onClick = { onDelete(ticket) }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Eliminar ticket",
                tint = Color.Red
            )
        }
    }
    HorizontalDivider()
}


suspend fun saveTicket(ticketDb: TecnicoDb, ticket: TicketEntity) {
    ticketDb.ticketDao().save(ticket)
}

