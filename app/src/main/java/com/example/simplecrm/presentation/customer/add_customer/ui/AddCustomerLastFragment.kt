package com.example.simplecrm.presentation.customer.add_customer.ui

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.simplecrm.R
import com.example.simplecrm.databinding.FragmentAddCustomerLastBinding
import com.example.simplecrm.databinding.FragmentSelectAddressBinding
import com.example.simplecrm.domain.model.Customer


class AddCustomerLastFragment : Fragment(), View.OnClickListener {
    private var customer: Customer? = null
    lateinit var binding: FragmentAddCustomerLastBinding
    private var isCall = false
    private var isVisit = false
    private var isFollowUp = false
    private val args: AddCustomerLastFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        args?.let {
            customer = it.customer
        }
        binding = FragmentAddCustomerLastBinding.inflate(inflater, container, false)


        binding.callAction.setOnClickListener(this)
        binding.visitAction.setOnClickListener(this)
        binding.followUpAction.setOnClickListener(this)
        binding.companyInformationSaveBtn.setOnClickListener(this)

        return binding.root
    }

    private fun validateUserInputs(): Boolean {
        return when {
            TextUtils.isEmpty(binding.addCompanyNameEt.text.toString().trim { it <= ' ' }) -> {
                binding.addCompanyNameTil.isErrorEnabled = true
                binding.addCompanyNameEt.error = "You need to enter customer's company name."
                false
            }

            TextUtils.isEmpty(
                binding.addCompanyInformationEt.text.toString().trim { it <= ' ' }) -> {
                binding.addCompanyInformationTil.isErrorEnabled = true
                binding.addCompanyInformationEt.error =
                    "You need to enter customer's company information."
                false
            }
            TextUtils.isEmpty(binding.companyPhoneNumberEt.text.toString().trim { it <= ' ' }) -> {
                binding.companyPhoneNumberTil.isErrorEnabled = true
                binding.companyPhoneNumberEt.error = "You need to enter customer's company number."
                false
            }

            else -> {
                binding.companyPhoneNumberTil.isErrorEnabled = false
                binding.addCompanyNameTil.isErrorEnabled = false
                binding.addCompanyInformationTil.isErrorEnabled = false
                true
            }
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.call_action -> {
                isCall = if (!isCall) {
                    binding.callAction.background.setTint(Color.parseColor("#FF03DAC5"))
                    //binding.callAction.setBackgroundColor(Color.parseColor("#FF03DAC5"))
                    true
                } else {
                    binding.callAction.background.setTint(Color.parseColor("#C4FDF8"))
                    false
                }
            }
            R.id.visit_action -> {
                isVisit = if (!isVisit) {
                    binding.visitAction.background.setTint(Color.parseColor("#FF03DAC5"))
                    true
                } else {
                    binding.visitAction.background.setTint(Color.parseColor("#C4FDF8"))
                    false
                }
            }
            R.id.follow_up_action -> {
                isFollowUp = if (!isFollowUp) {
                    binding.followUpAction.background.setTint(Color.parseColor("#FF03DAC5"))
                    true
                } else {
                    binding.followUpAction.background.setTint(Color.parseColor("#C4FDF8"))
                    false
                }
            }
            R.id.company_information_save_btn -> {
                if (validateUserInputs()) {
                    customer!!.companyName = binding.addCompanyNameEt.text.toString()
                    customer!!.information = binding.addCompanyInformationEt.text.toString()
                    customer!!.companyPhoneNumber = binding.companyPhoneNumberEt.text.toString()
                    customer!!.canCall = isCall
                    customer!!.canVisit = isVisit
                    customer!!.canFollowUp = isFollowUp
                    val action = AddCustomerLastFragmentDirections.actionAddCustomerLastFragmentToSelectAddressFragment(customer!!)
                    findNavController().navigate(action)
                }
            }
            R.id.back_btn -> {
                requireActivity().onBackPressed()
            }
        }
    }

}