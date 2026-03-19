package com.example.taream6l2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taream6l2.Adapter.PokemonAdapter
import com.example.taream6l2.Api.PokeApiService
import com.example.taream6l2.Model.PokemonResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerViewPokemon: RecyclerView
    private lateinit var pokemonAdapter: PokemonAdapter
    private lateinit var editTextPokemonName: EditText
    private lateinit var buttonSearch: Button
    private lateinit var apiService: PokeApiService
    private lateinit var repository: PokemonRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerViewPokemon = findViewById(R.id.recyclerViewPokemon)
        editTextPokemonName = findViewById(R.id.editTextPokemonName)
        buttonSearch = findViewById(R.id.buttonSearch)

        pokemonAdapter = PokemonAdapter()
        recyclerViewPokemon.layoutManager = LinearLayoutManager(this)
        recyclerViewPokemon.adapter = pokemonAdapter

        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(PokeApiService::class.java)

        val fakeDao = object : PokemonDao {
            override fun insertPokemon(pokemon: PokemonResponse) {
            }
        }

        repository = PokemonRepository(apiService, fakeDao)

        buscarPokemon("bulbasaur")

        buttonSearch.setOnClickListener {
            val nombrePokemon = editTextPokemonName.text.toString().trim().lowercase()

            if (nombrePokemon.isEmpty()) {
                Toast.makeText(this, "Escribe un nombre de Pokémon", Toast.LENGTH_SHORT).show()
            } else {
                buscarPokemon(nombrePokemon)
            }
        }
    }

    private fun buscarPokemon(nombre: String) {
        repository.getPokemon(nombre).observe(this) { pokemon ->
            if (pokemon != null) {
                pokemonAdapter.setPokemon(pokemon)
            } else {
                Toast.makeText(
                    this,
                    "Pokémon no encontrado o error de conexión",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}