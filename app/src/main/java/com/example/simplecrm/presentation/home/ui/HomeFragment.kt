package com.example.simplecrm.presentation.home.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplecrm.R
import com.example.simplecrm.common.Resource
import com.example.simplecrm.databinding.FragmentHomeBinding
import com.example.simplecrm.domain.model.Customer
import com.example.simplecrm.presentation.home.adapter.CustomersAdapter
import com.example.simplecrm.presentation.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.addCustomerFab.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_addCustomerFragment)
        }

        GlobalScope.launch(Dispatchers.Main){
            homeViewModel.getAllCustomers()
            homeViewModel.getAllCustomersResponse.collect{ response->
                when(response){
                    is Resource.Error -> Log.e("HomeFragment" , response.message!!)
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        updateUI(response.data!!)
                    }
                }
            }
        }


        return binding.root
    }

    private fun updateUI(customers: List<Customer>) {
        binding.customersRv.setHasFixedSize(true)
        binding.customersRv.layoutManager = LinearLayoutManager(requireContext())

        val adapter = CustomersAdapter(customers , this)
        binding.customersRv.adapter = adapter
        adapter.setOnItemClickListener(object : CustomersAdapter.OnItemClickListener{
            override fun onItemClick(position: Int, customer: Customer) {
                val action = HomeFragmentDirections.actionNavigationHomeToCustomerDetailsFragment(customer)
                findNavController().navigate(action)
            }
        })
    }

}