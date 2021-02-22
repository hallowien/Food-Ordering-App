package com.example.yemeksiparisuygulamasiv2.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.yemeksiparisuygulamasiv2.model.SepetYemek
import com.example.yemeksiparisuygulamasiv2.model.Yemek
import kotlinx.coroutines.flow.Flow

@Dao
interface SepetDao {

    @Insert(onConflict =  OnConflictStrategy.IGNORE)
    suspend fun insertAllToSepet(vararg sepet_yemekler: SepetYemek): List<Long>

    @Query("SELECT * FROM sepet_yemekler")
    fun getAllSepetYemekler(): List<SepetYemek>

    @Query("DELETE FROM sepet_yemekler")
    suspend fun deleteAllSepetYemekler()


    // @Query("SELECT * FROM sepet_yemekler WHERE yemek_adi LIKE:searchQuery")
    //fun searchDatabase(searchQuery: String): Flow<List<SepetYemek>>
}