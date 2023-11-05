package com.bregandert.filmsearch.data

import com.bregandert.filmsearch.R
import com.bregandert.filmsearch.domain.Film

class MainRepository {

//    Наша БД
        val filmsDataBase = mutableListOf(
            Film(1,"Elemental", R.drawable.elemental, "Elemental - fantasy animation film.", 7.7f),
            Film(2, "Barbie",
                R.drawable.barbie, "Margo Robbi is in the comedy about the famous doll.", 4.5f),
            Film(3, "Indiana Jones and the dial of destiny",
                R.drawable.indiana_jones_and_the_dial_of_destiny, "New part of famous franshise.", 2.3f),
            Film(4, "Oppenheimer",
                R.drawable.oppenheimer, "Film is about creator of the atomic bomb.", 5.5f),
            Film(5,"Insidious the red door",
                R.drawable.insidious_the_red_door, "New part of one of the scare horror.", 3.3f),
            Film(6, "Mission impossible dead reckoning peart one",
                R.drawable.mission_impossible__dead_reckoning_part_one, "Special agent Itan Hant safe the World again.", 5.7f),
            Film(7, "No hard fillings",
                R.drawable.no_hard_feelings, "Light comedy with beautiful Jenifer Lawrence", 6.7f),
            Film(8,"Spiderman across the spiderverse",
                R.drawable.spiderman_across_the_spiderverse, "Continuation of animation film", 8.8f),
            Film(9, "Transformers rise of the beasts",
                R.drawable.transformers_rise_of_the_beasts, "Beasts in the World of big robots",6.5f)
        )
    }
