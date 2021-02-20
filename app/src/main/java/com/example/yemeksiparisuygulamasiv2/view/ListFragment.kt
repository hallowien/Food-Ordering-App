package com.example.yemeksiparisuygulamasiv2.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yemeksiparisuygulamasiv2.R
import com.example.yemeksiparisuygulamasiv2.adapter.YemekAdapter
import com.example.yemeksiparisuygulamasiv2.model.YemekCevap
import com.example.yemeksiparisuygulamasiv2.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_list.*


class ListFragment : Fragment() {

    private lateinit var viewModel : ListViewModel
    private var yemekAdapter = YemekAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
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
            yemekError.visibility = View.GONE
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

                //YemekRV.adapter = yemekAdapter
                //yemekAdapter.updateYemekList(yemekler.yemekler)
                yemekLoading.visibility = View.GONE
                yemekError.visibility = View.GONE
            }

            yemekAdapter = YemekAdapter(yemekler.yemekler)
            YemekRV.adapter = yemekAdapter
        })

        viewModel.yemekerror.observe(viewLifecycleOwner, Observer {error ->
            error?.let {
                if(it) {
                    yemekError.visibility = View.VISIBLE
                }else{
                    yemekError.visibility = View.GONE
                }
            }
        })
        viewModel.yemekloading.observe(viewLifecycleOwner, Observer {loading ->
            loading?.let {
                if(it) {
                    YemekRV.visibility = View.VISIBLE
                    yemekLoading.visibility = View.VISIBLE
                    yemekError.visibility = View.GONE
                }else{
                    yemekLoading.visibility = View.GONE
                }
            }
        })

    }

}