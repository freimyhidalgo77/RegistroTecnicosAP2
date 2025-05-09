package edu.ucne.registrotecnico

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.room.*
import edu.ucne.registrotecnico.ui.theme.RegistroTecnicoTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var tecnicoDb: TecnicoDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        tecnicoDb = Room.databaseBuilder(
            applicationContext,
            TecnicoDb::class.java,
            "TecnicoDb"
        ).fallbackToDestructiveMigration().build()

        setContent {
            RegistroTecnicoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        TecnicoScreen(tecnicoDb)
                    }
                }
            }
        }
    }
}

@Composable
fun TecnicoScreen(tecnicoDb: TecnicoDb) {
    var nombre by remember { mutableStateOf("") }
    var sueldoText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var editando by remember { mutableStateOf<TecnicoEntity?>(null) }
    val scope = rememberCoroutineScope()
    val tecnicoList by tecnicoDb.tecnicoDao().getAll().collectAsState(initial = emptyList())

    Scaffold { innerPadding ->
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
                        label = { Text("Nombre del técnico") },
                        value = nombre,
                        onValueChange = { nombre = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        label = { Text("Sueldo del técnico") },
                        value = sueldoText,
                        onValueChange = { sueldoText = it },
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
                                sueldoText = ""
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
                                    errorMessage = "El nombre no puede estar vacío."
                                    return@OutlinedButton
                                }

                                val sueldo = sueldoText.toDoubleOrNull()
                                if (sueldo == null) {
                                    errorMessage = "El sueldo debe tener un valor válido."
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
                                    sueldoText = ""
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

            TecnicoListScreen(
                tecnicoList,
                onEdit = { tecnico ->
                    nombre = tecnico.nombre
                    sueldoText = tecnico.sueldo.toString()
                    editando = tecnico
                },
                onDelete = { tecnico ->
                    scope.launch {
                        tecnicoDb.tecnicoDao().delete(tecnico)
                    }
                }
            )
        }
    }
}

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

@Entity(tableName = "Tecnicos")
data class TecnicoEntity(
    @PrimaryKey(autoGenerate = true)
    val tecnicoId: Int? = null,
    val nombre: String = "",
    val sueldo: Double
)

@Dao
interface TecnicoDao {
    @Upsert
    suspend fun save(tecnicos: TecnicoEntity)

    @Query("SELECT * FROM Tecnicos WHERE tecnicoId = :id LIMIT 1")
    suspend fun find(id: Int): TecnicoEntity

    @Delete
    suspend fun delete(tecnicos: TecnicoEntity)

    @Query("SELECT * FROM Tecnicos")
    fun getAll(): Flow<List<TecnicoEntity>>
}

@Database( 
    entities = [TecnicoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TecnicoDb : RoomDatabase() {
    abstract fun tecnicoDao(): TecnicoDao
}
