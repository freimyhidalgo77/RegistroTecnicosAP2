package edu.ucne.registrotecnico.presentation.tecnico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucne.registrotecnico.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnico.data.repository.TecnicosRepository
import kotlinx.coroutines.launch

class TecnicosViewModel(
    private val tecnicosRepository: TecnicosRepository
): ViewModel() {
    fun saveTecnico(tecnico: TecnicoEntity) {
        viewModelScope.launch {
            tecnicosRepository.save(tecnico)
        }
    }

}