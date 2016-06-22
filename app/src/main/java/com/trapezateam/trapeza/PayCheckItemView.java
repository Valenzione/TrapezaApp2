package com.trapezateam.trapeza;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PayCheckItemView extends LinearLayout {

    private ImageButton mAddButton, mRemoveButton, mDeleteButton;
    private TextView mItemName;
    private EditText mItemNumber;

    public PayCheckItemView(Context context) {
        super(context);
        initializeViews(context);
    }

    public PayCheckItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public PayCheckItemView(Context context,
                            AttributeSet attrs,
                            int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(context);
    }

    /**
     * Inflates the views in the layout.
     *
     * @param context
     *           the current context for the view.
     */
    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.bill_entry, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }
}