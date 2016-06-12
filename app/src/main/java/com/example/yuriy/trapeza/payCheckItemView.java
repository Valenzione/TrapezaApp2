package com.example.yuriy.trapeza;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class payCheckItemView extends LinearLayout {

    private ImageButton mAddButton, mRemoveButton, mDeleteButton;
    private TextView mItemName;
    private EditText mItemNumber;

    public payCheckItemView(Context context) {
        super(context);
        initializeViews(context);
    }

    public payCheckItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public payCheckItemView(Context context,
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
        inflater.inflate(R.layout.pay_check_item_view, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }
}