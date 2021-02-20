package com.example.yemeksiparisuygulamasiv2.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.yemeksiparisuygulamasiv2.R
import com.example.yemeksiparisuygulamasiv2.model.Yemek
import com.example.yemeksiparisuygulamasiv2.model.YemekCevap
import com.example.yemeksiparisuygulamasiv2.util.downloadFromUrl
import com.example.yemeksiparisuygulamasiv2.util.placeholderProgressBar
import com.example.yemeksiparisuygulamasiv2.view.ListFragmentDirections
import kotlinx.android.synthetic.main.fragment_detay.view.*
import kotlinx.android.synthetic.main.item_yemek.view.*

class YemekAdapter (var yemekList: List<Yemek>): RecyclerView.Adapter<YemekAdapter.YemekViewHolder>(){

    class YemekViewHolder(var view: View) :RecyclerView.ViewHolder(view){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YemekViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_yemek, parent, false)
        return YemekViewHolder(view)
    }
    override fun onBindViewHolder(holder: YemekViewHolder, position: Int) {

        Log.e("cevap", yemekList[position].yemek_adi)
        holder.view.textViewYemekAdi.text = yemekList[position].yemek_adi
        holder.view.textViewYemekFiyat.text = yemekList[position].yemek_fiyat.toString()

        holder.view.setOnClickListener{
            val action = ListFragmentDirections.actionListFragmentToDetayFragment(yemekList[position].yemek_id)

            Navigation.findNavController(it).navigate(action)
        }

        val url =  "http://kasimadalan.pe.hu/yemekler/resimler/${yemekList[position].yemek_resim_adi}"
        holder.view.imageView2.downloadFromUrl(url, placeholderProgressBar(holder.view.context))
    }

    fun updateYemekList(newYemekList: List<Yemek>){
        yemekList = emptyList()
        this.yemekList = newYemekList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return yemekList.size
    }


}