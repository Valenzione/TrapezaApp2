package com.trapezateam.trapeza;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import com.trapezateam.trapeza.api.models.SaveCompleteResponse;
import com.trapezateam.trapeza.api.models.StatusResponse;
import com.trapezateam.trapeza.api.models.UploadResponse;
import com.trapezateam.trapeza.database.Category;
import com.trapezateam.trapeza.database.Dish;
import com.trapezateam.trapeza.database.RealmClient;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DishConfigurationFragment extends AdministratorActivityFragment {

    private static final String TAG = "DishesConfFragment";
    private static int IMAGE_PICKER_SELECT = 1;

    public static final String KEY_DISH = "dish";
    public static final String KEY_CATEGORY_ID = "father";

    ImageView mDishImage;
    Button mSaveDishButton;
    EditText mDishName, mDishDescription;
    EditText mDishPrice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dish_configuration_fragment, container, false);

        if (getArguments().containsKey(KEY_DISH)) {
            Dish dish = (Dish) getArguments().get(KEY_DISH);
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
                    final Dish dish;
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    if (getArguments().containsKey(KEY_DISH)) {
                        dish = (Dish) getArguments().get(KEY_DISH);
                    } else {
                        dish = new Dish();
                        int father = (int) getArguments().get(KEY_CATEGORY_ID);
                        dish.setCategoryId(father);
                    }
                    dish.setName(String.valueOf(mDishName.getText()));
                    dish.setDescription(String.valueOf(mDishDescription.getText()));
                    dish.setPrice(Integer.parseInt(String.valueOf(mDishPrice.getText())));
                    realm.commitTransaction();
                    RealmClient.updateModel(dish);
                    saveDish(dish);
                    uploadImage(((BitmapDrawable) mDishImage.getDrawable()).getBitmap());
                    Log.d(TAG, "Dish saved");
                    returnToMenu();
                }
            }
        });
    }

    private void uploadImage(Bitmap bitmap) {
        TrapezaRestClient.UploadMethods.uploadImage(bitmap, new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                Log.d(TAG, "Succes " + response.body().isSuccess());
                if (response.body().isSuccess()) {
                    Log.d(TAG, "Path is: " + response.body().getPath() + " " + response.body().isSuccess());
                }
            }

            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                t.printStackTrace();
                Log.d(TAG, t.toString());
            }
        });
    }

    private void returnToMenu() {
        getAdministratorActivity().startMenuFragment(true);
    }


    void saveDish(final Dish dish) {
        if (getArguments().containsKey(KEY_CATEGORY_ID)) {
            TrapezaRestClient.DishMethods.create(dish, new Callback<SaveCompleteResponse>() {
                @Override
                public void onResponse(Call<SaveCompleteResponse> call,
                                       final Response<SaveCompleteResponse> response) {
                    Toast toast;
                    if (response.body().isSuccess()) {
                        toast = Toast.makeText(getAdministratorActivity(), "Блюдо успешно добавлено", Toast.LENGTH_SHORT);
                        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                RealmResults<Dish> dishes = realm.where(Dish.class).equalTo("dishId", 0).findAll();

                                if (dishes.size() != 0) {
                                    Log.d(TAG, "Everything is ok. oldId is " + dishes.first().getDishId() + ". New id is " + response.body().getId());
                                    Log.d(TAG, "Category id set is " + dishes.first().getCategoryId());
                                    dishes.first().setDishId(response.body().getId());
                                }
                            }
                        });
                    } else {
                        toast = Toast.makeText(getAdministratorActivity(), "Произошла ошибка, блюдо не добавлено", Toast.LENGTH_SHORT);
                    }
                    toast.show();

                }

                @Override
                public void onFailure(Call<SaveCompleteResponse> call, Throwable t) {
                    Toast.makeText(getAdministratorActivity(), "Error saving dish " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                    t.printStackTrace();


                }
            });
        } else {
            //TODO check for price update
            TrapezaRestClient.DishMethods.update(dish, new Callback<StatusResponse>() {
                @Override
                public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                    Toast toast;
                    if (response.body().isSuccess()) {
                        toast = Toast.makeText(getAdministratorActivity(), "Блюдо успешно изменено", Toast.LENGTH_SHORT);
                    } else {
                        toast = Toast.makeText(getAdministratorActivity(), "Произошла ошибка, блюдо не изменено", Toast.LENGTH_SHORT);
                    }
                    toast.show();

                }

                @Override
                public void onFailure(Call<StatusResponse> call, Throwable t) {
                    Toast.makeText(getAdministratorActivity(), "Error updating dish " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                    t.printStackTrace();

                }
            });

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
