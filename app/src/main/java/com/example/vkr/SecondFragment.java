package com.example.vkr;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.vkr.databinding.FragmentSecondBinding;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private LineChart lineChart;
    private LineChart lineChart2;
    private LineChart lineChart3;


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lineChart = getView().findViewById(R.id.meter);
        XAxis xAxis = lineChart.getXAxis();
        YAxis yAxis = lineChart.getAxisRight();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        yAxis.setEnabled(false);

        lineChart2 = getView().findViewById(R.id.meter2);
        xAxis = lineChart2.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        yAxis = lineChart2.getAxisRight();
        yAxis.setEnabled(false);

        lineChart3 = getView().findViewById(R.id.meter3);
        xAxis = lineChart3.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        yAxis = lineChart3.getAxisRight();
        yAxis.setEnabled(false);

        retrieveChartData("coldWaterMeter");
        retrieveChartData("warmWaterMeter");
        retrieveChartData("gasMeter");


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void retrieveChartData(String collection) {
        String userId = FirebaseAuth.getInstance().getUid();

        db.collection("users")
                .document(userId)
                .collection(collection)
                .get()

                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Entry> data = new ArrayList<>();
                    float i = 0;
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        if (!document.getId().equals("tariff")){
                            String date = document.getString("Date");
                            float indication = Float.parseFloat(document.get("Indication").toString());

                            //data.add(new Entry(parseDate(date), indication));
                            data.add(new Entry(i, indication));
                            i += 1;
                        }

                    }

                    switch (collection.contains("cold") ? 1 : collection.contains("warm") ? 2 : 3 ){
                        case 1:
                            addChart1(showChart(data));
                            break;
                        case 2:
                            addChart2(showChart(data));
                            break;
                        case 3:
                            addChart3(showChart(data));
                            break;
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreError", "Ошибка при получении данных: " + e.getMessage());
                });
    }

    private float parseDate(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        try {
            Date date = format.parse(dateString);
            Log.e("NIG", date.toString());
            return date.getTime(); // Используйте timestamp вместо даты
        } catch (ParseException e) {
            e.printStackTrace();
            return 0f;
        }
    }

    private LineData showChart(ArrayList<Entry> data) {
        LineDataSet dataSet = new LineDataSet(data, "Indications");
        LineData lineData = new LineData(dataSet);
        return lineData;
    }

    private void addChart1(LineData lineData){
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    private void addChart2(LineData lineData){
        lineChart2.setData(lineData);
        lineChart2.invalidate();
    }

    private void addChart3(LineData lineData){
        lineChart3.setData(lineData);
        lineChart3.invalidate();
    }

}

