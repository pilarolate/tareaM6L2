package com.example.taream6l2

import com.example.taream6l2.Api.PokeApiService
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PokeApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: PokeApiService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokeApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getPokemon_returnsPokemonSuccessfully() {
        val body = """
            {
              "name": "pikachu",
              "height": 4,
              "weight": 60,
              "sprites": {
                "front_default": "https://pokeapi.co/media/sprites/pokemon/25.png"
              }
            }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(body)
        )

        val response = apiService.getPokemon("pikachu").execute()

        assertTrue(response.isSuccessful)
        assertEquals("pikachu", response.body()?.name)
        assertEquals(4, response.body()?.height)
        assertEquals(60, response.body()?.weight)

        val request = mockWebServer.takeRequest()
        assertEquals("/pokemon/pikachu", request.path)
    }

    @Test
    fun getPokemon_returns404WhenPokemonDoesNotExist() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(404)
                .setBody("""{"detail":"Not found"}""")
        )

        val response = apiService.getPokemon("pokemoninventado").execute()

        assertEquals(404, response.code())

        val request = mockWebServer.takeRequest()
        assertEquals("/pokemon/pokemoninventado", request.path)
    }
}