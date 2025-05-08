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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.room.Dao

import androidx.lifecycle.compose.collectAsStateWithLifecycle



//Modelo
import androidx.room.Entity
import androidx.room.PrimaryKey
import edu.ucne.registrotecnico.ui.theme.RegistroTecnicoTheme

//Base de Datos
import androidx.room.Database
import androidx.room.RoomDatabase


//Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Room
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.Alignment
import androidx.room.Room.databaseBuilder
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    //var de base de datos
    private lateinit var tecnicoDb:TecnicoDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //Crear migracion
        tecnicoDb = databaseBuilder(
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

                    ){
                       TecnicoScreen()
                    }

                }
            }
        }
    }
}


@Composable
fun TecnicoScreen(
){

    var nombre by remember { mutableStateOf("") }
    var  sueldo by remember { mutableStateOf("")}
    var errorMessage: String? by remember { mutableStateOf(null) }

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
                    label = { Text(text = "Nombre del tectonic") },
                    value = nombre,
                    onValueChange = { nombre = it },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    label = { Text(text = "Sueldo del tecnico") },
                    value = sueldo,
                    onValueChange = { sueldo = it },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.padding(2.dp))
                errorMessage.let {
                    Text(text = it, color = Color.Red)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    OutlinedButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "nuevo boton"
                        )
                        Text(text = "Nuevo")

                    }
                    val scope = rememberCoroutineScope()
                    OutlinedButton(
                        onClick = {
                            if (nombre.isBlanck())
                                errorMessage = "El nombre no pued estar vacio."

                            scope.launch {
                                saveTecnico(
                                    TecnicoEntity(
                                        nombre = nombre,
                                        sueldo = sueldo
                                    )

                                )
                                nombre = ""
                                sueldo = ""
                            }


                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "boton guardar"
                        )
                        Text(text = "Guardar")
                        val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
                        val tecnicoList by tecnicoDb.tecnicoDao().getAll()
                            .collectionAsStateWithLifecycle(
                                initialValue = emptyList(),
                                lifecycleOwner = lifecycleOwner,
                                minActiveState = Lifecycle.State.STARTED
                            )
                        TecnicoListscreen(tecnicoList)
                    }
                }
            }

        }

        @Composable
        fun TecnicoListScreen(tecnicoList: List<TecnicoEntity>){
            Column(
                modifier = Modifier.fillMaxSize()
            ){
                Spacer(modifier = Modifier.height(32.dp))
                    Text("Lista de tecnicos")

               LazyColumn(
                   modifier = Modifier.fillMaxSize()
               ){
                   items(tecnicoList){
                       TecnicoRow(it)
                   }
                }
            }


            @Composable
            private fun TecnicoRow(it: TecnicoEntity)
            Row(
                verticalAlignment = Alignment.CenterVertically

            ){
                Text(modifier = Modifier.weight(1f), text = it.tecnicoId.toString())
                    Text(
                        modifier = Modifier.weight(1f),
                        text= it.nombre,
                        style = MaterialTheme.typography.headlineLarge
                )
                Text(modifier = Modifier.weight(1f),text = it.sueldo)
            }
            HorizontalDivider()
        }

        private suspend fun saveTecnico(tecnico:tecnicoEntity){
            tecnicoDb.tecnicoDao().save(tecnico)
        }

    }

}

}


//Entidad tecnico
@Entity(tableName = "Tecnicos")
data class TecnicoEntity(

    @PrimaryKey
    val tecnicoId: Int? = null,
    val nombre: String = "",
    val sueldo: Double

)


//Dao
@Dao

interface TecnicoDao{

  @Upsert
  suspend fun save(tecnicos:TecnicoEntity)

  @Query("""
      SELECT *
          FROM Tecnicos
          WHERE tecnicoId=:id
         limit 1
  """)

  suspend fun find(id:Int): TecnicoEntity

  @Delete
  suspend fun  delete (tecnicos: TecnicoEntity)

  @Query("SELECT * FROM Tecnicos")
  fun getAll():Flow<List<TecnicoEntity>>

}


//Base de Datos

@Database(
    entities = [TecnicoEntity::class],
    version = 1,
    exportSchema = false


)

abstract class TecnicoDb(): RoomDatabase(){
    abstract fun tecnicoDao(): TecnicoDao
}




