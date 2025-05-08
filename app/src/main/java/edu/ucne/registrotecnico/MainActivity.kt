package edu.ucne.registrotecnico

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Upsert
import edu.ucne.registrotecnico.ui.theme.RegistroTecnicoTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    //var de base de datos
    private lateinit var tecnicoDb: TecnicoDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //Crear migracion
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
                        label = { Text(text = "Nombre del tecnico") },
                        value = nombre,
                        onValueChange = { nombre = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        label = { Text(text = "Sueldo del tecnico") },
                        value = sueldoText,
                        onValueChange = { sueldoText = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.padding(2.dp))

                    errorMessage?.let {
                        Text(text = it, color = Color.Red)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        OutlinedButton(
                            onClick = {
                                nombre = ""
                                sueldoText = ""
                                errorMessage = null
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "nuevo boton"
                            )
                            Text(text = "Nuevo")
                        }

                        OutlinedButton(
                            onClick = {
                                if (nombre.isBlank()) {
                                    errorMessage = "El nombre no puede estar vacio."
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
                                            nombre = nombre,
                                            sueldo = sueldo
                                        )
                                    )
                                    nombre = ""
                                    sueldoText = ""
                                    errorMessage = null
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "boton guardar"
                            )
                            Text(text = "Guardar")
                        }
                    }
                }
            }

            TecnicoListScreen(tecnicoList)
        }
    }
}

@Composable
fun TecnicoListScreen(tecnicoList: List<TecnicoEntity>) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Lista de tecnicos",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(tecnicoList) { tecnico ->
                TecnicoRow(tecnico)
            }
        }
    }
}

@Composable
fun TecnicoRow(tecnico: TecnicoEntity) {
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
    }
    HorizontalDivider()
}

suspend fun saveTecnico(tecnicoDb: TecnicoDb, tecnico: TecnicoEntity) {
    tecnicoDb.tecnicoDao().save(tecnico)
}

//Entidad tecnico
@Entity(tableName = "Tecnicos")
data class TecnicoEntity(
    @PrimaryKey(autoGenerate = true)
    val tecnicoId: Int? = null,
    val nombre: String = "",
    val sueldo: Double
)

//Dao
@Dao
interface TecnicoDao {
    @Upsert
    suspend fun save(tecnicos: TecnicoEntity)

    @Query("""
        SELECT *
        FROM Tecnicos
        WHERE tecnicoId=:id
        LIMIT 1
    """)
    suspend fun find(id: Int): TecnicoEntity

    @Delete
    suspend fun delete(tecnicos: TecnicoEntity)

    @Query("SELECT * FROM Tecnicos")
    fun getAll(): Flow<List<TecnicoEntity>>
}

//Base de Datos
@Database(
    entities = [TecnicoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TecnicoDb : RoomDatabase() {
    abstract fun tecnicoDao(): TecnicoDao
}