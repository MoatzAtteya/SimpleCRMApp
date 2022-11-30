package com.example.simplecrm.presentation.calls.ui

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplecrm.common.Resource
import com.example.simplecrm.databinding.FragmentCallsBinding
import com.example.simplecrm.domain.model.Customer
import com.example.simplecrm.presentation.calls.viewmodel.CallsViewModel
import com.example.simplecrm.presentation.calls.adapter.CallCustomersAdapter
import com.marsad.stylishdialogs.StylishAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CallsFragment : Fragment() {

    lateinit var binding: FragmentCallsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCallsBinding.inflate(inflater, container, false)
        val callsViewModel =
            ViewModelProvider(this)[CallsViewModel::class.java]

        GlobalScope.launch(Dispatchers.Main) {
            callsViewModel.getCallCustomers()
            callsViewModel.getCallCustomersResponse.collect { response ->
                when (response) {
                    is Resource.Error -> Toast.makeText(
                        requireContext(),
                        response.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    is Resource.Loading -> {}
                    is Resource.Success -> updateUi(response.data)
                }
            }
        }
        return binding.root
    }

    private fun updateUi(customers: List<Customer>?) {
        val adapter = CallCustomersAdapter(customers!!, this)
        binding.callsCustomerRv.setHasFixedSize(true)
        binding.callsCustomerRv.layoutManager = LinearLayoutManager(requireContext())
        binding.callsCustomerRv.adapter = adapter

        adapter.setOnItemClickListener(object : CallCustomersAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, customer: Customer) {
                val alertDialog = StylishAlertDialog(requireContext(), StylishAlertDialog.PROGRESS)
                alertDialog.titleText = "Starting a Call..."
                alertDialog.cancellable = false
                alertDialog.show()
                Handler().postDelayed({
                    alertDialog.dismissWithAnimation()
                    val action =
                        CallsFragmentDirections.actionNavigationCallsToCallingFragment(customer)
                    findNavController().navigate(action)
                },2000)
            }

        })
    }


}