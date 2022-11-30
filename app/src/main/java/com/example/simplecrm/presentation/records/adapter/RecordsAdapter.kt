package com.example.simplecrm.presentation.records.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.simplecrm.R
import com.example.simplecrm.databinding.RecordsListItemBinding
import com.example.simplecrm.domain.model.Record
import com.example.simplecrm.presentation.records.ui.RecordsFragment
import java.text.SimpleDateFormat
import java.util.*

class RecordsAdapter(private val records: List<Record>, private val fragment: RecordsFragment) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.records_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val record = records[position]
            holder.binding.apply {
                customerNameTv.text = record.customerName
                customerCompanyNameTv.text = record.customerCompany

                when (record.actionType) {
                    1 -> customerActionTypeTv.text = "Call"
                    2 -> customerActionTypeTv.text = "Visit"
                    3 -> customerActionTypeTv.text = "Follow-up"
                }
                val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss aaa")
                val date = sdf.format(Date(record.time))
                customerActionTimeTv.text = date

                Glide.with(fragment.requireContext())
                    .load(record.customerProfileImg)
                    .into(customerProfileImgIv)
            }

        }
    }

    override fun getItemCount(): Int {
        return records.size
    }

    private class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = RecordsListItemBinding.bind(view)
    }
}