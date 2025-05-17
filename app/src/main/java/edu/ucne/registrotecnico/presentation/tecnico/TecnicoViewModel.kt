package edu.ucne.registrotecnico.presentation.tecnico

import androidx.benchmark.perfetto.UiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnico.data.repository.TecnicosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TecnicosViewModel(
    private val tecnicosRepository: TecnicosRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TecnicoEntity(0, "", 0.0))
    val uiState = _uiState.asStateFlow()

    fun saveTecnico(tecnico: TecnicoEntity) {
        viewModelScope.launch {
            tecnicosRepository.save(tecnico)
        }
    }


    fun deleteTecnico(tecnico: TecnicoEntity) {
        viewModelScope.launch {
            tecnicosRepository.delete(_uiState.value)
        }
    }

    fun find(id: Int) {
        viewModelScope.launch {
            val tecnico = tecnicosRepository.find(id)
            if (tecnico != null) {
                _uiState.value = tecnico
            }
        }
    }
}