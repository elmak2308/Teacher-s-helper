package com.hfad.teachershelper

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.findNavController
import com.hfad.teachershelper.retrofit.AuthRequestFullName
import com.hfad.teachershelper.retrofit.MainAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginFragment : Fragment() {
    private lateinit var mainAPI: MainAPI

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val okButton = view.findViewById<Button>(R.id.okei_to_loginparol)
        val loginEditText = view.findViewById<EditText>(R.id.login_to_vxod)

        // Инициализация Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mainAPI = retrofit.create(MainAPI::class.java)

        // Блокировка кнопки по умолчанию
        okButton.isEnabled = false

        // Проверка ввода логина
        loginEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                okButton.isEnabled = s.toString().trim().isNotEmpty()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        okButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = mainAPI.auth(
                        AuthRequestFullName(loginEditText.text.toString())
                    )

                    activity?.runOnUiThread {
                        if (response != null) {  // Простая проверка на успешность
                            view.findNavController()
                                .navigate(R.id.action_loginFragment_to_loginparolFragment2)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        return view
    }
}


//class LoginFragment : Fragment() {
//    lateinit var binding: LoginFragmentBinding
//    private lateinit var adapter: SubjectAdapter
//
//    private fun runOnUiThread(action: () -> Unit) {
//        this ?: return
//        if (!isAdded) return
//        activity?.runOnUiThread(action)
//
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
//                               savedInstanceState: Bundle?): View? {
//        // Inflate the layout for this fragment
//        binding = LoginparolFragmentBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//
//        val view = inflater.inflate(R.layout.fragment_login, container, false)
//        val okButton = view.findViewById<Button>(R.id.okei_to_loginparol)
//
//        okButton.setOnClickListener {
//            view.findNavController()
//                .navigate(R.id.action_loginFragment_to_loginparolFragment2)
//        }
//
//
//        val retrofit = Retrofit.Builder()
//            .baseUrl("http://10.0.2.2:8000/")//тут должна быть ссылка родительская
//            //типо ссылка постоянная, а в SubjectAPI ее изменяемая часть
//            .addConverterFactory(GsonConverterFactory.create()).build()
//
//        val mainAPI = retrofit.create(MainAPI::class.java)
//
//        CoroutineScope(Dispatchers.IO).launch {
//            val user = mainAPI.auth(
//                AuthRequest(
//                    binding.login_to_vxod.text.toString()
//                )
//            )// здесь можно без 77
//            //это типо вызов конкретного предмета, может помочь при выборе учителем предмета
//            runOnUiThread {
//                binding.apply{
//
//                }
////                    tv.text = subject.get(1).name
////                    var temp = ""
////                    for (i in 0..subject.size -1){
////                        temp += subject.get(i).name + " "
////                    }
////                    tv.text = temp
////                    //цикл
//            }
//
//        }
//
//        return view
//    }
//
//}