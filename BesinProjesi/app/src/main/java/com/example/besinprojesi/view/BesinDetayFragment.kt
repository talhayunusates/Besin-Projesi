package com.example.besinprojesi.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.besinprojesi.databinding.FragmentBesinDetayBinding
import com.example.besinprojesi.util.gorselIndir
import com.example.besinprojesi.util.placeholderYap
import com.example.besinprojesi.viewmodel.BesinDetayiViewModel

class BesinDetayFragment : Fragment() {

    private var _binding: FragmentBesinDetayBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : BesinDetayiViewModel
    private var besinId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBesinDetayBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            besinId = BesinDetayFragmentArgs.fromBundle(it).besinid
        }

        viewModel = ViewModelProvider(this)[BesinDetayiViewModel::class.java]
        viewModel.roomVerisiniAl(besinId)
        observeLiveData()
        binding.geri.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    fun observeLiveData(){
        viewModel.besinLiveData.observe(viewLifecycleOwner) { besin ->
            binding.besinIsim.text = besin.besinIsim
            binding.besinKalori.text = besin.besinKalori
            binding.besinKarbonhidrat.text = besin.besinKarbonhidrat
            binding.besinProtein.text = besin.besinProtein
            binding.besinYag.text = besin.besinYag
            binding.besinImage.gorselIndir(besin.besinGorsel, placeholderYap(requireContext()))
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}