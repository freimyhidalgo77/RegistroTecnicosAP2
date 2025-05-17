package edu.ucne.registrotecnico.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Prioridades")
data class PrioridadEntity(
    @PrimaryKey(autoGenerate = true)
    val prioridadId: Int? = null,
    val descripcion: String = "",

)
