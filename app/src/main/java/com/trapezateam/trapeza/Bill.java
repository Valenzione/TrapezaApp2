package com.trapezateam.trapeza;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.trapezateam.trapeza.database.Dish;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ilgiz on 6/18/16.
 */
public class Bill extends RecyclerView.Adapter<Bill.ViewHolder> implements Parcelable {

    protected Bill(Parcel in) {
        in.readList(mEntries, null);
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
        parcel.writeList(mEntries);
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

        View mView;

        public ViewHolder(View v) {
            super(v);
            mView = v;
            ButterKnife.bind(this, v);
        }
    }

    private List<BillEntry> mEntries;
    private int mDiscount;

    public Bill() {
        mEntries = new ArrayList<>();
    }

    public void addEntry(Dish entry) {
        int index = -1;
        for (int i = 0; i < mEntries.size(); i++) {
            if (mEntries.get(i).getDish().equals(entry)) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            incrementQuantity(index);
            Log.d("CashierAc", "AlreadyThere");
        } else {
            mEntries.add(new BillEntry(entry, 1));
            notifyItemInserted(getItemCount() - 1);
        }
    }

    public void removeEntry(int position) {
        mEntries.remove(position);
        notifyItemRemoved(position);
    }

    /**
     *
     * @return price after discount
     */
    public double getTotalPrice() {
        int price = 0;
        for (BillEntry e : mEntries) {
            price += e.getPrice();
        }
        return Math.ceil(price * getDiscountFraction());
    }

    /**
     *
     * @return the part of the price the customer has to pay for
     */
    public double getDiscountFraction() {
        return (1D - ((double) mDiscount / 100));
    }

    public void setDiscount(int discount) {
        mDiscount = discount;
        notifyDataSetChanged();
    }

    public int getDiscount() {
        return mDiscount;
    }

    /**
     * Resets this Bill to default state: no elements and 0% discount
     */
    public void clear() {
        mDiscount = 0;
        int cnt = getItemCount();
        mEntries.clear();
        notifyItemRangeRemoved(0, cnt);
    }

    public void incrementQuantity(int position) {
        BillEntry e = mEntries.get(position);
        e.incrementQuantity();
        notifyItemChanged(position);
    }

    public void decrementQuantity(int position) {
        BillEntry e = mEntries.get(position);
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
        BillEntry e = mEntries.get(position);
        holder.mName.setText(e.getDish().getName() + System.getProperty("line.separator") + e.getDish().getDescription());
        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int p = holder.getLayoutPosition();
                removeEntry(p);
            }
        });
        holder.mDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int p = holder.getLayoutPosition();
                decrementQuantity(p);
            }
        });
        holder.mIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int p = holder.getLayoutPosition();
                incrementQuantity(p);
            }
        });
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int p = holder.getLayoutPosition();
                incrementQuantity(p);
            }
        });
        holder.mPrice.setText(e.getDish().getPrice() + " руб.");
        holder.mQuantity.setText("x" + e.getQuantity());
        holder.mTotalPrice.setText(e.getPrice() + " руб.");
    }

    @Override
    public int getItemCount() {
        return mEntries.size();
    }

    @Override
    public String toString() {
        String out = "";
        for (BillEntry e : mEntries) {
            out += e.getDish().getName() + " " + e.getPrice() + "\n";
        }
        return out;
    }

    JSONArray getPriceIdQuantityPairs() {
        JSONArray ans = new JSONArray();
        for(BillEntry e : mEntries) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("id", e.getDish().getPriceId());
                obj.put("num", e.getQuantity());
                ans.put(obj);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return ans;
    }
}

