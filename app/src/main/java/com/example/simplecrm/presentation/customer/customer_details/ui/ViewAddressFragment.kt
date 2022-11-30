package com.example.simplecrm.presentation.customer.customer_details.ui

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.simplecrm.R
import com.example.simplecrm.common.Resource
import com.example.simplecrm.databinding.FragmentViewAddressBinding
import com.example.simplecrm.domain.model.Customer
import com.example.simplecrm.domain.model.Record
import com.example.simplecrm.presentation.customer.customer_details.viewmodel.ViewAddressViewModel

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ViewAddressFragment : Fragment() {

    private val args: ViewAddressFragmentArgs by navArgs()
    lateinit var binding: FragmentViewAddressBinding
    private var customer: Customer? = null

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        args?.let {
            customer = it.customer
            val sydney = LatLng(it.customer.latitude, it.customer.longitude)
            googleMap.addMarker(MarkerOptions().position(sydney).title("Customer Location"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
            googleMap.isMyLocationEnabled = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewAddressBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this)[ViewAddressViewModel::class.java]

        binding.setAsVisitedBtn.setOnClickListener {
            val record = Record(
                0, 2, "${customer!!.firstName} ${customer!!.LastName}",
                customer!!.companyName, customer!!.profileImg, System.currentTimeMillis()
            )
            GlobalScope.launch(Dispatchers.Main) {
                viewModel.insertRecords(record)
                viewModel.insertRecordResponse.collect { response ->
                    when (response) {
                        is Resource.Error -> {
                            Toast.makeText(
                                requireContext(),
                                response.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            requireActivity().onBackPressed()
                        }
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            Toast.makeText(requireContext(),"Customer set as visited." ,Toast.LENGTH_SHORT).show()
                            requireActivity().onBackPressed()
                        }
                    }
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}