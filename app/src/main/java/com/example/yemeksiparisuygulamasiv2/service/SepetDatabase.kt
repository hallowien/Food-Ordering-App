package com.example.yemeksiparisuygulamasiv2.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.yemeksiparisuygulamasiv2.model.SepetYemek


@Database(entities = [SepetYemek::class],version = 1, exportSchema = false)
abstract class SepetDatabase : RoomDatabase(){

    abstract fun sepetdao() : SepetDao

    // Singleton
    // statik, public
    companion object {

        @Volatile
        private var INSTANCE: SepetDatabase? = null

        fun getDatabase(context: Context): SepetDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }

            synchronized(this){
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        SepetDatabase::class.java,
                        "sepet_yemekler"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}