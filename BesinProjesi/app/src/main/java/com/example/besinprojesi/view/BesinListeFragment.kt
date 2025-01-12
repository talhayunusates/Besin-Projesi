package com.example.besinprojesi.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.besinprojesi.adapter.BesinRecyclerAdapter
import com.example.besinprojesi.databinding.FragmentBesinListeBinding

import com.example.besinprojesi.viewmodel.BesinListesiViewModel



class BesinListeFragment : Fragment() {
    private var _binding: FragmentBesinListeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: BesinListesiViewModel
    private val besinAdapter = BesinRecyclerAdapter(arrayListOf())
    private val TAG = "BesinListeFragment"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBesinListeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[BesinListesiViewModel::class.java]
        viewModel.refreshData()
        binding.besinListRecycler.adapter = besinAdapter
        binding.besinListRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.besinListRecycler.visibility = View.GONE
            binding.besinHataMesaji.visibility = View.GONE
            binding.besinYukleniyor.visibility = View.VISIBLE
            viewModel.refreshFromInternet()
            binding.swipeRefreshLayout.isRefreshing = false
        }
        observeLiveData()
    }

    fun observeLiveData() {

        viewModel.besinler.observe(viewLifecycleOwner) {
            Log.i(TAG, "besinler : $it")
            binding.besinListRecycler.visibility = View.VISIBLE
            besinAdapter.besinListesiniGuncelle(it)
        }
        viewModel.besinYukleniyor.observe(viewLifecycleOwner) {
            if (it) {
                Log.i(TAG, "isLoading ")
                binding.besinListRecycler.visibility = View.GONE
                binding.besinHataMesaji.visibility = View.GONE
                binding.besinYukleniyor.visibility = View.VISIBLE
            } else {
                binding.besinYukleniyor.visibility = View.GONE
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}