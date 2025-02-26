package com.example.vkr;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vkr.databinding.FragmentBillBinding;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class BillFragment extends Fragment {

    private FragmentBillBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBillBinding.inflate(inflater, container, false);

        View view = inflater.inflate(R.layout.fragment_bill, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        BillAdapter adapter = new BillAdapter();
        recyclerView.setAdapter(adapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        db.collection("users")
                .document(mAuth.getUid())
                .collection("bills")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Bill> bills = new ArrayList<>();
                        Log.e("MISTAKE", task.getResult().toString());

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            bills.add(document.toObject(Bill.class));
                        }
                        adapter.setBillList(bills);
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("MISTAKE", "SUPER MISTAKE");
                    }
                });

        return view;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*//окно счета за март
        LinearLayout marchLayout = getView().findViewById(R.id.payBillMarch);

        //окно оплаты счета, кнопка закрытия окна, кнопка оплаты
        RelativeLayout payBillWindow = getView().findViewById(R.id.payBillWindow);
        ImageView payBillCloseBtn = getView().findViewById(R.id.payBillCloseBtn);
        MaterialButton payBillPayBtn = getView().findViewById(R.id.payBillPayBtn);

        //окно успешной оплаты
        RelativeLayout payBillOkLayout = getView().findViewById(R.id.payBillOkLayout);
        ImageView payBillOkCloseBtn = getView().findViewById(R.id.payBillOkCloseBtn);

        marchLayout.setOnClickListener(v -> {
            payBillWindow.setAlpha(1.0f);
        });

        payBillCloseBtn.setOnClickListener(v -> {
            payBillWindow.setAlpha(0.0f);

        });

        payBillPayBtn.setOnClickListener(v -> {
            payBillWindow.setAlpha(0.0f);
            payBillOkLayout.setAlpha(1.0f);

        });

        payBillOkCloseBtn.setOnClickListener(v -> {
            payBillOkLayout.setAlpha(0.0f);
        });*/

    }

}