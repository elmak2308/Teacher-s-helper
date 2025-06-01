package com.hfad.teachershelper

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.navigation.findNavController


class ChatFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        val homeButton = view.findViewById<ImageButton>(R.id.home_chat)
        val settingButton = view.findViewById<ImageButton>(R.id.flow_chat)
        val searchButton = view.findViewById<ImageButton>(R.id.search_chat)
        val backButtontoHome = view.findViewById<ImageButton>(R.id.back_roflchat_to_home)

        homeButton.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_chatFragment2_to_homeFragment)
        }

        settingButton.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_chatFragment2_to_settingsFragment)
        }

        searchButton.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_chatFragment2_to_searchFragment)
        }

        backButtontoHome.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_chatFragment2_to_homeFragment)
        }

        return view
    }



}