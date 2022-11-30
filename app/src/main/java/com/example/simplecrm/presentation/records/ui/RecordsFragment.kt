package com.example.simplecrm.presentation.records.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplecrm.common.Resource
import com.example.simplecrm.databinding.FragmentRecordsBinding
import com.example.simplecrm.domain.model.Record
import com.example.simplecrm.presentation.records.adapter.RecordsAdapter
import com.example.simplecrm.presentation.records.viewmodel.RecordsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecordsFragment : Fragment() {

    lateinit var binding: FragmentRecordsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val recordsViewModel =
            ViewModelProvider(this)[RecordsViewModel::class.java]
        binding = FragmentRecordsBinding.inflate(inflater, container, false)

        GlobalScope.launch(Dispatchers.Main){
            recordsViewModel.getRecords()
            recordsViewModel.getRecordsResponse.collect{response->
                when(response){
                    is Resource.Error -> Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        updateUI(response.data)
                    }
                }
            }
        }

        return binding.root
    }

    private fun updateUI(records: List<Record>?) {
        val adapter = RecordsAdapter(records!!,this)
        binding.recordsRv.setHasFixedSize(true)
        binding.recordsRv.layoutManager = LinearLayoutManager(requireContext())
        binding.recordsRv.adapter=adapter
    }


}