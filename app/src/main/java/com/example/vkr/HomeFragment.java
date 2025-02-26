package com.example.vkr;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.ListFormatter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class HomeFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        RelativeLayout addNewService = getView().findViewById(R.id.addNewService);
        Button addNewServiceBtn = getView().findViewById(R.id.addNewServiceBtn);
        MaterialButton addMeterBtn = getView().findViewById(R.id.addNewMeterBtn);
        ImageView closeBtn = getView().findViewById(R.id.addNewServiceCloseBtn);
        LinearLayout mainLayout = getView().findViewById(R.id.meterLayout);
        TextView mainText = getView().findViewById(R.id.mainText);

        EditText addNameOfService = getView().findViewById(R.id.addNameOfService);
        EditText addServiceTariff = getView().findViewById(R.id.addServiceTariff);

        DocumentReference docRef = db.collection("users").document(mAuth.getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot doc = task.getResult();
                if(doc.exists()){
                    mainText.setText("Ваш адрес: г. " + doc.getString("City") + " " + doc.getString("Street") + " д. " + doc.getString("House") + " кв. " + doc.getString("FlatNumber"));
                }
                else {
                    Log.e(TAG, "Документ не существует");}
            } else {
                Log.e("Firestore", "Ошибка при получении данных: " + task.getException());}
        });

        addMeterBtn.setOnClickListener(v ->{
            addNewService.setAlpha(1f);
            addNewServiceBtn.setClickable(true);
        });

        closeBtn.setOnClickListener(v ->{
            addNewService.setAlpha(0f);
            addNewServiceBtn.setClickable(false);
        });

        addNewServiceBtn.setOnClickListener(v ->{
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            Button newService = new Button(new ContextThemeWrapper(getContext(), R.style.homeBtn));
            newService.setText("Епта мать что-то добавилось");


            db.collection("users").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {

                if(task.isSuccessful()){
                    if(TextUtils.isEmpty(addNameOfService.getText())){
                        addNameOfService.setFocusable(true);
                        Toast.makeText(this.getContext(), "Поле не может быть пустым!", Toast.LENGTH_LONG).show();
                    } else if (addServiceTariff.getText().toString().isEmpty()) {
                        addServiceTariff.setFocusable(true);
                        Toast.makeText(this.getContext(), "Поле не может быть пустым!", Toast.LENGTH_LONG).show();
                    }
                    else{


                    }
                }
            });

            mainLayout.addView(newService);
            addNewService.setAlpha(0f);
            addNewServiceBtn.setClickable(false);
        });


    }
}