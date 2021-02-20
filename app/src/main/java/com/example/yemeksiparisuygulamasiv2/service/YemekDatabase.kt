package com.example.yemeksiparisuygulamasiv2.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.yemeksiparisuygulamasiv2.model.Yemek
import com.example.yemeksiparisuygulamasiv2.model.YemekCevap

@Database(entities = arrayOf(Yemek::class),version = 1)
abstract class YemekDatabase :RoomDatabase(){

    abstract fun yemekdao() : YemekDao

    // Singleton
    // statik, public
    companion object {

        @Volatile
        private var instance : YemekDatabase? = null
        private val lock = Any()

        // aynı anda tek bir threadde işlem yapılacak
        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            instance ?: makeDatabase(context).also {

            }
        }

        private fun makeDatabase(context: Context) = Room.databaseBuilder(
                context.applicationContext, YemekDatabase::class.java, "yemekler"
        ).build()

    }

}