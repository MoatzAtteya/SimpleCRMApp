package com.example.simplecrm.presentation.calls.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.simplecrm.R
import com.example.simplecrm.databinding.CallsListItemBinding
import com.example.simplecrm.domain.model.Customer
import com.example.simplecrm.presentation.calls.ui.CallsFragment

class CallCustomersAdapter(
    private val customers: List<Customer>,
    private val fragment: CallsFragment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(
            position: Int,
            customer: Customer
        )
    }

    private lateinit var mListener: OnItemClickListener

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.calls_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val customer = customers[position]
            holder.binding.apply {
                customerNameTitleTv.text = "${customer.firstName} ${customer.LastName}"
                customerCompanyNameTitleTv.text = customer.companyName

                Glide.with(fragment.requireContext())
                    .load(customer.profileImg)
                    .into(customerProfileImgIv)

                customerCallIv.setOnClickListener {
                    mListener.onItemClick(position,customer)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return customers.size
    }

    private class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = CallsListItemBinding.bind(view)
    }
}