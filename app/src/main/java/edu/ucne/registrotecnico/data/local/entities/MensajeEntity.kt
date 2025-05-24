package edu.ucne.registrotecnico.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Entity(tableName="Mensajes")
data class MensajeEntity(
    @PrimaryKey(autoGenerate = true)
    val mensajeId: Int? = null,
    val tecnicoId: Int? = null,
    val remitente:String = "",
    val descripcion: String = "",
    val fecha: Date?,
    val rol: String


)
