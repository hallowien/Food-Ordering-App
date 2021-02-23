package com.example.yemeksiparisuygulamasiv2.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.yemeksiparisuygulamasiv2.model.SepetYemek
import com.example.yemeksiparisuygulamasiv2.model.Yemek

@Database(entities = arrayOf(SepetYemek::class),version = 1)
abstract class SepetDatabase :RoomDatabase(){

    abstract fun sepetdao() : SepetDao

    // Singleton
    // statik, public
    companion object {

        @Volatile
        private var instance : SepetDatabase? = null
        private val lock = Any()

        // aynı anda tek bir threadde işlem yapılacak
        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            instance ?: makeDatabase(context).also {
            }
        }

        private fun makeDatabase(context: Context) = Room.databaseBuilder(
                context.applicationContext, SepetDatabase::class.java, "sepet_yemekler"
        ).build()

    }

}