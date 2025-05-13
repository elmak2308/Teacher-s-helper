package com.hfad.teachershelper

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.findNavController


class MarksFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_marks, container, false)
        val backmarkstohomeButton = view.findViewById<ImageButton>(R.id.back_marks_to_home)
        val homefrommarksButton = view.findViewById<ImageButton>(R.id.home_marks)
        val settfrommarksButton = view.findViewById<ImageButton>(R.id.flow_marks)
        val searchfrommarksButton = view.findViewById<ImageButton>(R.id.search_marks)

        backmarkstohomeButton.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_marksFragment_to_homeFragment)
        }

        homefrommarksButton.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_marksFragment_to_homeFragment)
        }

        settfrommarksButton.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_marksFragment_to_settingsFragment)
        }

        searchfrommarksButton.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_marksFragment_to_searchFragment)
        }


        return view
    }
}