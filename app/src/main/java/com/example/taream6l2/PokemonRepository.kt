package com.example.taream6l2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.taream6l2.Api.PokeApiService
import com.example.taream6l2.Model.PokemonResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonRepository (
    private val apiService: PokeApiService,
    private val pokemonDao: PokemonDao
){
    fun getPokemon(name: String): LiveData<PokemonResponse?>{
        val pokemonLiveData = MutableLiveData<PokemonResponse?>()

        val call = apiService.getPokemon(name)
        call.enqueue(object : Callback<PokemonResponse>{
            override fun onResponse(
                call: Call<PokemonResponse>,
                response: Response<PokemonResponse>
            ){
                if(response.isSuccessful && response.body() != null){
                    val pokemon =response.body()!!
                    pokemonDao.insertPokemon(pokemon)
                    pokemonLiveData.value = pokemon
                } else {
                    pokemonLiveData.value = null
                }
            }
            override fun onFailure(call: Call<PokemonResponse>, t: Throwable){
                pokemonLiveData.value = null
            }
        })
        return pokemonLiveData
    }
}