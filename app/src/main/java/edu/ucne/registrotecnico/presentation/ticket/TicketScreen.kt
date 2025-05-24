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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import edu.ucne.registrotecnico.data.local.database.TecnicoDb
import edu.ucne.registrotecnico.data.local.entities.TicketEntity
import kotlinx.coroutines.launch
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import java.util.Date

import java.text.SimpleDateFormat
import java.util.*

// Reemplaza el bloque de imports si es necesario:
import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar

// En la función Composable...
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketScreen(ticketId: Int?, ticketDb: TecnicoDb) {
    var fecha by remember { mutableStateOf(Date()) }
    var prioridad by remember { mutableStateOf("") }
    var cliente by remember { mutableStateOf("") }
    var asunto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var tecnico by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var editando by remember { mutableStateOf<TicketEntity?>(null) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val prioridadList by ticketDb.prioridadDao().getAll().collectAsState(initial = emptyList())
    val tecnicoList by ticketDb.tecnicoDao().getAll().collectAsState(initial = emptyList())

    var expandedPrioridad by remember { mutableStateOf(false) }
    var expandedTecnico by remember { mutableStateOf(false) }

    val dateFormatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val fechaTexto = fecha?.let { dateFormatter.format(it) } ?: ""

    // Para mostrar el DatePicker
    val calendar = Calendar.getInstance()
    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                calendar.set(year, month, dayOfMonth)
                fecha = calendar.time
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Registrar Ticket",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF2196F3)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    // Campo de fecha con selector
                    OutlinedTextField(
                        label = { Text("Fecha") },
                        value = fechaTexto,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                datePickerDialog.show()
                            }
                    )

                    // Prioridad
                    ExposedDropdownMenuBox(
                        expanded = expandedPrioridad,
                        onExpandedChange = { expandedPrioridad = !expandedPrioridad }
                    ) {
                        TextField(
                            readOnly = true,
                            value = prioridadList.find { it.prioridadId.toString() == prioridad }?.descripcion
                                ?: "",
                            onValueChange = {},
                            label = { Text("Seleccione una Prioridad") },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedPrioridad,
                            onDismissRequest = { expandedPrioridad = false }
                        ) {
                            prioridadList.forEach {
                                DropdownMenuItem(
                                    text = { Text(it.descripcion) },
                                    onClick = {
                                        prioridad = it.prioridadId.toString()
                                        expandedPrioridad = false
                                    }
                                )
                            }
                        }
                    }

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

                    // Técnico
                    ExposedDropdownMenuBox(
                        expanded = expandedTecnico,
                        onExpandedChange = { expandedTecnico = !expandedTecnico }
                    ) {
                        TextField(
                            readOnly = true,
                            value = tecnicoList.find { it.tecnicoId.toString() == tecnico }?.nombre ?: "",
                            onValueChange = {},
                            label = { Text("Seleccione un Técnico") },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedTecnico,
                            onDismissRequest = { expandedTecnico = false }
                        ) {
                            tecnicoList.forEach {
                                DropdownMenuItem(
                                    text = { Text(it.nombre) },
                                    onClick = {
                                        tecnico = it.tecnicoId.toString()
                                        expandedTecnico = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.padding(2.dp))

                    errorMessage?.let {
                        Text(text = it, color = Color.Red)
                    }

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = {
                                fecha = Date()
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
                                if (fecha == null) {
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
                                            fecha = fecha!!,
                                            prioridadId = prioridad.toIntOrNull(),
                                            cliente = cliente,
                                            asunto = asunto,
                                            descripcion = descripcion,
                                            tecnicoId = tecnico.toIntOrNull() ?: 0
                                        )
                                    )


                                    fecha = Date()
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
