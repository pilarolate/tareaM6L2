package com.example.taream6l2.Api

import retrofit2.Call
import com.example.taream6l2.Model.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApiService {
    @GET("pokemon/{name}") //arma una URL
    fun getPokemon(@Path("name") name: String): Call<PokemonResponse>
}