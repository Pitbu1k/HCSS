package com.example.vkr;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.vkr.databinding.ItemBillBinding;

import java.util.ArrayList;
import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder> {
    private List<Bill> billList = new ArrayList<>();

    public void setBillList(List<Bill> billList) {
        this.billList = billList;
    }

    @Override
    public BillViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemBillBinding binding = ItemBillBinding.inflate(layoutInflater, parent, false);
        return new BillViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(BillViewHolder holder, int position) {
        Bill bill = billList.get(position);
        holder.bind(bill);
    }

    @Override
    public int getItemCount() {
        return billList.size();
    }

    class BillViewHolder extends RecyclerView.ViewHolder {
        private final ItemBillBinding binding;

        BillViewHolder(ItemBillBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Bill bill) {
            binding.setBill(bill);
            binding.executePendingBindings();
        }
    }

}


