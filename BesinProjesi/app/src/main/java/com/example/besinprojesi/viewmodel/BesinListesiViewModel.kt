package com.example.besinprojesi.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.besinprojesi.model.Besin
import com.example.besinprojesi.roomdb.BesinDatabase
import com.example.besinprojesi.service.BesinAPIServis
import com.example.besinprojesi.util.OzelSharedPreferances
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BesinListesiViewModel(application: Application) : AndroidViewModel(application) {
    val besinler = MutableLiveData<List<Besin>>()
    val besinHataMesaji = MutableLiveData<Boolean>()
    val besinYukleniyor = MutableLiveData<Boolean>()
    private var guncellemeZamani = 10 * 60 * 1000 * 1000 * 1000L

    private val besinApiServis = BesinAPIServis()
    private val ozelSharedPreferences = OzelSharedPreferances(getApplication())
    private val TAG = "BesinListesiViewModel"


    fun refreshData() {

        val kaydedilmeZamani = ozelSharedPreferences.zamaniAl()
        if (kaydedilmeZamani != null && kaydedilmeZamani != 0L && System.nanoTime() - kaydedilmeZamani < guncellemeZamani){
            //Sqlite'tan çek
            verileriSQLitetanAl()
        } else {
            verileriInternettenAl()
        }
    }

    fun refreshFromInternet(){
        verileriInternettenAl()
    }

    private fun verileriSQLitetanAl(){
        besinYukleniyor.value = true

        viewModelScope.launch {

            val besinListesi = BesinDatabase(getApplication()).besinDao().getAllBesin()
            besinleriGoster(besinListesi)
            Toast.makeText(getApplication(),"Besinleri Room'dan Aldık",Toast.LENGTH_LONG).show()

        }

    }

    private fun verileriInternettenAl(){
        besinYukleniyor.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val besinler = besinApiServis.getData()
            withContext(Dispatchers.Main) {
                besinYukleniyor.value = false
                sqliteSakla(besinler)
                Toast.makeText(getApplication(),"Besinleri Internet'ten Aldık",Toast.LENGTH_LONG).show()
            }
        }


    }

    private fun besinleriGoster(besinlerListesi : List<Besin>){
        besinler.value = besinlerListesi
        Log.i(TAG,"besinlerListesi : $besinlerListesi")
        besinHataMesaji.value = false
        besinYukleniyor.value = false
    }

    private fun sqliteSakla(besinListesi: List<Besin>){

        viewModelScope.launch {

            val dao = BesinDatabase(getApplication()).besinDao()
            dao.deleteAllBesin()
            val uuidListesi = dao.insertAll(*besinListesi.toTypedArray())
            var i = 0
            while (i < besinListesi.size){
                besinListesi[i].uuid = uuidListesi[i].toInt()
                i = i + 1
            }
            besinleriGoster(besinListesi)
        }

        ozelSharedPreferences.zamaniKaydet(System.nanoTime())

    }

}

