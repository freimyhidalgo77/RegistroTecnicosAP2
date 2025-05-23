package edu.ucne.registrotecnico.presentation.Mensaje

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.registrotecnico.data.local.entities.MensajeEntity
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MensajeScreen(viewModel: MensajeViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val mensajes by viewModel.mensajes.collectAsState()

    var tecnicoId by remember { mutableStateOf("") }
    var selectRole by remember { mutableStateOf("Operador") }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Enviar mensaje",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectRole == "Operador",
                        onClick = { selectRole = "Operador" }
                    )
                    Text("Operador")
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectRole == "Owner",
                        onClick = { selectRole = "Owner" }
                    )
                    Text("Owner")
                }
            }

            OutlinedTextField(
                value = tecnicoId,
                onValueChange = { tecnicoId = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.descripcion,
                onValueChange = { viewModel.onDescripcionChange(it) },
                label = { Text("Mensaje") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                singleLine = false,
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = uiState.fecha?.let {
                    "Fecha: ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(it)}"
                } ?: "Fecha: No disponible"
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val mensaje = MensajeEntity(
                        mensajeId = 0,
                        tecnicoId = tecnicoId.toIntOrNull() ?: 0,
                        descripcion = "$selectRole: ${uiState.descripcion}",
                        fecha = Date()
                    )
                    viewModel.save(mensaje)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(mensajes) { mensaje ->
                    MensajeCard(mensaje = mensaje)
                }
            }
        }
    }
}

@Composable
fun MensajeCard(mensaje: MensajeEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Descripción: ${mensaje.descripcion}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Fecha: ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(mensaje.fecha)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(Color.Blue)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Técnico ID: ${mensaje.tecnicoId}",
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
