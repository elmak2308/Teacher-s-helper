package com.hfad.teachershelper

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val subjectButton = view.findViewById<Button>(R.id.subject)
        val marksButton = view.findViewById<Button>(R.id.marks)
        val settingButton = view.findViewById<ImageButton>(R.id.flow_home)
//        val buttonNavView = view.findViewById<BottomNavigationView>(R.id.bottomNavigationView_home)
        val searchButton = view.findViewById<ImageButton>(R.id.search_home)

        subjectButton.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_homeFragment_to_subjectsFragment)
        }

        marksButton.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_homeFragment_to_marksFragment)
        }

        settingButton.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_homeFragment_to_settingsFragment)
        }

        searchButton.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_homeFragment_to_searchFragment)
        }

//        buttonNavView.setOnItemSelectedListener {  item ->
//            when(item.itemId){
//                R.id.flow_home -> {
//                    view.findNavController()
//                        .navigate(R.id.action_homeFragment_to_settingsFragment)
//
//                    true }
//                R.id.home_home -> { true }
//                R.id.search_home -> { true }
//                else -> false
//            }
//        }

        return view
    }

}