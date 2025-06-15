package edu.ucne.registrotecnico.presentation.retencion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RetencionScreen(viewModel: RetencionViewModel = hiltViewModel(), goBack: () -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Registrar retencion",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Atras")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF6200EE)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                label = { Text(text = "Descripción") },
                value = uiState.descripcion,
                onValueChange = viewModel::onDescripcionChange,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                label = { Text(text = "Monto") },
                value = uiState.monto.toString(),
                onValueChange = { viewModel.onMontoChange(it) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )


            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { viewModel.saveRetencion() },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Guardar")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Guardar")
                }

                Button(
                    onClick = { viewModel.new() },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Nuevo")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Nuevo")
                }
            }

            if (uiState.errorMessage != null) {
                Text(
                    text = "Registrar retencion",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

            }
        }
    }
}


