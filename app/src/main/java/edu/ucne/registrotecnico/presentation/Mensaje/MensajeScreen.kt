package edu.ucne.registrotecnico.presentation.Mensaje

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.registrotecnico.data.local.entities.MensajeEntity
import edu.ucne.registrotecnico.data.local.entities.TicketEntity
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date as UtilDate
import java.sql.Date as SqlDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MensajeScreen(viewModel: MensajeViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
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
                    androidx.compose.material3.RadioButton(
                        selected = selectRole == "Operador",
                        onClick = { selectRole = "Operador" }
                    )
                    Text("Operador")
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    androidx.compose.material3.RadioButton(
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
                    "Fecha: ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(it.time))}"
                } ?: "Fecha: No disponible"
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val mensaje = MensajeEntity(
                        mensajeId = 0,
                        tecnicoId = tecnicoId.toIntOrNull() ?: 0,
                        descripcion = "${selectRole}: ${uiState.descripcion}",
                        fecha = SqlDate(UtilDate().time)
                    )
                    viewModel.save(mensaje)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }
        }
    }
}


@Composable
fun MensajeCard(mensaje: MensajeEntity, tecnicos: List<MensajeEntity>) {
    val tecnico = tecnicos.find { it.tecnicoId == mensaje.tecnicoId }

    Column(modifier = Modifier.padding(horizontal = 4.dp)) {
        Text(
            text = "Mensaje enviado por Técnico en ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(mensaje.fecha)}",
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
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
                    text = "Técnico",
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}







