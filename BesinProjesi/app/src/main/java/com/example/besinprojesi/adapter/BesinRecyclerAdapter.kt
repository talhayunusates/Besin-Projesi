package com.example.besinprojesi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.besinprojesi.databinding.BesinRecyclerRowBinding
import com.example.besinprojesi.model.Besin
import com.example.besinprojesi.util.gorselIndir
import com.example.besinprojesi.util.placeholderYap
import com.example.besinprojesi.view.BesinListeFragmentDirections


class BesinRecyclerAdapter(val besinListesi : ArrayList<Besin>) : RecyclerView.Adapter<BesinRecyclerAdapter.BesinViewHolder>() {

    class BesinViewHolder(var view: BesinRecyclerRowBinding) : RecyclerView.ViewHolder(view.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BesinViewHolder {
        val recyclerRowBinding: BesinRecyclerRowBinding = BesinRecyclerRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BesinViewHolder(recyclerRowBinding)
    }

    override fun getItemCount(): Int {
        return besinListesi.size
    }


    fun besinListesiniGuncelle(yeniBesinListesi: List<Besin>) {
        besinListesi.clear()
        besinListesi.addAll(yeniBesinListesi)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: BesinViewHolder, position: Int) {

        holder.view.isim.text = besinListesi.get(position).besinIsim
        holder.view.kalori.text = besinListesi.get(position).besinKalori

        holder.itemView.setOnClickListener {
            val action = BesinListeFragmentDirections.actionBesinListeFragmentToBesinDetayFragment(besinListesi.get(position).uuid)
            Navigation.findNavController(it).navigate(action)
        }
        holder.view.imageView.gorselIndir(besinListesi.get(position).besinGorsel, placeholderYap(holder.itemView.context))
    }
}

