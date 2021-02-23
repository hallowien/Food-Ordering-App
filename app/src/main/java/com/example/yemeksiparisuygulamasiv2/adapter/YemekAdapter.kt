package com.example.yemeksiparisuygulamasiv2.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.yemeksiparisuygulamasiv2.R
import com.example.yemeksiparisuygulamasiv2.databinding.ItemYemekBinding
import com.example.yemeksiparisuygulamasiv2.model.CRUDCevap
import com.example.yemeksiparisuygulamasiv2.model.Yemek
import com.example.yemeksiparisuygulamasiv2.service.YemekApiService
import com.example.yemeksiparisuygulamasiv2.util.downloadFromUrl
import com.example.yemeksiparisuygulamasiv2.util.placeholderProgressBar
import com.example.yemeksiparisuygulamasiv2.view.ListFragmentDirections
import kotlinx.android.synthetic.main.item_yemek.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class YemekAdapter (var yemekList: List<Yemek>): RecyclerView.Adapter<YemekAdapter.YemekViewHolder>(){

    class YemekViewHolder(val view: ItemYemekBinding) :RecyclerView.ViewHolder(view.root){

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YemekViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemYemekBinding>(inflater, R.layout.item_yemek, parent, false)
        return YemekViewHolder(view)
    }

    override fun onBindViewHolder(holder: YemekViewHolder, position: Int) {

        holder.view.yemek = yemekList[position]
        holder.view.root.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToDetayFragment(yemekList[position].yemek_id)
            Navigation.findNavController(it).navigate(action)
        }

        holder.view.root.sepet_urun_ekle_list.setOnClickListener {
            yemekEkle(yemekList[position].yemek_id, yemekList[position].yemek_adi, yemekList[position].yemek_resim_adi, yemekList[position].yemek_fiyat, 1)
        }

        Log.e("cevap", yemekList[position].yemek_adi)
        //holder.view.textViewYemekAdi.text = yemekList[position].yemek_adi
        //holder.view.textViewYemekFiyat.text = yemekList[position].yemek_fiyat.toString()

        /*holder.view.setOnClickListener{
            val action = ListFragmentDirections.actionListFragmentToDetayFragment(yemekList[position].yemek_id)

            Navigation.findNavController(it).navigate(action)
        }

        val url =  "http://kasimadalan.pe.hu/yemekler/resimler/${yemekList[position].yemek_resim_adi}"
        holder.view.imageView2.downloadFromUrl(url, placeholderProgressBar(holder))

         */

        }
    private fun yemekSil(yemek_id: Int){

        val ydi = YemekApiService.getYemekInterface()
        ydi.sepettenSil(yemek_id).enqueue(object : Callback<CRUDCevap> {
            override fun onFailure(call: Call<CRUDCevap>, t: Throwable) {
            }
            override fun onResponse(call: Call<CRUDCevap>, response: Response<CRUDCevap>) {
                Log.e("başarılı silme", response.body()?.success.toString())
                Log.e("mesaj sil", response.body()?.message.toString())
            }
        })

    }

    private fun yemekEkle(yemek_id: Int, yemek_adi: String, yemek_resim_adi: String, yemek_fiyat: Int, yemek_siparis_adet: Int){

        val ydi = YemekApiService.getYemekInterface()
        ydi.sepeteEkle(yemek_id, yemek_adi.toString(),  yemek_resim_adi.toString(), yemek_fiyat, yemek_siparis_adet).enqueue(object : Callback<CRUDCevap> {
            override fun onFailure(call: Call<CRUDCevap>, t: Throwable) {
            }
            override fun onResponse(call: Call<CRUDCevap>, response: Response<CRUDCevap>) {
                Log.e("başarı", response.body()?.success.toString())
                Log.e("mesaj", response.body()?.message.toString())
            }
        })
    }

    fun updateYemekList(newYemekList: List<Yemek>){
        yemekList = emptyList()
        this.yemekList = newYemekList
        notifyDataSetChanged()

    }

    fun setData(newData: List<Yemek>){
        yemekList = newData
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return yemekList.size
    }


}