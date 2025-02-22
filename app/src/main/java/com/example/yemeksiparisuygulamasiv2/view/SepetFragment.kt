package com.example.yemeksiparisuygulamasiv2.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yemeksiparisuygulamasiv2.R
import com.example.yemeksiparisuygulamasiv2.adapter.SepetAdapter
import com.example.yemeksiparisuygulamasiv2.viewmodel.BaseViewModel
import com.example.yemeksiparisuygulamasiv2.viewmodel.ListViewModel
import com.example.yemeksiparisuygulamasiv2.viewmodel.SepetViewModel
import kotlinx.android.synthetic.main.fragment_sepet.*
import kotlinx.android.synthetic.main.item_sepet.*


class SepetFragment : Fragment() {


    private lateinit var viewModel : SepetViewModel
    private var sepetAdapter = SepetAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sepet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(SepetViewModel::class.java)
        viewModel.refreshData()

        SepetRV.layoutManager = LinearLayoutManager(context)
        SepetRV.adapter = sepetAdapter

        buttonSepet.setOnClickListener {
            val action = SepetFragmentDirections.actionSepetFragmentToListFragment()
            Navigation.findNavController(it).navigate(action)
        }

        swipeRefreshLayoutSepet.setOnRefreshListener {
            SepetRV.visibility = View.GONE
            sepetError.visibility = View.GONE
            sepetLoading.visibility = View.GONE
            viewModel.refreshData()
            swipeRefreshLayoutSepet.isRefreshing = false
        }
        observeLiveData()
    }


    private fun observeLiveData() {
        viewModel.sepet_yemekler.observe(viewLifecycleOwner, Observer {sepettekiler->
            sepettekiler?.let {
                SepetRV.visibility = View.VISIBLE
                //sepetAdapter.updateYemekList(sepettekiler.sepet_yemekler)
                sepetLoading.visibility = View.GONE
                sepetError.visibility = View.GONE
            }

            sepetAdapter = SepetAdapter(sepettekiler.sepet_yemekler)
            SepetRV.adapter = sepetAdapter
        })

        viewModel.sepeterror.observe(viewLifecycleOwner, Observer {error ->
            error?.let {
                if(it) {
                    sepetError.visibility = View.VISIBLE
                }else{
                    sepetError.visibility = View.GONE
                }
            }
        })
        viewModel.sepetloading.observe(viewLifecycleOwner, Observer {loading ->
            loading?.let {
                if(it) {
                    SepetRV.visibility = View.VISIBLE
                    sepetLoading.visibility = View.VISIBLE
                    sepetError.visibility = View.GONE
                }else{
                    sepetLoading.visibility = View.GONE
                }
            }
        })
    }
}