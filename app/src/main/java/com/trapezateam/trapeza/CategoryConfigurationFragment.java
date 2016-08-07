package com.trapezateam.trapeza;

import android.app.Activity;
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

import com.trapezateam.trapeza.api.TrapezaRestClient;
import com.trapezateam.trapeza.api.models.SaveCompleteResponse;
import com.trapezateam.trapeza.api.models.StatusResponse;
import com.trapezateam.trapeza.database.Category;
import com.trapezateam.trapeza.database.RealmClient;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryConfigurationFragment extends AdministratorActivityFragment {

    private static final String TAG = "CategoryConfig";
    public static final String KEY_CATEGORY = "category";
    private static int IMAGE_PICKER_SELECT = 1;

    private ImageView mCategoryImage;
    private EditText mCategoryName;
    private Button mSaveCategory;

    private Category editCategory;

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
        mSaveCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCategory(String.valueOf(mCategoryName.getText()));
                returnToMenu();
            }
        });

        if (getArguments() != null) {
            if (getArguments().containsKey(KEY_CATEGORY)) {
                editCategory = (Category) getArguments().get(KEY_CATEGORY);
                mCategoryName.setText(editCategory.getName());
            }
        }

    }

    private void returnToMenu() {
        getAdministratorActivity().startMenuFragment(true);
    }

    private void saveCategory(String categoryName) {
        final Category category;
        final Realm realm = Realm.getDefaultInstance();
        if (getArguments() != null) {
            realm.beginTransaction();
            category = (Category) getArguments().get(KEY_CATEGORY);
            category.setName(categoryName);
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
            realm.commitTransaction();
        } else {

            realm.beginTransaction();
            category = realm.createObject(Category.class);
            category.setName(categoryName);
            realm.commitTransaction();

            TrapezaRestClient.CategoryMethods.create(category, new Callback<SaveCompleteResponse>() {
                @Override
                public void onResponse(Call<SaveCompleteResponse> call, final Response<SaveCompleteResponse> response) {
                    Toast toast;
                    if (response.body().isSuccess()) {
                        toast = Toast.makeText(getAdministratorActivity(), "Категория успешно добавлена", Toast.LENGTH_SHORT);
                        realm.executeTransaction(new Realm.Transaction() {
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


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_PICKER_SELECT
                && resultCode == Activity.RESULT_OK) {
            String path = getPathFromCameraData(data, this.getAdministratorActivity());
            Log.i("PICTURE", "Path: " + path);
            if (path != null) {
                Bitmap src = BitmapFactory.decodeFile(path);
                int width = src.getWidth();
                int height = src.getHeight();
                int crop = (width - height) / 2;
                Bitmap cropImg = Bitmap.createBitmap(src, crop, 0, height, height);
                mCategoryImage.setImageBitmap(cropImg);
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
}
