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
import com.bregandert.filmsearch.MainActivity.Companion.filmsDataBase
import androidx.recyclerview.widget.RecyclerView


class HomeFragment : Fragment() {

    private lateinit var filmsAdapter: FilmListRecyclerAdapter
    private lateinit var binding: FragmentHomeBinding



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