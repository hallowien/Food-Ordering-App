package com.example.yemeksiparisuygulamasiv2.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.manager.SupportRequestManagerFragment
import com.example.yemeksiparisuygulamasiv2.R
import com.example.yemeksiparisuygulamasiv2.adapter.YemekAdapter
import com.example.yemeksiparisuygulamasiv2.model.YemekCevap
import com.example.yemeksiparisuygulamasiv2.service.YemekApiService
import com.example.yemeksiparisuygulamasiv2.service.YemekDatabase
import com.example.yemeksiparisuygulamasiv2.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListFragment : Fragment(), SearchView.OnQueryTextListener{

    private lateinit var viewModel : ListViewModel
    private var yemekAdapter = YemekAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.menu_sepet){
            val view = fragment.view
            val action = ListFragmentDirections.actionListFragmentToSepetFragment()
            Navigation.findNavController(view!!).navigate(action)
        }
        return super.onOptionsItemSelected(item)
    }


    fun searchApiData(searchQuery: String) {
        val ydi = YemekApiService.getYemekInterface()
        ydi.yemekAra(searchQuery).enqueue(object : Callback<YemekCevap> {
            override fun onFailure(call: Call<YemekCevap>, t: Throwable) {
            }
            override fun onResponse(call: Call<YemekCevap>, response: Response<YemekCevap>) {
                val yemekList = response.body()?.yemekler
                Log.e("yemek", "yemek")
                yemekList?.let {
                    yemekAdapter.setData(yemekList)
                }
            }
        })
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null) {
            searchApiData(query)
        }
        return true
    }


    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText != null) {
            searchApiData(newText)
        }
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.refreshData()

        YemekRV.layoutManager = LinearLayoutManager(context)
        YemekRV.adapter = yemekAdapter

        /*

        button.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToDetayFragment()
            Navigation.findNavController(it).navigate(action)
        }

         */
        swipeRefreshLayout.setOnRefreshListener {
            YemekRV.visibility = View.GONE
            //yemekError.visibility = View.GONE
            yemekLoading.visibility = View.VISIBLE
            viewModel.refreshData()
            swipeRefreshLayout.isRefreshing = false

        }
        observeLiveData()
    }
    private fun observeLiveData() {
        viewModel.yemekler.observe(viewLifecycleOwner, Observer {yemekler ->
            yemekler?.let {
                Log.e("cevap", yemekler.yemekler[3].yemek_adi)
                YemekRV.visibility = View.VISIBLE
                yemekLoading.visibility = View.GONE
                //yemekError.visibility = View.GONE
            }

            yemekAdapter = YemekAdapter(yemekler.yemekler)
            YemekRV.adapter = yemekAdapter
        })

        /*viewModel.yemekerror.observe(viewLifecycleOwner, Observer {error ->
            error?.let {
                if(it) {
                    yemekError.visibility = View.VISIBLE
                }else{
                    yemekError.visibility = View.GONE
                }
            }
        })

         */
        viewModel.yemekloading.observe(viewLifecycleOwner, Observer {loading ->
            loading?.let {
                if(it) {
                    YemekRV.visibility = View.VISIBLE
                    yemekLoading.visibility = View.VISIBLE
                    //yemekError.visibility = View.GONE
                }else{
                    yemekLoading.visibility = View.GONE
                }
            }
        })

    }
}