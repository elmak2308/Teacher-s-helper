package com.hfad.teachershelper

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.findNavController


class SettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        val homebuttonfromsett = view.findViewById<ImageButton>(R.id.home_sett)
        val setttosearchButton = view.findViewById<ImageButton>(R.id.search_sett)


        homebuttonfromsett.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_settingsFragment_to_homeFragment)
        }

        setttosearchButton.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_settingsFragment_to_searchFragment)
        }



        return view
    }


}