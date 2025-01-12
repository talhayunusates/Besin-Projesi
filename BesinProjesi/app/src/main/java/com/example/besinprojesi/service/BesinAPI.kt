package com.example.besinprojesi.service

import com.example.besinprojesi.model.Besin
import retrofit2.http.GET

interface BesinAPI {
    @GET("atilsamancioglu/BTK20-JSONVeriSeti/master/besinler.json")
    suspend fun getBesin() : List<Besin>
}