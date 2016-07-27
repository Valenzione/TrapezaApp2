package com.trapezateam.trapeza;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.trapezateam.trapeza.api.TrapezaRestClient;
import com.trapezateam.trapeza.api.models.SavedDishResponse;
import com.trapezateam.trapeza.database.Dish;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DishConfigurationFragment extends Fragment {

    private static final String TAG = "DishesConfFragment";
    private static int IMAGE_PICKER_SELECT = 1;

    ImageView mDishImage;
    Button mSaveDishButton;
    EditText mDishName, mDishDescription;
    EditText mDishPrice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dish_configuration_fragment, container, false);
        if (getArguments() != null) {
            Dish dish = (Dish) getArguments().get("dish");
            EditText mDishName = (EditText) view.findViewById(R.id.dish_name);
            mDishName.setText(dish.getName());
            EditText mDishDescription = (EditText) view.findViewById(R.id.dish_description);
            mDishDescription.setText(dish.getDescription());
            EditText mDishPrice = (EditText) view.findViewById(R.id.dish_price);
            mDishPrice.setText(String.valueOf(dish.getPrice()));
        }

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        mDishImage = (ImageView) getView().findViewById(R.id.dish_image);
        mDishImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, IMAGE_PICKER_SELECT);
            }
        });

        mSaveDishButton = (Button) getView().findViewById(R.id.save_dish_button);
        mDishName = (EditText) getView().findViewById(R.id.dish_name);
        mDishDescription = (EditText) getView().findViewById(R.id.dish_description);
        mDishPrice = (EditText) getView().findViewById(R.id.dish_price);


        mSaveDishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    Log.d(TAG, "Dish saved");
                }
            }
        });
    }


    void saveDish(final Dish dish) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Saving dish");
        dialog.setCancelable(false);
        dialog.show();
        if (getArguments() == null) {
            TrapezaRestClient.addDish(dish, new Callback<List<SavedDishResponse>>() {
                @Override
                public void onResponse(Call<List<SavedDishResponse>> call,
                                       Response<List<SavedDishResponse>> response) {
                    if (response.body().get(0).getStatus() == 0) {
                        Log.d(TAG, "Dish Does Not Added");
                    } else {
                        Log.d(TAG, "Dish Added: " + dish.toString());
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<List<SavedDishResponse>> call, Throwable t) {
                    Toast.makeText(getActivity(), "Error saving dishes " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                    t.printStackTrace();
                    dialog.dismiss();

                }
            });
        } else {
            //TODO add TrapezaRestClient.modifyDish(Dish);
        }

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_PICKER_SELECT
                && resultCode == Activity.RESULT_OK) {
            String path = getPathFromCameraData(data, this.getActivity());
            Log.i("PICTURE", "Path: " + path);
            if (path != null) {
                Bitmap src = BitmapFactory.decodeFile(path);
                int width = src.getWidth();
                int height = src.getHeight();
                int crop = (width - height) / 2;
                Bitmap cropImg = Bitmap.createBitmap(src, crop, 0, height, height);
                mDishImage.setImageBitmap(cropImg);
            }
        }
    }

    public static String getPathFromCameraData(Intent data, Context context) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }

    public boolean validate() {
        boolean valid = true;

        String name = mDishName.getText().toString();
        String description = mDishDescription.getText().toString();
        String price = mDishPrice.getText().toString();

        if (name.isEmpty()) {
            mDishName.setError("Введите имя!");
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(mDishName);
            valid = false;
        } else {
            mDishName.setError(null);
        }


        if (price.isEmpty()) {
            mDishPrice.setError("Введите цену!");
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(mDishPrice);
            valid = false;
        } else {
            mDishPrice.setError(null);
        }

        return valid;
    }


}
