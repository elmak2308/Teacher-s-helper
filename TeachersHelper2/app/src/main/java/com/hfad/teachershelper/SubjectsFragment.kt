package com.hfad.teachershelper

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
//import androidx.databinding.DataBindingUtil.setContentView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.teachershelper.Adapter.SubjectAdapter
import com.hfad.teachershelper.retrofit.MainAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
//import com.hfad.teachershelper.databinding.FragmentSubjectsBinding
import retrofit2.converter.gson.GsonConverterFactory


class SubjectsFragment : Fragment() {
    private lateinit var adapter: SubjectAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var mainAPI: MainAPI

    private fun runOnUiThread(action: () -> Unit) {
        if (!isAdded) return
        activity?.runOnUiThread(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_subjects, container, false)

        // Инициализация UI элементов
        recyclerView = view.findViewById(R.id.rview)
        val backToHomeButton = view.findViewById<ImageButton>(R.id.back_subject_to_home)
        val homeButton = view.findViewById<ImageButton>(R.id.home_subb)
        val settingsButton = view.findViewById<ImageButton>(R.id.flow_subb)
        val searchButton = view.findViewById<ImageButton>(R.id.search_subb)

        // Настройка RecyclerView
        adapter = SubjectAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Обработчики нажатий
        backToHomeButton.setOnClickListener {
            navigateTo(R.id.action_subjectsFragment_to_homeFragment)
        }

        homeButton.setOnClickListener {
            navigateTo(R.id.action_subjectsFragment_to_homeFragment)
        }

        settingsButton.setOnClickListener {
            navigateTo(R.id.action_subjectsFragment_to_settingsFragment)
        }

        searchButton.setOnClickListener {
            navigateTo(R.id.action_subjectsFragment_to_searchFragment)
        }

        // Настройка Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mainAPI = retrofit.create(MainAPI::class.java)

        // Загрузка данных
        loadSubjects()

        return view
    }

    private fun navigateTo(actionId: Int) {
        view?.findNavController()?.navigate(actionId)
    }

    private fun loadSubjects() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val list = mainAPI.getAllItems()
                runOnUiThread {
                    adapter.submitList(list)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Можно добавить обработку ошибок
            }
        }
    }
}


//class SubjectsFragment : Fragment() {
//    private lateinit var adapter: SubjectAdapter
//    lateinit var binding: FragmentSubjectsBinding
//
//    private fun runOnUiThread(action: () -> Unit) {
//        this ?: return
//        if (!isAdded) return
//        activity?.runOnUiThread(action)
//
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?): View? {
//
//        binding = SubjectsFragmentBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        // Inflate the layout for this fragment
//        val view = inflater.inflate(R.layout.fragment_subjects, container, false)
//        val backsubbtohome = view.findViewById<ImageButton>(R.id.back_subject_to_home)
//        val homefromsubbButton = view.findViewById<ImageButton>(R.id.home_subb)
//        val settfromsubbButton = view.findViewById<ImageButton>(R.id.flow_subb)
//        val searchfromsubbButton = view.findViewById<ImageButton>(R.id.search_subb)
////        val mathSubjectButton = view.findViewById<Button>(R.id.math_subb)
//
//        backsubbtohome.setOnClickListener {
//            view.findNavController()
//                .navigate(R.id.action_subjectsFragment_to_homeFragment)
//        }
//
//        homefromsubbButton.setOnClickListener {
//            view.findNavController()
//                .navigate(R.id.action_subjectsFragment_to_homeFragment)
//        }
//
//        settfromsubbButton.setOnClickListener {
//            view.findNavController()
//                .navigate(R.id.action_subjectsFragment_to_settingsFragment)
//        }
//
//        searchfromsubbButton.setOnClickListener {
//            view.findNavController()
//                .navigate(R.id.action_subjectsFragment_to_searchFragment)
//        }
//
////        mathSubjectButton.setOnClickListener {
////            view.findNavController()
////                .navigate(R.id.action_subjectsFragment_to_mathSubjectsFragment)
////        }
//        adapter = SubjectAdapter()
//        binding.rview.layoutManager = LinearLayoutManager(this)
//        binding.rview.adapter = adapter
//        adapter.submitList()
//
////        val tv = view.findViewById<TextView>(R.id.trix) //вместо 55 должно быть айди куда показывать
////        val b = view.findViewById<Button>(R.id.button) //вместо 66 должно быть айди или что-нибудь
//        //что вызывает показ списка предметов, возможно и типо буттон нужно поменять
//        //а так же наверное такое стоит наверное писать в верхнем онкреате
//
//        val retrofit = Retrofit.Builder()
//            .baseUrl("http://10.0.2.2:8000/")//тут должна быть ссылка родительская
//            //типо ссылка постоянная, а в SubjectAPI ее изменяемая часть
//            .addConverterFactory(GsonConverterFactory.create()).build()
//
//        val mainAPI = retrofit.create(MainAPI::class.java)
//
//
//        CoroutineScope(Dispatchers.IO).launch {
//                val list = mainAPI.getAllItems() // здесь можно без 77
//                //это типо вызов конкретного предмета, может помочь при выборе учителем предмета
//                runOnUiThread {
//                    binding.apply{
//                        adapter.submitList(list)
//                    }
////                    tv.text = subject.get(1).name
////                    var temp = ""
////                    for (i in 0..subject.size -1){
////                        temp += subject.get(i).name + " "
////                    }
////                    tv.text = temp
////                    //цикл
//                }
//
//        }
//
//        return view
//    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_subjects)
//
//        val tv = findViewById<TextView>(R.id.55) //вместо 55 должно быть айди куда показывать
//        val b = findViewById<Button>(R.id.66) //вместо 66 должно быть айди или что-нибудь
//        //что вызывает показ списка предметов, возможно и типо буттон нужно поменять
//        //а так же наверное такое стоит наверное писать в верхнем онкреате
//
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://")//тут должна быть ссылка родительская
//            //типо ссылка постоянная, а в SubjectAPI ее изменяемая часть
//            .addConverterFactory(GsonConverterFactory.create()).build()
//
//        val subjectAPI = retrofit.create(SubjectAPI::class.java)
//
//        b.setOnClickListener{
//            CoroutineScope(Dispatchers.IO).launch {
//                val subject = subjectAPI.getSubjectById(77) // здесь можно без 77
//                //это типо вызов конкретного предмета, может помочь при выборе учителем предмета
//                runOnUiThread {
//                    tv.text = Subject.title
//
//                }
//
//            }
//
//
//        }


//    }


