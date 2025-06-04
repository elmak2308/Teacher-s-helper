package com.hfad.teachershelper

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.hfad.teachershelper.retrofit.Subject
import com.hfad.teachershelper.retrofit.SubjectAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_subjects)

        val tv = findViewById<TextView>(R.id.55) //вместо 55 должно быть айди куда показывать
        val b = findViewById<Button>(R.id.66) //вместо 66 должно быть айди или что-нибудь
        //что вызывает показ списка предметов, возможно и типо буттон нужно поменять
        //а так же наверное такое стоит наверное писать в верхнем онкреате

        val retrofit = Retrofit.Builder()
            .baseUrl("https://")//тут должна быть ссылка родительская
            //типо ссылка постоянная, а в SubjectAPI ее изменяемая часть
            .addConverterFactory(GsonConverterFactory.create()).build()

        val subjectAPI = retrofit.create(SubjectAPI::class.java)

        b.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {
                val subject = subjectAPI.getSubjectById(77) // здесь можно без 77
                //это типо вызов конкретного предмета, может помочь при выборе учителем предмета
                runOnUiThread {
                    tv.text = Subject.title

                }

            }


        }


    }

}
