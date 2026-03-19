package com.example.taream6l2

import com.example.taream6l2.Model.PokemonResponse
import com.example.taream6l2.Model.Sprites
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TestPokemonResponse {

    private lateinit var pokemon: PokemonResponse

    @Before
    fun setup(){
        //Creamos la instancia para test
        pokemon = PokemonResponse(
            name = "charmander",
            height = 6,
            weight = 85,
            sprites = Sprites(
                frontDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/4.png"
            )
        )
    }

    @Test
    fun comprobarNombrePokemon() {
        assertEquals("charmander", pokemon.name)
    }

    @Test
    fun comprobarAlturaPokemon() {
        assertEquals(6, pokemon.height)
    }

    @Test
    fun comprobarPesoPokemon() {
        assertEquals(85, pokemon.weight)
    }

    @Test
    fun comprobarSpritePokemon() {
        assertEquals(
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/4.png",
            pokemon.sprites.frontDefault
        )
    }
}