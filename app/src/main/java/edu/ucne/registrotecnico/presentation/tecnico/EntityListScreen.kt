package edu.ucne.registrotecnico.presentation.tecnico

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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TecnicoListScreen(
    tecnicoList: List<TecnicoEntity>,
    onEdit: (TecnicoEntity) -> Unit,
    onDelete: (TecnicoEntity) -> Unit
) {


    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Lista de técnicos",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(tecnicoList) { tecnico ->
                TecnicoRow(tecnico, onEdit, onDelete)
            }
        }
    }
}

@Composable
fun TecnicoRow(
    tecnico: TecnicoEntity,
    onEdit: (TecnicoEntity) -> Unit,
    onDelete: (TecnicoEntity) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = tecnico.tecnicoId?.toString() ?: "N/A"
        )
        Text(
            modifier = Modifier.weight(2f),
            text = tecnico.nombre,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            modifier = Modifier.weight(1f),
            text = tecnico.sueldo.toString()
        )

        IconButton(onClick = { onEdit(tecnico) }) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Editar técnico",
                tint = Color.Blue
            )
        }
        IconButton(onClick = { onDelete(tecnico) }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Eliminar técnico",
                tint = Color.Red
            )
        }
    }
    HorizontalDivider()
}


suspend fun saveTecnico(tecnicoDb: TecnicoDb, tecnico: TecnicoEntity) {
    tecnicoDb.tecnicoDao().save(tecnico)
}

