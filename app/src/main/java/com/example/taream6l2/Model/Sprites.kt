package com.example.taream6l2.Model

import com.google.gson.annotations.SerializedName

data class Sprites (
    @SerializedName("front_default")
    val frontDefault: String? //Para guardar la URL de la imagen del Pokemon
)