package com.example.simplecrm.presentation.customer.add_customer.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.simplecrm.R
import com.example.simplecrm.common.Resource
import com.example.simplecrm.databinding.FragmentSelectAddressBinding
import com.example.simplecrm.domain.model.Customer
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*

@AndroidEntryPoint
class SelectAddressFragment : Fragment(), OnMapReadyCallback, LocationListener,
    GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraMoveStartedListener,
    GoogleMap.OnCameraIdleListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentSelectAddressBinding
    private val LOCATION_PERMISSION_REQUEST = 1
    private var long: Double? = null
    private var lat: Double? = null
    private val args: SelectAddressFragmentArgs by navArgs()
    private var customer: Customer? = null

    private lateinit var viewModel: AddCustomerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectAddressBinding.inflate(layoutInflater, container, false)
        args?.let {
            customer = it.customer
        }
        viewModel = ViewModelProvider(this)[AddCustomerViewModel::class.java]

        binding.selectAddressBtn.setOnClickListener {
            if (lat != null && long != null) {
                customer!!.latitude = lat as Double
                customer!!.longitude = long as Double
                viewModel.addCustomer(customer!!)
                GlobalScope.launch(Dispatchers.Main) {
                    viewModel.insertCustomerResponse.collect { response ->
                        when (response) {
                            is Resource.Error -> {
                                Toast.makeText(requireContext(), response.message , Toast.LENGTH_SHORT).show()
                            }
                            is Resource.Loading -> {}
                            is Resource.Success -> {
                                Toast.makeText(requireContext(), response.data , Toast.LENGTH_SHORT).show()
                                findNavController().navigate(R.id.action_selectAddressFragment_to_navigation_home)
                            }
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
        mapFragment?.getMapAsync(this)
    }

    override fun onLocationChanged(location: Location) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        var address: List<Address>? = null
        try {
            address = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        setAddress(address!![0])
    }


    override fun onCameraMove() {

    }

    override fun onCameraMoveStarted(p0: Int) {

    }

    override fun onCameraIdle() {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        var addresses: List<Address>? = null
        try {
            addresses = geocoder.getFromLocation(
                mMap.cameraPosition.target.latitude,
                mMap.cameraPosition.target.longitude,
                1
            )
            setAddress(addresses!![0])
            println(
                "lat is : ${addresses[0].latitude} ,long is: ${addresses[0].longitude} , address is : ${
                    addresses[0].getAddressLine(
                        0
                    )
                } "
            )

        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }
    }

    private fun setAddress(address: Address) {
        if (address != null) {
            if (address.getAddressLine(0) != null) {
                println(address.getAddressLine(0))
                long = address.longitude
                lat = address.latitude
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getLocationAccess()

        mMap.setOnCameraIdleListener(this)
        mMap.setOnCameraMoveStartedListener(this)
        mMap.setOnCameraMoveListener(this)
    }

    //checking if the location permissions is granted
    private fun getLocationAccess() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST
            )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                mMap.isMyLocationEnabled = true
            } else {
                Toast.makeText(
                    requireContext(),
                    "User has not granted location access permission",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}