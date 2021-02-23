package com.example.yemeksiparisuygulamasiv2.service

import androidx.room.*
import com.example.yemeksiparisuygulamasiv2.model.SepetYemek
import com.example.yemeksiparisuygulamasiv2.model.Yemek
import kotlinx.coroutines.flow.Flow

@Dao
interface SepetDao {

    @Insert(onConflict =  OnConflictStrategy.IGNORE)
    suspend fun insertAllToSepet(vararg sepet_yemekler: SepetYemek): List<Long>

    @Insert(onConflict =  OnConflictStrategy.IGNORE)
    fun insertToSepet(yemek: SepetYemek)

    @Delete
    fun deleteFromSepet(yemek: SepetYemek)

    @Query("SELECT * FROM sepet_yemekler")
    suspend fun getAllSepetYemekler(): List<SepetYemek>

    @Query("DELETE FROM sepet_yemekler")
    suspend fun deleteAllSepetYemekler()


    // @Query("SELECT * FROM sepet_yemekler WHERE yemek_adi LIKE:searchQuery")
    //fun searchDatabase(searchQuery: String): Flow<List<SepetYemek>>
}