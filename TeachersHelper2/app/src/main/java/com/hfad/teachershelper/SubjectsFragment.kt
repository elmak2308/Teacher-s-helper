package com.hfad.teachershelper

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.navigation.findNavController


class SubjectsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_subjects, container, false)
        val backsubbtohome = view.findViewById<ImageButton>(R.id.back_subject_to_home)
        val homefromsubbButton = view.findViewById<ImageButton>(R.id.home_subb)
        val settfromsubbButton = view.findViewById<ImageButton>(R.id.flow_subb)
        val searchfromsubbButton = view.findViewById<ImageButton>(R.id.search_subb)
        val mathSubjectButton = view.findViewById<Button>(R.id.math_subb)

        backsubbtohome.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_subjectsFragment_to_homeFragment)
        }

        homefromsubbButton.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_subjectsFragment_to_homeFragment)
        }

        settfromsubbButton.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_subjectsFragment_to_settingsFragment)
        }

        searchfromsubbButton.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_subjectsFragment_to_searchFragment)
        }

        mathSubjectButton.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_subjectsFragment_to_mathSubjectsFragment)
        }

        return view
    }

}
