package com.trapezateam.trapeza;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.trapezateam.trapeza.database.Dish;
import com.trapezateam.trapeza.database.RealmClient;
import com.trapezateam.trapeza.database.User;

import butterknife.Bind;
import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

/**
 * Created by Yuriy on 7/27/2016.
 */
public class StaffManagementFragment extends AdministratorActivityFragment {

    ListView mStaffList;

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
        mStaffList.setAdapter(new StaffAdapter(getActivity(), RealmClient.getUsers()));
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
                viewHolder.mStaffPhoto = (ImageView) view.findViewById(R.id.staff_photo);
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
                    getAdministratorActivity().startUserConfigurationFragment((User) adapterData.get(i),true);
                }
            });

            return view;
        }

        private class ViewHolder {
            TextView mStaffName;
            ImageView mStaffPhoto;
            Button mDeleteStaff;
            Button mModifyStaff;
        }
    }
}
