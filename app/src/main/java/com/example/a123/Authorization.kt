package com.example.a123

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.a123.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Authorization : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_autorization)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "app-database"
        ).build()

        val userLogin: EditText = findViewById(R.id.user_login)
        val userPass: EditText = findViewById(R.id.user_pass)
        val buttonLogin: Button = findViewById(R.id.button_login) // Кнопка для входа
        val buttonRegister: Button = findViewById(R.id.button_reg) // Кнопка для регистрации

        // Обработчик кнопки для входа
        buttonLogin.setOnClickListener {
            val login = userLogin.text.toString().trim()
            val pass = userPass.text.toString().trim()

            if (login == "" || pass == "") {
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
            } else {
                // Проверка на существование пользователя в базе данных
                CoroutineScope(Dispatchers.IO).launch {
                    val user = db.userDao().getUserByLoginAndPassword(login, pass)
                    withContext(Dispatchers.Main) {
                        if (user != null) {
                            // Авторизация успешна
                            Toast.makeText(this@Authorization, "Авторизация успешна", Toast.LENGTH_SHORT).show()

                            // Переход на другую активность
                            val intent = Intent(this@Authorization, MainActivity::class.java)
                            startActivity(intent)
                            finish() // Закрыть активность авторизации
                        } else {
                            // Неверный логин или пароль
                            Toast.makeText(this@Authorization, "Неверный логин или пароль", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        // Обработчик кнопки для регистрации нового пользователя
        buttonRegister.setOnClickListener {
            val login = userLogin.text.toString().trim()
            val pass = userPass.text.toString().trim()

            if (login == "" || pass == "") {
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
            } else {
                // Проверка на существование пользователя в базе данных
                CoroutineScope(Dispatchers.IO).launch {
                    val existingUser = db.userDao().getUserByLogin(login) // Получаем пользователя по логину
                    withContext(Dispatchers.Main) {
                        if (existingUser != null) {
                            // Пользователь с таким логином уже существует
                            Toast.makeText(this@Authorization, "Пользователь с таким логином уже существует", Toast.LENGTH_SHORT).show()
                        } else {
                            // Регистрация нового пользователя
                            val newUser = User(login = login, password = pass)
                            CoroutineScope(Dispatchers.IO).launch {
                                db.userDao().insert(newUser) // Сохраняем нового пользователя
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(this@Authorization, "Регистрация успешна", Toast.LENGTH_SHORT).show()

                                    // После регистрации можно авторизоваться
                                    val intent = Intent(this@Authorization, MainActivity::class.java)
                                    startActivity(intent)
                                    finish() // Закрыть активность авторизации
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
