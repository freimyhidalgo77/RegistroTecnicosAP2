package edu.ucne.registrotecnico.presentation.prioridad

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.ucne.registrotecnico.data.local.database.TecnicoDb
import edu.ucne.registrotecnico.data.local.entities.PrioridadEntity
import edu.ucne.registrotecnico.presentation.prioridad.savePrioridad

import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioridadScreen(prioridadId:Int? ,prioridadDb: TecnicoDb) {
    var descripcion by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var editando by remember { mutableStateOf<PrioridadEntity?>(null) }
    val scope = rememberCoroutineScope()
    val prioridadList by prioridadDb.prioridadDao().getAll().collectAsState(initial = emptyList())


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Registrar prioridad",
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
                        label = { Text("Descripcion de la prioridad") },
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        modifier = Modifier.fillMaxWidth()
                    )


                    Spacer(modifier = Modifier.padding(2.dp))

                    errorMessage?.let {
                        Text(text = it, color = Color.Red)
                    }

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = {
                                descripcion = ""
                                errorMessage = null
                                editando = null
                            }
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Nuevo")
                            Text("Nuevo")
                        }


                        OutlinedButton(
                            onClick = {
                                if (descripcion.isBlank()) {
                                    errorMessage = "La descripcion no puede estar vacia."
                                    return@OutlinedButton
                                }

                                scope.launch {
                                    savePrioridad(
                                        prioridadDb,
                                        PrioridadEntity(
                                            prioridadId = editando?.prioridadId,
                                            descripcion = descripcion,
                                        )
                                    )
                                    descripcion = ""
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
