package com.example.vkr;

import static android.content.ContentValues.TAG;

import static androidx.fragment.app.FragmentManagerKt.commit;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.vkr.databinding.ActivityLoginBinding;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;
    EditText userLogin;
    EditText userPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        // навигация по кнопкам авторизации + авторизация

        userLogin = findViewById(R.id.usrLgn);
        userPassword = findViewById(R.id.usrPswd);
        MaterialButton lgnBtn = findViewById(R.id.lgnBtn);
        TextView regBtn = findViewById(R.id.registerBtn);

        lgnBtn.setOnClickListener(v ->{
            loginUser();
        });

        regBtn.setOnClickListener(v ->{
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });


    }


    private void loginUser(){
        String email = userLogin.getText().toString();
        String password = userPassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            userLogin.setError("Поле не может быть пустым!");
            userLogin.requestFocus();
        }
        else if(TextUtils.isEmpty(password)){
            userPassword.setError("Поле не может быть пустым!");
            userPassword.requestFocus();
        }
        else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Добро пожаловать!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Неверный логин или пароль! Повторите попытку!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

}