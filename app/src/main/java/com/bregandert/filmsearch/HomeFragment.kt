package com.bregandert.filmsearch

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bregandert.filmsearch.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var filmsAdapter: FilmListRecyclerAdapter
    private lateinit var binding: FragmentHomeBinding

    var filmsDataBase = mutableListOf<Film>(
        Film(1,"Elemental", R.drawable.elemental, "Elemental - fantasy animation film."),
        Film(2, "Barbie", R.drawable.barbie, "Margo Robbi is in the comedy about the famous doll."),
        Film(3, "Indiana Jones and the dial of destiny", R.drawable.indiana_jones_and_the_dial_of_destiny, "New part of famous franshise."),
        Film(4, "Oppenheimer", R.drawable.oppenheimer, "Film is about creator of the atomic bomb."),
        Film(5,"Insidious the red door", R.drawable.insidious_the_red_door, "New part of one of the scare horror."),
        Film(6, "Mission impossible dead reckoning peart one", R.drawable.mission_impossible__dead_reckoning_part_one, "Special agent Itan Hant safe the World again."),
        Film(7, "No hard fillings", R.drawable.no_hard_feelings, "Light comedy with beautiful Jenifer Lawrence"),
        Film(8,"Spiderman across the spiderverse", R.drawable.spiderman_across_the_spiderverse, "Continuation of animation film"),
        Film(9, "Transformers rise of the beasts", R.drawable.transformers_rise_of_the_beasts, "Beasts in the World of big robots")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        initHomeFragment()

        return binding.root
    }

    //находим наш RV
    private fun initHomeFragment() {

            filmsAdapter = FilmListRecyclerAdapter(object : FilmListRecyclerAdapter.OnItemClickListener {
                    override fun click(film: Film) {
                        (requireActivity() as MainActivity).launchDetailsFragment(film)
                    }
                })





            filmsAdapter.addItems(filmsDataBase)
            //Присваиваем адаптер
            binding.mainRecycler.adapter = filmsAdapter

            //Применяем декоратор для отступов
            val decorator = TopSpacingItemDecoration(8)
            binding.mainRecycler.addItemDecoration(decorator)

        }




}