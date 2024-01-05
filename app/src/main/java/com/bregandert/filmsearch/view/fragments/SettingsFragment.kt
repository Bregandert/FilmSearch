package com.bregandert.filmsearch.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bregandert.filmsearch.R
import com.bregandert.filmsearch.databinding.FragmentSettingsBinding
import com.bregandert.filmsearch.utils.AnimationHelper
import com.bregandert.filmsearch.viewmodel.SettingsFragmentViewModel

class SettingsFragment: Fragment() {

    private lateinit var settingsBinding: FragmentSettingsBinding
    private val viewModel: SettingsFragmentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsBinding = FragmentSettingsBinding.inflate(layoutInflater)
        return settingsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AnimationHelper.performFragmentCircularRevealAnimation(settingsBinding.root,requireActivity(), 3)

        viewModel.categoryPropertyLifeData.observe(viewLifecycleOwner, Observer<String> {
            when(it) {
                POPULAR_CATEGORY -> settingsBinding.radioGroup.check(R.id.radio_popular)
                TOP_RATED_CATEGORY -> settingsBinding.radioGroup.check(R.id.radio_top_rated)
                UPCOMING_CATEGORY -> settingsBinding.radioGroup.check(R.id.radio_upcoming)
                NOW_PLAYING_CATEGORY -> settingsBinding.radioGroup.check(R.id.radio_now_playing)
            }
        })

        settingsBinding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
                R.id.radio_popular -> viewModel.putCategoryProperty(POPULAR_CATEGORY)
                R.id.radio_top_rated -> viewModel.putCategoryProperty(TOP_RATED_CATEGORY)
                R.id.radio_upcoming -> viewModel.putCategoryProperty(UPCOMING_CATEGORY)
                R.id.radio_now_playing -> viewModel.putCategoryProperty(NOW_PLAYING_CATEGORY)
            }
        }



    }
    companion object {
        private const val POPULAR_CATEGORY = "popular"
        private const val TOP_RATED_CATEGORY = "top_rated"
        private const val UPCOMING_CATEGORY = "upcoming"
        private const val NOW_PLAYING_CATEGORY = "now_playing"
    }

}