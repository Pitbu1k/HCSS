package com.example.vkr;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private CardView mainBar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        //навигация по кнопкам mainBar
        mainBar = findViewById(R.id.mainBar);
        ImageView homeBtn = findViewById(R.id.homeBtn);
        ImageView billBtn = findViewById(R.id.billBtn);
        ImageView aboutBtn = findViewById(R.id.aboutHomeBtn);
        ImageView profileBtn = findViewById(R.id.profileBtn);
        ImageView logoutBtn = findViewById(R.id.logoutBtn);

        homeBtn.setOnClickListener(v ->{
            getSupportFragmentManager().beginTransaction().replace(R.id.main, new SecondFragment()).commit();
        });

        billBtn.setOnClickListener(v ->{
            getSupportFragmentManager().beginTransaction().replace(R.id.main, new BillFragment()).commit();
        });

        aboutBtn.setOnClickListener(v ->{
            getSupportFragmentManager().beginTransaction().replace(R.id.main, new HomeFragment()).commit();
        });

        profileBtn.setOnClickListener(v ->{
            getSupportFragmentManager().beginTransaction().replace(R.id.main, new ProfileFragment()).commit();
        });

        logoutBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            mAuth.signOut();
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if user is signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }
}