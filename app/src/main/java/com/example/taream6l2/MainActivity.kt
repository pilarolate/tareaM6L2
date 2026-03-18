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
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    //Referencias visuales: Guardamos referencias al RecyclerView y al Adapter
    private lateinit var recyclerViewPokemon: RecyclerView
    private lateinit var pokemonAdapter: PokemonAdapter
    private lateinit var editTextPokemonName: EditText
    private lateinit var buttonSearch: Button
    private lateinit var apiService: PokeApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Enlazar con el XML: Le decimos a Android qué layout usar y obtenemos el RecyclerView del XML
        setContentView(R.layout.activity_main)

        recyclerViewPokemon = findViewById(R.id.recyclerViewPokemon)
        editTextPokemonName = findViewById(R.id.editTextPokemonName)
        buttonSearch = findViewById(R.id.buttonSearch)

        //Preparar RecyclerView
        pokemonAdapter = PokemonAdapter() //Coneecta el RecyclerView con los datos

        //LinearLayoutManager(this) hace que los items se muestren en una columna (vertical)
        recyclerViewPokemon.layoutManager = LinearLayoutManager(this)
        recyclerViewPokemon.adapter = pokemonAdapter

        //Crear Retrofit
        //El conversor Gson que transforma el JSON en objetos Kotlin
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/") //Configura la URL base
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //Crear servicio de API
        //Retrofit crea una implementación real de la interfaz que se define
        apiService = retrofit.create(PokeApiService::class.java)

        //Esto hace la consulta
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
        val call: Call<PokemonResponse> = apiService.getPokemon(nombre)

        call.enqueue(object : Callback<PokemonResponse> {
            override fun onResponse(
                call: Call<PokemonResponse>,
                response: Response<PokemonResponse>
            ) {
                if (response.isSuccessful) {
                    val pokemon = response.body()
                    if (pokemon != null) {
                        pokemonAdapter.setPokemon(pokemon)
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Pokémon no encontrado",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<PokemonResponse>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}