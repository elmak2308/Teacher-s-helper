package com.hfad.teachershelper

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.createGraph
import androidx.navigation.findNavController


class MathSubjectsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_math_subjects, container, false)
        val backMathSubjectToSubject = view.findViewById<ImageButton>(R.id.back_mathSubject_to_subject)
        val homeButton = view.findViewById<ImageButton>(R.id.home_mathSubject)
        val searchButton = view.findViewById<ImageButton>(R.id.search_mathSubject)
        val settingsButton = view.findViewById<ImageButton>(R.id.flow_mathSubject)

        backMathSubjectToSubject.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_mathSubjectsFragment_to_subjectsFragment)
        }

        homeButton.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_mathSubjectsFragment_to_homeFragment)
        }

        searchButton.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_mathSubjectsFragment_to_searchFragment)
        }

        settingsButton.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_mathSubjectsFragment_to_settingsFragment)
        }

        return view
    }



}