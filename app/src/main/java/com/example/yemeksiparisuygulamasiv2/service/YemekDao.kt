package com.example.yemeksiparisuygulamasiv2.service


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.yemeksiparisuygulamasiv2.model.SepetYemek
import com.example.yemeksiparisuygulamasiv2.model.Yemek
import kotlinx.coroutines.flow.Flow


@Dao
interface YemekDao {

    @Insert
    suspend fun insertAll(vararg yemekler: Yemek): List<Long>


    @Query("SELECT * FROM yemekler")
    suspend fun getAllYemekler(): List<Yemek>

    @Query("SELECT * FROM yemekler WHERE yemek_id = :yemek_id")
    suspend fun getYemek(yemek_id :Int): Yemek

    @Query("DELETE FROM yemekler")
    suspend fun deleteAllYemekler()

    @Query("SELECT * FROM yemekler WHERE yemek_adi LIKE :search_query")
    fun searchYemek(search_query: String): Flow<List<Yemek>>


}