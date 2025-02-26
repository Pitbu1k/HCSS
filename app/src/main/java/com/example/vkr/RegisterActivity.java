package com.example.vkr;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    private EditText newUserLogin;
    private EditText newUserPassword;
    private EditText repeatNewUserPassword;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        newUserLogin = findViewById(R.id.newUsrLgn);
        newUserPassword = findViewById(R.id.newUsrPswd);
        repeatNewUserPassword = findViewById(R.id.repeatNewUsrPswd);
        MaterialButton regBtn = findViewById(R.id.newRegBtn);
        regBtn.setOnClickListener(v -> {
            createUser();
        });

        TextView backBtn = findViewById(R.id.backFromRegToAuthBtn);
        backBtn.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });

    }

    private void createUser(){
        String email = newUserLogin.getText().toString();
        String password = newUserPassword.getText().toString();
        String repeatPassword = repeatNewUserPassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            newUserLogin.setError("Поле не может быть пустым!");
            newUserLogin.requestFocus();
        }
        else if(TextUtils.isEmpty(password)){
            newUserPassword.setError("Поле не может быть пустым!");
            newUserPassword.requestFocus();
        }
        else if(TextUtils.isEmpty(repeatPassword)){
            repeatNewUserPassword.setError("Поле не может быть пустым!");
            repeatNewUserPassword.requestFocus();
        }

        if(password.equals(repeatPassword) && !TextUtils.isEmpty(password)){
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "Пользователь зарегистрирован", Toast.LENGTH_SHORT).show();
                        createUserDB();
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    }
                    else{
                        Toast.makeText(RegisterActivity.this, "Ошибка: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("RegisterException", task.getException().getMessage().toString());
                    }
                }
            });
        }
        else{
            repeatNewUserPassword.setError("Пароли не совпадают. Повторите попытку");
            repeatNewUserPassword.requestFocus();
        }
    }
    private void createUserDB(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        User user = new User();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        user.Email = currentUser.getEmail();
        user.Surname = user.Name = user.Birthday = user.Patronymic =user.Street = user.City = user.House = user.FlatNumber = "";
        db.collection("users").document(currentUser.getUid()).set(user).addOnSuccessListener(v -> {
            Log.e("ADD_USER", currentUser.getUid());
        });
    }

}

