package edu.ucne.registrotecnico.presentation.Mensaje

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.registrotecnico.data.local.entities.MensajeEntity
import java.text.SimpleDateFormat
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import java.util.*
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MensajeScreen(viewModel: MensajeViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val mensajes by viewModel.mensajes.collectAsState()

    var selectRole by remember { mutableStateOf("Operador") }
    var errorMessage by remember { mutableStateOf("") }

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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = uiState.rol == "Operador",
                        onClick = { viewModel.onRolChange("Operador") }
                    )
                    Text("Operador")
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = uiState.rol == "Owner",
                        onClick = { viewModel.onRolChange("Owner") }
                    )
                    Text("Owner")
                }
            }


            OutlinedTextField(
                value = uiState.remitente,
                onValueChange = { viewModel.onRemitenteChange(it) },
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
                    if (uiState.remitente.isBlank() || uiState.descripcion.isBlank()) {
                        errorMessage = "Todos los campos son obligatorios"
                    } else {
                        errorMessage = ""
                        viewModel.save()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(mensajes) { mensaje ->
                    MensajeCard(
                        mensaje = mensaje,
                        onDelete = { viewModel.deleteMensaje(it) }
                    )
                }
            }
        }
    }
}

@Composable
fun MensajeCard(mensaje: MensajeEntity, onDelete: (MensajeEntity) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Remitente: ")
                        }
                        append(mensaje.remitente)
                    }
                )

                Spacer(modifier = Modifier.height(25.dp))

                Text(
                    text = mensaje.rol,
                    fontWeight = FontWeight.Bold,
                    color = when (mensaje.rol) {
                        "Owner" -> Color.Green
                        "Operador" -> Color.Blue
                        else -> Color.Black
                    }
                )

            }

            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Descripción: ")
                    }
                    append(mensaje.descripcion)
                }
            )

            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Fecha: ")
                    }
                    append(
                        SimpleDateFormat(
                            "dd/MM/yyyy HH:mm",
                            Locale.getDefault()
                        ).format(mensaje.fecha)
                    )
                }
            )

            Spacer(modifier = Modifier.height(25.dp))

            IconButton(
                onClick = { onDelete(mensaje) },
                modifier = Modifier
                    .align(Alignment.End)
                    .size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = Color.Red
                )
            }
        }
    }
}



