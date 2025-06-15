package edu.ucne.registrotecnico.presentation.retencion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnico.data.remote.Resource
import edu.ucne.registrotecnico.data.remote.dto.RetencionDTO
import edu.ucne.registrotecnico.data.repository.RetencionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RetencionViewModel @Inject constructor(
    private val retencionRepository: RetencionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RetencionUIState())
    val uiState = _uiState.asStateFlow()

    private val _load = MutableStateFlow(false)
    val load: StateFlow<Boolean> = _load

    init {
        getRetencion()
    }

    fun saveRetencion() {
        viewModelScope.launch {
            if (isValid()) {
                retencionRepository.save(_uiState.value.toEntity())
            }
        }
    }

    fun deleteRetencion(id: Int) {
        viewModelScope.launch {
            retencionRepository.delete(id)
        }
    }

    fun updateRetencion() {
        viewModelScope.launch {
            retencionRepository.update(
                _uiState.value.retencionId, RetencionDTO(
                    retencionId = _uiState.value.retencionId,
                    descripcion = _uiState.value.descripcion,
                    monto = _uiState.value.monto
                )
            )
        }
    }

    fun new() {
        _uiState.value = RetencionUIState()
    }

    fun onDescripcionChange(descripcion: String) {
        _uiState.update {
            it.copy(
                descripcion = descripcion,
                errorMessage = if (descripcion.isBlank()) "El campo de descripcion debe ser rellenado"
                else null
            )
        }
    }

    fun onMontoChange(monto: String) {
        _uiState.update {
            val montoDouble = monto.toDoubleOrNull()?:0.0
            it.copy(
                monto = montoDouble!!,
                errorMessage = when {
                    montoDouble <= 0.0 -> "El valor debe ser mayor a 0!"
                    else -> null
                }
            )
        }
    }


    fun findRetencion(retencionId: Int) {
        viewModelScope.launch {
            if (retencionId > 0) {
                val retencionDto = retencionRepository.find(retencionId)
                if (retencionDto.retencionId != 0) {
                    _uiState.update {
                        it.copy(
                            retencionId = retencionDto.retencionId,
                            descripcion = retencionDto.descripcion,
                            monto = retencionDto.monto
                        )
                    }
                }
            }
        }
    }

    fun getRetencion() {
        viewModelScope.launch {
            retencionRepository.getRetenciones().collectLatest { getting ->
                when (getting) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                retenciones = getting.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                errorMessage = getting.message ?: "Hubo un error al cargar la retencion",
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    fun isValid(): Boolean {
        return uiState.value.descripcion.isNotBlank() &&
                uiState.value.descripcion.isNotBlank() &&
                uiState.value.monto != 0.0

    }
}


fun RetencionUIState.toEntity() = RetencionDTO(
    retencionId = retencionId,
    descripcion = descripcion,
    monto = monto

)


