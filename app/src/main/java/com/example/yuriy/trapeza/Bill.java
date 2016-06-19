package com.example.yuriy.trapeza;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ilgiz on 6/18/16.
 */
public class Bill extends RecyclerView.Adapter<Bill.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.bill_entry_delete)
        ImageButton mDelete;
        @Bind(R.id.bill_entry_name)
        TextView mName;
        @Bind(R.id.bill_entry_price)
        TextView mPrice;
        @Bind(R.id.bill_entry_decrement)
        ImageButton mDecrement;
        @Bind(R.id.bill_entry_quantity)
        EditText mQuantity;
        @Bind(R.id.bill_entry_increment)
        ImageButton mIncrement;
        @Bind(R.id.bill_entry_total_price)
        TextView mTotalPrice;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    private List<BillEntry> mEntires;

    public Bill() {
        mEntires = new ArrayList<>();
    }

    public void addEntry(Dish entry) {
        mEntires.add(new BillEntry(entry, 1));
        notifyItemInserted(getItemCount() - 1);
    }

    public void removeEntry(int position) {
        mEntires.remove(position);
        notifyItemRemoved(position);
    }

    public int getTotalPrice() {
        int price = 0;
        for (BillEntry e : mEntires) {
            price += e.getPrice();
        }
        return price;
    }

    public void clear() {
        int cnt = getItemCount();
        mEntires.clear();
        notifyItemRangeRemoved(0, cnt);
    }

    public void incrementQuantity(int position) {
        BillEntry e = mEntires.get(position);
        e.incrementQuantity();
        notifyItemChanged(position);
    }

    public void decrementQuantity(int position) {
        BillEntry e = mEntires.get(position);
        e.decrementQuantity();
        if (e.isEmpty()) {
            removeEntry(position);
            notifyItemRemoved(position);
        } else {
            notifyItemChanged(position);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bill_entry, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        BillEntry e = mEntires.get(position);
        holder.mName.setText(e.getDish().getName());
        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeEntry(position);
            }
        });
        holder.mDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementQuantity(position);
            }
        });
        holder.mIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementQuantity(position);
            }
        });
        holder.mPrice.setText(e.getDish().getPrice() + " руб.");
        holder.mQuantity.setText("x" + e.getQuantity());
        holder.mTotalPrice.setText(e.getPrice() + " руб.");
    }

    @Override
    public int getItemCount() {
        return mEntires.size();
    }



}
