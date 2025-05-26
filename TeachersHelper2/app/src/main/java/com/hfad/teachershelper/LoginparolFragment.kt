package com.hfad.teachershelper

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController


class LoginparolFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_loginparol, container, false)
        val okButtonhome = view.findViewById<Button>(R.id.okei_to_home)
        val otmenaButtontologin = view.findViewById<Button>(R.id.otmena_reg)

        okButtonhome.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_loginparolFragment2_to_homeFragment)
        }

        otmenaButtontologin.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_loginparolFragment2_to_loginFragment)
        }

        return view
    }
}