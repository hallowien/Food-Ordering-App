package com.example.yemeksiparisuygulamasiv2.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.yemeksiparisuygulamasiv2.R
import com.example.yemeksiparisuygulamasiv2.databinding.ItemSepetBinding
import com.example.yemeksiparisuygulamasiv2.model.CRUDCevap
import com.example.yemeksiparisuygulamasiv2.model.SepetYemek
import com.example.yemeksiparisuygulamasiv2.service.YemekApiService
import com.example.yemeksiparisuygulamasiv2.util.downloadFromUrl
import com.example.yemeksiparisuygulamasiv2.util.placeholderProgressBar
import com.example.yemeksiparisuygulamasiv2.view.SepetFragmentDirections
import com.example.yemeksiparisuygulamasiv2.viewmodel.DetayViewModel
import com.example.yemeksiparisuygulamasiv2.viewmodel.SepetViewModel
import kotlinx.android.synthetic.main.item_sepet.*
import kotlinx.android.synthetic.main.item_sepet.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SepetAdapter(var List: List<SepetYemek>): RecyclerView.Adapter<SepetAdapter.SepetViewHolder>(), ItemClickListener{


    private lateinit var viewModel : SepetViewModel
    class SepetViewHolder(var view: ItemSepetBinding) : RecyclerView.ViewHolder(view.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SepetViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemSepetBinding>(inflater, R.layout.item_sepet, parent, false)
        return SepetViewHolder(view)
    }

    override fun onBindViewHolder(holder: SepetViewHolder, position: Int) {

        holder.view.yemeksepet = List[position]
        holder.view.listenersepet = this
        /*
        Log.e("sepet cevap", List[position].yemek_adi)
        holder.view.textSepetYemekAdi.text = List[position].yemek_adi
        holder.view.textSepetYemekFiyat.text = "${List[position].yemek_fiyat.toString()}TL"

        holder.view.sepet_urun_ekle.setOnClickListener {
            yemekEkle(List[position].yemek_id, List[position].yemek_adi, List[position].yemek_resim_adi, List[position].yemek_fiyat, 1)
        }

        holder.view.sepet_urun_sil.setOnClickListener {
            yemekSil(List[position].yemek_id)
        }

        holder.view.textSepetSiparisAdet.text = "${List[position].yemek_siparis_adet.toString()} adet"

        val url =  "http://kasimadalan.pe.hu/yemekler/resimler/${List[position].yemek_resim_adi}"
        holder.view.imageViewSepet.downloadFromUrl(url, placeholderProgressBar(holder.view.context))

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


    private fun updateYemekList(newYemekList: List<SepetYemek>){
        List = emptyList()
        this.List = newYemekList
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return List.size
    }

    override fun onItemClicked(v: View) {
        val id = v.sepetidtext.text.toString().toInt()
        val action = SepetFragmentDirections.actionSepetFragmentToDetayFragment(id)
        Navigation.findNavController(v).navigate(action)
    }

}