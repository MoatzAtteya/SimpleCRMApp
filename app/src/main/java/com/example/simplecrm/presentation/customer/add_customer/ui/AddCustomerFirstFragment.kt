package com.example.simplecrm.presentation.customer.add_customer.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.example.simplecrm.R
import com.example.simplecrm.databinding.FragmentAddCustomerBinding
import com.example.simplecrm.domain.model.Customer

class AddCustomerFirstFragment : Fragment(), View.OnClickListener {

    lateinit var binding: FragmentAddCustomerBinding
    private var profileImgUri: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddCustomerBinding.inflate(layoutInflater, container, false)
        binding.editProfileImageIb.setOnClickListener {
            cropImage.launch(
                options {
                    setGuidelines(CropImageView.Guidelines.ON)
                    setCropShape(CropImageView.CropShape.OVAL)
                    setAspectRatio(1, 1)
                }
            )
        }
        binding.personalInformationSaveBtn.setOnClickListener(this)
        binding.backBtn.setOnClickListener(this)
        return binding.root
    }

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            val uriContent = result.uriContent
            profileImgUri = uriContent.toString()

            Glide.with(requireContext())
                .load(uriContent)
                .placeholder(R.drawable.profile_pic)
                .into(binding.customerProfileImgIv)

        } else {
            val exception = result.error
            Log.e("AddCustomerFirstFragment", exception!!.message.toString())
            Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateUserInputs(): Boolean {
        return when {
            TextUtils.isEmpty(binding.addFirstNameEt.text.toString().trim { it <= ' ' }) -> {
                binding.addFirstNameTil.isErrorEnabled = true
                binding.addFirstNameEt.error = "You need to enter customer's First name."
                false
            }
            TextUtils.isEmpty(binding.addLastNameEt.text.toString().trim { it <= ' ' }) -> {
                binding.addLastNameTil.isErrorEnabled = true
                binding.addLastNameEt.error = "You need to enter customer's Last name."
                false
            }

            TextUtils.isEmpty(binding.addEmailEt.text.toString().trim { it <= ' ' }) -> {
                binding.addEmailTil.isErrorEnabled = true
                binding.addEmailEt.error = "You need to enter customer's email."
                false
            }

            !binding.addEmailEt.text!!.contains('@') || !binding.addEmailEt.text!!.contains('.') -> {
                binding.addEmailTil.isErrorEnabled = true
                binding.addEmailEt.error = "You need to enter correct email form."
                false
            }

            TextUtils.isEmpty(binding.addPhoneNumberEt.text.toString().trim { it <= ' ' }) -> {
                binding.addPhoneNumberTil.isErrorEnabled = true
                binding.addPhoneNumberEt.error = "You need to enter customer's phone number."
                false
            }

            profileImgUri.isEmpty() -> {
                Toast.makeText(
                    requireContext(),
                    "Please select customer photo.",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }

            else -> {
                binding.addFirstNameTil.isErrorEnabled = false
                binding.addLastNameTil.isErrorEnabled = false
                binding.addEmailTil.isErrorEnabled = false
                binding.addPhoneNumberTil.isErrorEnabled = false
                true
            }
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.back_btn -> {
                requireActivity().onBackPressed()
            }
            R.id.personal_information_save_btn -> {
                if (validateUserInputs()) {
                    val firstName = binding.addFirstNameEt.text.toString()
                    val lastName = binding.addLastNameEt.text.toString()
                    val email = binding.addEmailEt.text.toString()
                    val phoneNumber = binding.addPhoneNumberEt.text.toString()
                    val customer = Customer(
                        0,
                        firstName,
                        lastName,
                        email,
                        phoneNumber,
                        profileImgUri
                    )
                    val action =
                        AddCustomerFirstFragmentDirections.actionAddCustomerFragmentToAddCustomerLastFragment(
                            customer
                        )
                    findNavController().navigate(action)
                }
            }
        }
    }
}