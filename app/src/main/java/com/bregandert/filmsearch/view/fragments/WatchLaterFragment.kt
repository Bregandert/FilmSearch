package com.bregandert.filmsearch.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bregandert.filmsearch.databinding.FragmentWatchLaterBinding
import com.bregandert.filmsearch.utils.AnimationHelper


class WatchLaterFragment : Fragment() {

    private lateinit var binding: FragmentWatchLaterBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWatchLaterBinding.inflate(layoutInflater)
        AnimationHelper.performFragmentCircularRevealAnimation(binding.root, requireActivity(), 2)
        return binding.root
    }
}