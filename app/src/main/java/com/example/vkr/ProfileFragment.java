package com.example.vkr;

import static android.content.ContentValues.TAG;

import static java.util.Map.entry;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.vkr.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    String[] cities = {
            "Москва", "Санкт-Петербург", "Новосибирск", "Екатеринбург", "Нижний Новгород",
            "Казань", "Челябинск", "Омск", "Самара", "Ростов-на-Дону",
            "Уфа", "Красноярск", "Пермь", "Воронеж", "Волгоград",
            "Калининград", "Тюмень", "Ижевск", "Саратов", "Хабаровск"
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    String addressUID;
    DocumentReference addressRef;

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText email = getView().findViewById(R.id.emailProfile);
        EditText surname = getView().findViewById(R.id.surnameProfile);
        EditText name = getView().findViewById(R.id.nameProfile);
        EditText patronymic = getView().findViewById(R.id.patronymicProfile);
        EditText birthday = getView().findViewById(R.id.birthdayProfile);
        EditText address = getView().findViewById(R.id.addressProfile);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("users").document(uid).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot doc = task.getResult();
                Log.e("RESULT", doc.getString("Surname"));
                Log.e("RESULT", "RJRHFDJEJ");
                if(doc.exists()){
                    surname.setText(doc.getString("Surname"));
                    name.setText(doc.getString("Name"));
                    patronymic.setText(doc.getString("Patronymic"));
                    birthday.setText(doc.getString("Birthday"));
                    email.setText(mAuth.getCurrentUser().getEmail());
                    address.setText("г. " + doc.getString("City") + " " + doc.getString("Street") + " д. " + doc.getString("House") + " кв. " + doc.getString("FlatNumber"));

                }
                else {
                    Log.e(TAG, "Документ не существует");}
            } else {
                Log.e("Firestore", "Ошибка при получении данных: " + task.getException());}
        });

        MaterialButton saveBtn = getView().findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(v -> {

            String [] currAddress = address.getText().toString().trim().split(" ");

            Map<String, Object> updateProfile = new HashMap<String, Object>(){{

                put("Surname", surname.getText().toString().trim());
                put("Name", name.getText().toString().trim());
                put("Patronymic", patronymic.getText().toString().trim());
                put("Birthday", birthday.getText().toString().trim());
                put("City", currAddress[1]);
                put("Street", currAddress[2] + " " + currAddress[3]);
                put("House", currAddress[5]);
                put("FlatNumber", currAddress[7]);
            }};

            db.collection("users").document(uid).update(updateProfile);

        });


    }
}