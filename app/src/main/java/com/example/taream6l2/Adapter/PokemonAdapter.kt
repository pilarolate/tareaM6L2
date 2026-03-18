package com.example.taream6l2.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.taream6l2.R
import androidx.recyclerview.widget.RecyclerView
import com.example.taream6l2.Model.PokemonResponse
import com.squareup.picasso.Picasso

class PokemonAdapter : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    private var pokemon: PokemonResponse? = null

    fun setPokemon(newPokemon: PokemonResponse){ //Guarda el Pokemon recibido y refresca la lista
        pokemon = newPokemon
        notifyDataSetChanged()
    }
    class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imageViewPokemon: ImageView = itemView.findViewById(R.id.imageViewPokemon)
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        val textViewHeight: TextView = itemView.findViewById(R.id.textViewHeight)
        val textViewWeight: TextView = itemView.findViewById(R.id.textViewWeight)
    }

    //Infla el XML item_pokemon
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pokemon, parent, false)
        return PokemonViewHolder(view)
    }

    //Pone los datos en pantalla
    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        pokemon?.let {
            holder.textViewName.text = it.name.replaceFirstChar { c -> c.uppercase() }
            holder.textViewHeight.text = "Altura: ${it.height}"
            holder.textViewWeight.text = "Peso: ${it.weight}"

            //Descarga y muestra la imagen automáticamente
            Picasso.get()
                .load(it.sprites.frontDefault)
                .into(holder.imageViewPokemon)
        }
    }

    //Devuelve 1 porque en este ejercicio mostramos un solo Pokemon
    override fun getItemCount(): Int {
        return if (pokemon == null) 0 else 1
    }
}