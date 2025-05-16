package edu.ucne.registrotecnico

import android.annotation.SuppressLint
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
import edu.ucne.registrotecnico.data.local.database.TecnicoDb
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnico.presentation.navigation.AppNavigation
import edu.ucne.registrotecnico.presentation.tecnico.TecnicoScreen
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
                AppNavigation(tecnicoDb)
            }
        }
    }
}











