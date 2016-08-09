package com.trapezateam.trapeza;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import com.squareup.picasso.Picasso;
import com.trapezateam.trapeza.api.TrapezaRestClient;
import com.trapezateam.trapeza.api.models.SaveCompleteResponse;
import com.trapezateam.trapeza.api.models.StatusResponse;
import com.trapezateam.trapeza.api.models.UploadResponse;
import com.trapezateam.trapeza.database.Category;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryConfigurationFragment extends AdministratorActivityFragment {

    private static final String TAG = "CategoryConfig";
    public static final String KEY_CATEGORY = "category";
    private static int IMAGE_PICKER_SELECT = 1;
    private static String imagePath;


    private ImageView mCategoryImage;
    private EditText mCategoryName;
    private Button mSaveCategory;
    Drawable oldDrawable;

    private Category editCategory, category;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_configuration_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        mCategoryImage = (ImageView) view.findViewById(R.id.category_image);
        mCategoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, IMAGE_PICKER_SELECT);
            }
        });

        mCategoryName = (EditText) view.findViewById(R.id.category_name);
        mSaveCategory = (Button) view.findViewById(R.id.save_category_button);

        if (getArguments() != null) {
            if (getArguments().containsKey(KEY_CATEGORY)) {
                editCategory = (Category) getArguments().get(KEY_CATEGORY);
                mCategoryName.setText(editCategory.getName());
                Picasso.with(getActivity()).setLoggingEnabled(true);
                Picasso.with(getActivity()).load(TrapezaRestClient.getFileUrl(editCategory.getPhotoUrl())).into(mCategoryImage);
                Picasso.with(getActivity()).setLoggingEnabled(false);
            }
        }

        mSaveCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                if (getArguments() != null) {
                    category = (Category) getArguments().get(KEY_CATEGORY);
                } else {
                    category = realm.createObject(Category.class);
                }
                category.setName(String.valueOf(mCategoryName.getText()));
                Realm.getDefaultInstance().commitTransaction();
                saveCategory(category, ((BitmapDrawable) mCategoryImage.getDrawable()).getBitmap());
                returnToMenu();

            }
        });


    }

    private void returnToMenu() {
        getAdministratorActivity().startMenuFragment(true);
    }

    private void uploadImage(Bitmap bitmap) {


    }

    private void saveCategory(final Category category, Bitmap bitmap) {

        TrapezaRestClient.UploadMethods.uploadImage(bitmap, new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, final Response<UploadResponse> response) {
                Log.d(TAG, "Message: " + response.body().getMessage());
                Log.d(TAG, "Succes " + response.body().isSuccess());
                if (response.body().isSuccess()) {
                    Realm.getDefaultInstance().beginTransaction();
                    category.setPhotoUrl(response.body().getPath());
                    Realm.getDefaultInstance().commitTransaction();

                    if (getArguments() != null) {
                        TrapezaRestClient.CategoryMethods.update(category, new Callback<StatusResponse>() {
                            @Override
                            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                                Toast toast;
                                if (response.body().isSuccess()) {
                                    toast = Toast.makeText(getAdministratorActivity(), "Категория успешно изменена", Toast.LENGTH_SHORT);
                                } else {
                                    toast = Toast.makeText(getAdministratorActivity(), "Произошла ошибка, категория не изменена", Toast.LENGTH_SHORT);
                                }
                                toast.show();
                            }

                            @Override
                            public void onFailure(Call<StatusResponse> call, Throwable t) {
                                Toast.makeText(getAdministratorActivity(), "Error updating category " + t.getMessage(),
                                        Toast.LENGTH_LONG).show();
                                t.printStackTrace();

                            }
                        });

                    } else {


                        TrapezaRestClient.CategoryMethods.create(category, new Callback<SaveCompleteResponse>() {
                            @Override
                            public void onResponse(Call<SaveCompleteResponse> call, final Response<SaveCompleteResponse> response) {
                                Toast toast;
                                if (response.body().isSuccess()) {
                                    toast = Toast.makeText(getAdministratorActivity(), "Категория успешно добавлена", Toast.LENGTH_SHORT);
                                    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            RealmResults<Category> categories = realm.where(Category.class).equalTo("categoryId", 0).findAll();
                                            categories.first().setCategoryId(response.body().getId());
                                        }
                                    });
                                } else {
                                    toast = Toast.makeText(getAdministratorActivity(), "Произошла ошибка, категория не добавлена", Toast.LENGTH_SHORT);

                                }
                                toast.show();
                            }

                            @Override
                            public void onFailure(Call<SaveCompleteResponse> call, Throwable t) {
                                Toast.makeText(getAdministratorActivity(), "Error saving category " + t.getMessage(),
                                        Toast.LENGTH_LONG).show();
                                t.printStackTrace();
                            }
                        });

                    }
                }
            }

            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                t.printStackTrace();
                Log.d(TAG, t.toString());
            }
        });


    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_PICKER_SELECT
                && resultCode == Activity.RESULT_OK) {
            Bitmap src = null;
            try {
                src = MediaStore.Images.Media.getBitmap(
                        getAdministratorActivity().getContentResolver(),
                        data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
            int width = src.getWidth();
            int height = src.getHeight();
            Bitmap cropImg;
            if (width > height) {
                int crop = (width - height) / 2;
                cropImg = Bitmap.createBitmap(src, crop, 0, height, height);
            } else {
                int crop = (height - width) / 2;
                cropImg = Bitmap.createBitmap(src, 0, crop, width, width);
            }

            mCategoryImage.setImageBitmap(cropImg);
        }
    }


}
