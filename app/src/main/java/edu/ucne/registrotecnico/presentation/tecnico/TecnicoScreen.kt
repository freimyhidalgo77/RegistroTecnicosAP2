package edu.ucne.registrotecnico.presentation.tecnico

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
import androidx.navigation.NavController
import edu.ucne.registrotecnico.data.local.database.TecnicoDb
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import kotlinx.serialization.Serializable

import kotlinx.coroutines.launch

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TecnicoScreen(tecnicoId: Int?, tecnicoDb: TecnicoDb) {
    var nombre by remember { mutableStateOf("") }
    var sueldo by remember { mutableStateOf(0.0) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var editando by remember { mutableStateOf<TecnicoEntity?>(null) }
    val scope = rememberCoroutineScope()
    val tecnicoList by tecnicoDb.tecnicoDao().getAll().collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Registrar Tecnico",
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
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    OutlinedTextField(
                        label = { Text("Nombre del tecnico") },
                        value = nombre,
                        onValueChange = { nombre = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        label = { Text("Sueldo del tecnico") },
                        value = sueldo.toString(),
                        onValueChange = {
                            val newValue = it.toDoubleOrNull()
                            if (newValue != null) {
                                sueldo = newValue
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.padding(2.dp))

                    errorMessage?.let {
                        Text(text = it, color = Color.Red)
                    }

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = {
                                nombre = ""
                                sueldo = 0.0
                                errorMessage = null
                                editando = null
                            }
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Nuevo")
                            Text("Nuevo")
                        }

                        OutlinedButton(
                            onClick = {
                                if (nombre.isBlank()) {
                                    errorMessage = "El nombre no puede estar vacio."
                                    return@OutlinedButton
                                }
                                if (sueldo == 0.0) {
                                    errorMessage = "El sueldo debe tener un valor valido."
                                    return@OutlinedButton
                                }

                                scope.launch {
                                    saveTecnico(
                                        tecnicoDb,
                                        TecnicoEntity(
                                            tecnicoId = editando?.tecnicoId,
                                            nombre = nombre,
                                            sueldo = sueldo
                                        )
                                    )
                                    nombre = ""
                                    sueldo = 0.0
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
