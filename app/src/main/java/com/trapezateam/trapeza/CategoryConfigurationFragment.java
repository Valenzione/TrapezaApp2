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
import android.widget.TextView;
import android.widget.Toast;

import com.trapezateam.trapeza.api.TrapezaRestClient;
import com.trapezateam.trapeza.api.models.ModifiedCategoryResponse;
import com.trapezateam.trapeza.api.models.SavedCategoryResponse;
import com.trapezateam.trapeza.database.Category;
import com.trapezateam.trapeza.database.RealmClient;

import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryConfigurationFragment extends Fragment {

    private static final String TAG = "CategoryConfigyrat";
    private static int IMAGE_PICKER_SELECT = 1;

    private ImageView mCategoryImage;
    private EditText mCategoryName;
    private Button mSaveCategory;

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
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                Category category;
                if (getArguments() != null) {
                    category = (Category) getArguments().get("category");
                } else {
                    category = new Category();
                }
                category.setName(String.valueOf(mCategoryName.getText()));
                realm.commitTransaction();
                saveCategory(category);
            }
        });


    }

    private void saveCategory(Category category) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Saving category");
        dialog.setCancelable(false);
        dialog.show();
        if (getArguments() != null) {
            TrapezaRestClient.modifyCategory(category, new Callback<List<ModifiedCategoryResponse>>() {
                @Override
                public void onResponse(Call<List<ModifiedCategoryResponse>> call, Response<List<ModifiedCategoryResponse>> response) {
                    if (response.body().get(0).getStatus() == 1) {
                        Log.d(TAG, "Category Modified");
                    } else {
                        Log.d(TAG, "Category Not Modified");
                    }
                }

                @Override
                public void onFailure(Call<List<ModifiedCategoryResponse>> call, Throwable t) {
                    Toast.makeText(getActivity(), "Error updating category " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                    t.printStackTrace();

                }
            });
        } else {
            TrapezaRestClient.addCategory(category, new Callback<List<SavedCategoryResponse>>() {
                @Override
                public void onResponse(Call<List<SavedCategoryResponse>> call, Response<List<SavedCategoryResponse>> response) {
                    if (response.body().get(0).getStatus() == 1) {
                        Log.d(TAG, "Category Saved");
                    } else {
                        Log.d(TAG, "Category Not Saved");
                    }
                }

                @Override
                public void onFailure(Call<List<SavedCategoryResponse>> call, Throwable t) {
                    Toast.makeText(getActivity(), "Error saving category " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                    t.printStackTrace();
                }
            });
        }
        RealmClient.updateDatabase(TrapezaApplication.getCompany());
        dialog.dismiss();
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
