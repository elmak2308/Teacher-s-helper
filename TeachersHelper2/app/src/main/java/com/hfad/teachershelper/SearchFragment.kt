package com.hfad.teachershelper

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.findNavController


class SearchFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val searchtohomeButton = view.findViewById<ImageButton>(R.id.home_search)
        val searchtosettButton = view.findViewById<ImageButton>(R.id.flow_search)


        searchtohomeButton.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_searchFragment_to_homeFragment)
        }

        searchtosettButton.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_searchFragment_to_settingsFragment)
        }



        return view
    }



}