package com.example.taream6l2

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.taream6l2.Api.PokeApiService
import com.example.taream6l2.Model.PokemonResponse
import com.example.taream6l2.Model.Sprites
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.mockito.Mockito.verifyNoInteractions

class PokemonRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockApiService: PokeApiService

    @Mock
    private lateinit var mockPokemonDao: PokemonDao

    @Mock
    private lateinit var mockCall: Call<PokemonResponse>

    private lateinit var repository: PokemonRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = PokemonRepository(mockApiService, mockPokemonDao)
    }

    @Test
    fun getPokemon_successfulResponse_dataStoredLocally() {
        val pokemon = PokemonResponse(
            name = "pikachu",
            height = 4,
            weight = 60,
            sprites = Sprites("https://pokeapi.co/media/sprites/pokemon/25.png")
        )

        `when`(mockApiService.getPokemon("pikachu")).thenReturn(mockCall)

        @Suppress("UNCHECKED_CAST")
        val callbackCaptor =
            ArgumentCaptor.forClass(Callback::class.java) as ArgumentCaptor<Callback<PokemonResponse>>

        val liveData = repository.getPokemon("pikachu")

        verify(mockCall).enqueue(callbackCaptor.capture())

        callbackCaptor.value.onResponse(mockCall, Response.success(pokemon))

        verify(mockApiService).getPokemon("pikachu")
        verify(mockPokemonDao).insertPokemon(pokemon)
        assertEquals(pokemon, liveData.value)
    }

    @Test
    fun getPokemon_apiError_noDataStoredLocally() {
        `when`(mockApiService.getPokemon("pikachu")).thenReturn(mockCall)

        @Suppress("UNCHECKED_CAST")
        val callbackCaptor =
            ArgumentCaptor.forClass(Callback::class.java) as ArgumentCaptor<Callback<PokemonResponse>>

        val liveData = repository.getPokemon("pikachu")

        verify(mockCall).enqueue(callbackCaptor.capture())

        callbackCaptor.value.onFailure(mockCall, Throwable("Error de conexión"))

        verify(mockApiService).getPokemon("pikachu")
        verifyNoInteractions(mockPokemonDao)
        assertNull(liveData.value)
    }
}