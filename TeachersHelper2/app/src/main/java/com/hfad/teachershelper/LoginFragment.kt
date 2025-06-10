package com.hfad.teachershelper

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.hfad.teachershelper.retrofit.AuthRequestFullName
import com.hfad.teachershelper.retrofit.MainAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginFragment : Fragment() {
    private lateinit var navController: NavController
    private lateinit var loginEditText: EditText
    private lateinit var continueButton: Button
    private lateinit var mainAPI: MainAPI
    private var temp_token: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        navController = findNavController()

        loginEditText = view.findViewById(R.id.login_to_vxod)
        continueButton = view.findViewById(R.id.okei_to_loginparol)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        mainAPI = retrofit.create(MainAPI::class.java)


        // Проверка ввода логина
        loginEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                continueButton.isEnabled = s?.toString()?.trim()?.isNotEmpty() ?: false
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        continueButton.setOnClickListener {
            val phone = loginEditText.text.toString().trim()

            // Проверка формата логина (email/телефон)
            if (!isValidLogin(phone)) {
                showError("Некорректный формат логина")
                return@setOnClickListener
            }

            // Передаём логин на следующий экран через аргументы
//            val bundle = Bundle().apply {
//                putString("login", loginEditText.text.toString())
//            }
//            findNavController().navigate(
//                R.id.action_loginFragment_to_loginparolFragment2,
//                bundle
//            )
            checkUsername(phone)
        }

        return view
    }

    private fun checkUsername(phone: String) {
        lifecycleScope.launch {
            try {
                continueButton.isEnabled = false
//                var temp = AuthRequestFullName(login)

                val response = mainAPI.getToken(phone)

                if (response.isSuccessful) {
                    temp_token = response.body()?.accessToken
                    temp_token?.let {
                        navigateToLoginParolFragment(phone, it)
                    } ?: showError("Ошибка получения временного токена")
                } else{
                    val error = response.errorBody()?.string()
                    showError(error ?: "Пользователь не найден")
                }
            } catch (e: Exception) {
                showError("Ошибка сети: ${e.message}")
            } finally {
                continueButton.isEnabled = true
            }
        }
    }

    private fun navigateToLoginParolFragment(phone: String, temp_token: String) {
        val bundle = Bundle().apply {
            putString("phone", phone)
            putString("temp_token", temp_token)
        }
        findNavController().navigate(
            R.id.action_loginFragment_to_loginparolFragment2,
            bundle
        )
    }

    private fun isValidLogin(login: String): Boolean {
        // Реализуйте проверку логина (email или телефон)
        return login.isNotEmpty() // Базовая проверка
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}


//class LoginFragment : Fragment() {
//    private lateinit var mainAPI: MainAPI
//    private lateinit var okButton: Button
//    private lateinit var loginEditText: EditText
//    private lateinit var passwordEditText: EditText // Добавляем поле для пароля
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val view = inflater.inflate(R.layout.fragment_login, container, false)
//
//        okButton = view.findViewById(R.id.okei_to_loginparol)
//        loginEditText = view.findViewById(R.id.login_to_vxod)
////        passwordEditText = view.findViewById(R.id.parol_to_vxod) // Предполагаем, что такой элемент есть
//
//        // Инициализация Retrofit с логгированием
//        val retrofit = Retrofit.Builder()
//            .baseUrl("http://10.0.2.2:8000/")
//            .client(
//                OkHttpClient.Builder()
//                    .addInterceptor(HttpLoggingInterceptor().apply {
//                        level = HttpLoggingInterceptor.Level.BODY
//                    })
//                    .build()
//            )
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        mainAPI = retrofit.create(MainAPI::class.java)
//
//        // Блокировка кнопки по умолчанию
//        updateButtonState()
//
//        // Проверка ввода логина и пароля
//        val textWatcher = object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) = updateButtonState()
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//        }
//
//        loginEditText.addTextChangedListener(textWatcher)
//        passwordEditText.addTextChangedListener(textWatcher)
//
//        okButton.setOnClickListener {
//            viewLifecycleOwner.lifecycleScope.launch {
//                try {
//                    val response = mainAPI.getToken(
//                        username = loginEditText.text.toString(),
//                        password = passwordEditText.text.toString()
//                    )
//
//                    withContext(Dispatchers.Main) {
//                        if (response.isSuccessful) {
//                            val token = response.body()?.accessToken
//                            token?.let {
//                                // Сохраняем токен и переходим дальше
//                                saveToken(it)
//                                findNavController().navigate(R.id.action_loginFragment_to_loginparolFragment2)
//                            } ?: showError("Пустой токен в ответе")
//                        } else {
//                            val error = response.errorBody()?.string()
//                            showError("Ошибка сервера: ${error ?: response.code()}")
//                        }
//                    }
//                } catch (e: Exception) {
//                    withContext(Dispatchers.Main) {
//                        showError("Ошибка сети: ${e.message}")
//                    }
//                }
//            }
//        }
//
//        return view
//    }
//
//    private fun updateButtonState() {
//        okButton.isEnabled = loginEditText.text.toString().trim().isNotEmpty() &&
//                passwordEditText.text.toString().trim().isNotEmpty()
//    }
//
//    private fun showError(message: String) {
//        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
//    }
//
//    private fun saveToken(token: String) {
//        // Реализация сохранения токена (SharedPreferences, EncryptedSharedPref и т.д.)
//    }
//}

//class LoginFragment : Fragment() {
//    private lateinit var mainAPI: MainAPI
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
//                              savedInstanceState: Bundle?): View {
//        val view = inflater.inflate(R.layout.fragment_login, container, false)
//
//        val okButton = view.findViewById<Button>(R.id.okei_to_loginparol)
//        val loginEditText = view.findViewById<EditText>(R.id.login_to_vxod)
//
////        val logging = HttpLoggingInterceptor().apply {
////            level = HttpLoggingInterceptor.Level.BODY
////        }
////        val client = OkHttpClient.Builder()
////            .addInterceptor(logging)
////            .build()
//
//        // Инициализация Retrofit
//        val retrofit = Retrofit.Builder()
//            .baseUrl("http://10.0.2.2:8000/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        mainAPI = retrofit.create(MainAPI::class.java)
//
//        // Блокировка кнопки по умолчанию
//        okButton.isEnabled = false
//
//        // Проверка ввода логина
//        loginEditText.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                okButton.isEnabled = s.toString().trim().isNotEmpty()
//            }
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//        })
//
//        okButton.setOnClickListener {
//            CoroutineScope(Dispatchers.IO).launch {
//
//                try {
//                    val response = mainAPI.getToken(
//                        username = "12345678",
//                        password = "12345678"
//                    )
//
//                    if (response.isSuccessful) {
//                        val token = response.body()?.accessToken
//                        // Обработка успешного ответа
//                    } else {
//                        // Обработка ошибки
//                        val errorBody = response.errorBody()?.string()
//                        Log.e("API_ERROR", "Error: $errorBody")
//                    }
//                } catch (e: Exception) {
//                    Log.e("NETWORK_ERROR", "Exception: ${e.message}")
//                }
//
////                try {
////                    val response = mainAPI.auth(
////                        AuthRequestFullName(loginEditText.text.toString())
////                    )
////
////                    activity?.runOnUiThread {
////                        if (response != null) {  // Простая проверка на успешность
////                            view.findNavController()
////                                .navigate(R.id.action_loginFragment_to_loginparolFragment2)
////                        }
////                    }
////                } catch (e: Exception) {
////                    e.printStackTrace()
////                }
//            }
//        }
//
//        return view
//    }
//}


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