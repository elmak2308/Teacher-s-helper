package com.hfad.teachershelper

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.findNavController


class ZabilParolFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_zabil_parol, container, false)
        val backButtontoLoginParol = view.findViewById<ImageButton>(R.id.back_zabilparol_to_loginparol)

        backButtontoLoginParol.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_zabilParolFragment_to_loginparolFragment2)
        }

        return view
    }


}