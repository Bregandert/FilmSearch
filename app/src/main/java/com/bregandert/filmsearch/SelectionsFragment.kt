package com.bregandert.filmsearch

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bregandert.filmsearch.databinding.FragmentSelectionsBinding



class SelectionsFragment : Fragment() {

    private lateinit var binding: FragmentSelectionsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSelectionsBinding.inflate(layoutInflater)
        AnimationHelper.performFragmentCircularRevealAnimation(binding.root, requireActivity(), 3)
        return binding.root
    }
}