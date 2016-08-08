package com.trapezateam.trapeza;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.trapezateam.trapeza.api.TrapezaRestClient;
import com.trapezateam.trapeza.api.models.StatusResponse;
import com.trapezateam.trapeza.database.RealmClient;
import com.trapezateam.trapeza.database.User;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Yuriy on 7/27/2016.
 */
public class StaffManagementFragment extends AdministratorActivityFragment {

    ListView mStaffList;
    StaffAdapter mStaffAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.staff_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mStaffList = (ListView) getView().findViewById(R.id.staff_list);
        requestStaff();
    }

    private void requestStaff() {
        mStaffAdapter = new StaffAdapter(getActivity(), RealmClient.getUsers());
        mStaffList.setAdapter(mStaffAdapter);
    }

    private class StaffAdapter extends RealmBaseAdapter implements ListAdapter {

        public StaffAdapter(@NonNull Context context, @Nullable OrderedRealmCollection data) {
            super(context, data);
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;

            if (view == null) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.staff_list_entry, viewGroup, false);
                viewHolder = new ViewHolder();
                viewHolder.mStaffName = (TextView) view.findViewById(R.id.staff_name);
                viewHolder.mResetPass = (Button) view.findViewById(R.id.reset_pass_button);
                viewHolder.mDeleteStaff = (Button) view.findViewById(R.id.staff_delete_button);
                viewHolder.mModifyStaff = (Button) view.findViewById(R.id.staff_modify_button);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            User user = (User) getItem(i);
            viewHolder.mStaffName.setText(user.toString());
            viewHolder.mModifyStaff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getAdministratorActivity().startUserConfigurationFragment((User) adapterData.get(i), true);
                }
            });
            viewHolder.mDeleteStaff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteUser((User) adapterData.get(i));
                }
            });

            return view;
        }

        private class ViewHolder {
            TextView mStaffName;
            Button mResetPass;
            Button mDeleteStaff;
            Button mModifyStaff;
        }


    }

    private void deleteUser(final User user) {
        TrapezaRestClient.UserMethods.delete(user, new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                Toast toast;
                if (response.body().isSuccess()) {
                    toast = Toast.makeText(getAdministratorActivity(), "Пользователь успешно удалено", Toast.LENGTH_SHORT);
                    RealmClient.deleteModel(user);
                    mStaffAdapter.notifyDataSetChanged();
                } else {
                    toast = Toast.makeText(getAdministratorActivity(), "Произошла ошибка, Пользователь не удален", Toast.LENGTH_SHORT);
                }
                toast.show();
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Error deleting user " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
                t.printStackTrace();

            }
        });
    }
}
