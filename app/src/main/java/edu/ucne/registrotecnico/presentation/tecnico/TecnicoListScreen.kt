package edu.ucne.registrotecnico.presentation.tecnico

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.ucne.registrotecnico.data.local.database.TecnicoDb
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TecnicoListScreen(
    tecnicoList: List<TecnicoEntity>,
    tecnicoCreate: () -> Unit,
    onEdit: (TecnicoEntity) -> Unit,
    onDelete: (TecnicoEntity) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Lista de técnicos",
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF2196F3)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = tecnicoCreate,
                modifier = Modifier.padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir Técnico")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(tecnicoList) { tecnico ->
                    TecnicoRow(tecnico, onEdit, onDelete)
                }
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
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
        elevation = CardDefaults.cardElevation(6.dp)
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
}

suspend fun saveTecnico(tecnicoDb: TecnicoDb, tecnico: TecnicoEntity) {
    tecnicoDb.tecnicoDao().save(tecnico)
}
