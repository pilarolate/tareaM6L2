package com.example.taream6l2

import com.example.taream6l2.Model.PokemonResponse

interface PokemonDao {
    fun insertPokemon(pokemon: PokemonResponse)
}