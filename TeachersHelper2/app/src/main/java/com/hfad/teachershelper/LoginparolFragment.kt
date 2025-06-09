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
import com.hfad.teachershelper.retrofit.AuthRequestHashedPassword
import com.hfad.teachershelper.retrofit.MainAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginparolFragment : Fragment() {
    private lateinit var mainAPI: MainAPI
    private lateinit var okButtonHome: Button
    private lateinit var otmenaButton: Button
    private lateinit var zabilParolButton: Button
    private lateinit var passwordEdit: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_loginparol, container, false)

        // Инициализация UI элементов
        okButtonHome = view.findViewById(R.id.okei_to_home)
        otmenaButton = view.findViewById(R.id.otmena_reg)
        zabilParolButton = view.findViewById(R.id.parolzabil)
        passwordEdit = view.findViewById(R.id.passwordEdit)

        // Настройка Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        mainAPI = retrofit.create(MainAPI::class.java)

        // Блокировка кнопки по умолчанию
        okButtonHome.isEnabled = false

        // Проверка ввода пароля
        passwordEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateInput()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Обработчики нажатий
        okButtonHome.setOnClickListener {
            authenticateAndNavigate()
        }

        otmenaButton.setOnClickListener {
            navigateTo(R.id.action_loginparolFragment2_to_loginFragment)
        }

        zabilParolButton.setOnClickListener {
            navigateTo(R.id.action_loginparolFragment2_to_zabilParolFragment)
        }

        return view
    }

    private fun validateInput() {
        okButtonHome.isEnabled = passwordEdit.text.toString().trim().isNotEmpty()
    }

    private fun authenticateAndNavigate() {
        val password = passwordEdit.text.toString()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = mainAPI.autH(
                    AuthRequestHashedPassword(
                        hashed_password = password
                    )
                )

                activity?.runOnUiThread {
                    if (response != null) { // Успешная аутентификация
                        navigateTo(R.id.action_loginparolFragment2_to_homeFragment)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun navigateTo(actionId: Int) {
        view?.findNavController()?.navigate(actionId)
    }
}


//class LoginparolFragment : Fragment() {
//
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
//                              savedInstanceState: Bundle?): View? {
//        // Inflate the layout for this fragment
//        binding = LoginparolFragmentBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val view = inflater.inflate(R.layout.fragment_loginparol, container, false)
//        val okButtonhome = view.findViewById<Button>(R.id.okei_to_home)
//        val otmenaButtontologin = view.findViewById<Button>(R.id.otmena_reg)
//        val zabilParolButton = view.findViewById<Button>(R.id.parolzabil)
//
//        okButtonhome.setOnClickListener {
//            view.findNavController()
//                .navigate(R.id.action_loginparolFragment2_to_homeFragment)
//        }
//
//        otmenaButtontologin.setOnClickListener {
//            view.findNavController()
//                .navigate(R.id.action_loginparolFragment2_to_loginFragment)
//        }
//
//        zabilParolButton.setOnClickListener {
//            view.findNavController()
//                .navigate(R.id.action_loginparolFragment2_to_zabilParolFragment)
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
//                    binding.passwordEdit.text.toString()
//                )
//            )// здесь можно без 77
//            //это типо вызов конкретного предмета, может помочь при выборе учителем предмета
//            runOnUiThread {
//                binding.apply{
//                    adapter.submitList(list)
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
//}