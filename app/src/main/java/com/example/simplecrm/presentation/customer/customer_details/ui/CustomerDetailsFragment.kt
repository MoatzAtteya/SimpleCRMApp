package com.example.simplecrm.presentation.customer.customer_details.ui

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.simplecrm.R
import com.example.simplecrm.common.Resource
import com.example.simplecrm.databinding.FragmentCustomerDetailsBinding
import com.example.simplecrm.domain.model.Customer
import com.example.simplecrm.domain.model.Record
import com.example.simplecrm.presentation.calls.ui.CallsFragmentDirections
import com.example.simplecrm.presentation.customer.add_customer.ui.AddCustomerViewModel
import com.example.simplecrm.presentation.customer.customer_details.viewmodel.CustomerDetailsViewModel
import com.marsad.stylishdialogs.StylishAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CustomerDetailsFragment : Fragment(), View.OnClickListener {

    lateinit var binding: FragmentCustomerDetailsBinding
    private val args: CustomerDetailsFragmentArgs by navArgs()
    private var customer: Customer? = null
    private lateinit var viewModel: CustomerDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCustomerDetailsBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[CustomerDetailsViewModel::class.java]

        args?.let {
            customer = it.customer
            updateUi(it.customer)
        }

        binding.backBtn.setOnClickListener(this)
        binding.callAction.setOnClickListener(this)
        binding.visitAction.setOnClickListener(this)
        binding.followUpAction.setOnClickListener(this)
        binding.deleteCustomerTv.setOnClickListener(this)
        return binding.root
    }

    private fun updateUi(customer: Customer) {
        binding.apply {
            Glide.with(requireContext())
                .load(customer.profileImg)
                .placeholder(R.drawable.profile_pic)
                .into(binding.customerProfileImgIv)

            customerNameTv.text = "${customer.firstName} ${customer.LastName}"
            customerCompanyNameTv.text = customer.companyName
            customerEmailTv.text = customer.Email
            customerMobileNumberTv.text = customer.mobileNumber
            companyMobileNumberTv.text = customer.companyPhoneNumber
            companyInformationTv.text =
                getString(R.string.company_information_text, customer.information)

            if (!customer.canVisit)
                visitAction.visibility = View.GONE
            if (!customer.canCall)
                callAction.visibility = View.GONE
            if (!customer.canFollowUp)
                followUpAction.visibility = View.GONE
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.back_btn -> requireActivity().onBackPressed()
            R.id.call_action -> {
                val alertDialog = StylishAlertDialog(requireContext(), StylishAlertDialog.PROGRESS)
                alertDialog.titleText = "Starting a Call..."
                alertDialog.cancellable = false
                alertDialog.show()
                Handler().postDelayed({
                    alertDialog.dismissWithAnimation()
                    val action =
                        CustomerDetailsFragmentDirections.actionCustomerDetailsFragmentToCallingFragment(
                            customer!!
                        )
                    findNavController().navigate(action)
                }, 2000)
            }
            R.id.visit_action -> {
                val action =
                    CustomerDetailsFragmentDirections.actionCustomerDetailsFragmentToViewAddressFragment(
                        customer!!
                    )
                findNavController().navigate(action)
            }
            R.id.follow_up_action -> {
                val builder = AlertDialog.Builder(context)
                val inflater = layoutInflater
                val dialogLayout = inflater.inflate(R.layout.follow_up_message, null)
                val editText = dialogLayout.findViewById<EditText>(R.id.follow_up_message_et)
                with(builder) {
                    setPositiveButton("Send") { dialog, which ->
                        if (editText.text.isNotEmpty()) {
                            Toast.makeText(
                                requireContext(),
                                "Follow-up message sent.",
                                Toast.LENGTH_SHORT
                            ).show()
                            editText.text.clear()
                            storeFollowUpRecord()
                        } else
                            Toast.makeText(
                                requireContext(),
                                "Can't send empty message!",
                                Toast.LENGTH_SHORT
                            ).show()

                    }
                        .setNegativeButton("Cancel") { _, _ -> }
                    setView(dialogLayout)
                    show()
                }
            }
            R.id.delete_customer_tv -> {
                viewModel.deleteCustomer(customer!!)
                GlobalScope.launch(Dispatchers.Main) {
                    viewModel.deleteCustomerResponse.collect { response ->
                        when (response) {
                            is Resource.Error -> Toast.makeText(
                                requireContext(),
                                response.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            is Resource.Loading -> {}
                            is Resource.Success -> {
                                Toast.makeText(requireContext(), response.data, Toast.LENGTH_SHORT)
                                    .show()
                                requireActivity().onBackPressed()
                            }
                        }

                    }
                }
            }
        }
    }

    private fun storeFollowUpRecord() {
        val record = Record(
            0, 3, "${customer!!.firstName} ${customer!!.LastName}",
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

}