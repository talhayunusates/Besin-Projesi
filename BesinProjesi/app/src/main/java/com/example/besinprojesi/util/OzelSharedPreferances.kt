package com.example.besinprojesi.util

import android.content.Context
import android.content.SharedPreferences

class OzelSharedPreferances {
    companion object {

        private val ZAMAN = "zaman"
        private var sharedPreferences : SharedPreferences? = null

        @Volatile private var instance : OzelSharedPreferances? = null
        private val lock = Any()
        operator fun invoke(context: Context) : OzelSharedPreferances = instance ?: synchronized(lock) {
            instance ?: ozelSharedPreferencesYap(context).also {
                instance = it
            }
        }

        private fun ozelSharedPreferencesYap(context: Context): OzelSharedPreferances{
            sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
            return OzelSharedPreferances()
        }
    }

    fun zamaniKaydet(zaman: Long){
        sharedPreferences?.edit()?.putLong(ZAMAN,zaman)?.apply()
    }

    fun zamaniAl() = sharedPreferences?.getLong(ZAMAN,0)

}