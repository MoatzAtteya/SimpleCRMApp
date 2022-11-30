package com.example.simplecrm.presentation.calls.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.simplecrm.R
import com.example.simplecrm.common.Resource
import com.example.simplecrm.databinding.FragmentCallingBinding
import com.example.simplecrm.domain.model.Customer
import com.example.simplecrm.domain.model.Record
import com.example.simplecrm.presentation.calls.viewmodel.CallingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CallingFragment : Fragment() {

    lateinit var binding: FragmentCallingBinding
    private lateinit var viewModel: CallingViewModel
    private val args: CallingFragmentArgs by navArgs()
    private var customer: Customer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCallingBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(CallingViewModel::class.java)

        args?.let {
            customer = it.customer
            updateUi(customer!!)
        }

        binding.endCallBtn.setOnClickListener {
            val record = Record(
                0, 1, "${customer!!.firstName} ${customer!!.LastName}",
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
                        is Resource.Success -> requireActivity().onBackPressed()
                    }
                }
            }
        }
        return binding.root
    }

    private fun updateUi(customer: Customer) {
        binding.customerNameTv.text = "${customer.firstName} ${customer.LastName}"
        Glide.with(requireActivity())
            .load(customer.profileImg)
            .placeholder(R.drawable.profile_pic)
            .into(binding.customerProfileImgIv)
    }


}