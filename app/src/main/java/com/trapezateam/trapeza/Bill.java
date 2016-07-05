package com.trapezateam.trapeza;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.trapezateam.trapeza.models.Dish;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ilgiz on 6/18/16.
 */
public class Bill extends RecyclerView.Adapter<Bill.ViewHolder> implements Parcelable {

    protected Bill(Parcel in) {
        in.readList(mEntires, null);
    }

    public static final Creator<Bill> CREATOR = new Creator<Bill>() {
        @Override
        public Bill createFromParcel(Parcel in) {
            return new Bill(in);
        }

        @Override
        public Bill[] newArray(int size) {
            return new Bill[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(mEntires);
    }

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
        int index = -1;
        for (int i = 0; i < mEntires.size(); i++) {
            if (mEntires.get(i).getDish().equals(entry)) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            incrementQuantity(index);
            Log.d("CashierAc", "AlreadyThere");
        } else {
            mEntires.add(new BillEntry(entry, 1));
            notifyItemInserted(getItemCount() - 1);
        }
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        BillEntry e = mEntires.get(position);
        holder.mName.setText(e.getDish().getName() + System.getProperty("line.separator") + e.getDish().getDescription());
        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int p = holder.getAdapterPosition();
                if (p == RecyclerView.NO_POSITION) {
                    return;
                }
                removeEntry(p);
            }
        });
        holder.mDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int p = holder.getAdapterPosition();
                if (p == RecyclerView.NO_POSITION) {
                    return;
                }
                decrementQuantity(p);
            }
        });
        holder.mIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int p = holder.getAdapterPosition();
                if (p == RecyclerView.NO_POSITION) {
                    return;
                }
                incrementQuantity(p);
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

    @Override
    public String toString() {
        String out = "";
        for (BillEntry e : mEntires) {
            out += e.getDish().getName() + " " + e.getPrice() + "\n";
        }
        return out;
    }
}

