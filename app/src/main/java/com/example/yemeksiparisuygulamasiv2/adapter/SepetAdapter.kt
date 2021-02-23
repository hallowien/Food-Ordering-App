package com.example.yemeksiparisuygulamasiv2.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.yemeksiparisuygulamasiv2.R
import com.example.yemeksiparisuygulamasiv2.databinding.ItemSepetBinding
import com.example.yemeksiparisuygulamasiv2.model.CRUDCevap
import com.example.yemeksiparisuygulamasiv2.model.SepetYemek
import com.example.yemeksiparisuygulamasiv2.service.YemekApiService
import com.example.yemeksiparisuygulamasiv2.view.SepetFragment
import com.example.yemeksiparisuygulamasiv2.viewmodel.SepetViewModel
import kotlinx.android.synthetic.main.item_sepet.view.*
import com.example.yemeksiparisuygulamasiv2.view.SepetFragmentDirections
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.android.synthetic.main.item_sepet.view.textSepetYemekFiyat as textSepetYemekFiyat1


class SepetAdapter(var List: List<SepetYemek>): RecyclerView.Adapter<SepetAdapter.SepetViewHolder>(){

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
        val toplam = List[position].yemek_fiyat * List[position].yemek_siparis_adet
        holder.view.textViewtoplam.text = "${toplam.toString()}₺"
        //holder.view.textSepetSiparisAdet.text = "${List[position].yemek_siparis_adet.toString()} adet"
        //val fiyat = holder.view.root.textSepetYemekFiyat1.text.toString().toInt()
        //val adet = holder.view.root.textSepetSiparisAdet.text.toString().toInt()
        //val toplam = (adet * fiyat).toString()
        //holder.view.textViewtoplam.text = toplam

        holder.view.root.setOnClickListener {
            val action = SepetFragmentDirections.actionSepetFragmentToDetayFragment(List[position].yemek_id)
            Navigation.findNavController(it).navigate(action)
        }

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
        notifyDataSetChanged()
    }

    private fun yemekEkle(yemek_id: Int, yemek_adi: String, yemek_resim_adi: String, yemek_fiyat: Int, yemek_siparis_adet: Int){

       // viewModel = ViewModelProviders.of(this).get(SepetViewModel::class.java)

        val yeniYemek = SepetYemek(yemek_id, yemek_adi, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet)

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

    fun updateYemekList(newYemekList: List<SepetYemek>){
        List = emptyList()
        this.List = newYemekList
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return List.size
    }


}