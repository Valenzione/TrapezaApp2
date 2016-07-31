package com.trapezateam.trapeza;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by ilgiz on 7/30/16.
 */
public abstract class AdministratorActivityFragment extends Fragment {

    private AdministratorActivity mAdministratorActivity;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.d("AdminFragment","onCreate Called");
        if(getActivity().getClass() != AdministratorActivity.class) {
            throw new IllegalStateException("Why is this fragment not in AdministratorActivity?");
        }
        mAdministratorActivity = (AdministratorActivity) getActivity();
    }

    public AdministratorActivity getAdministratorActivity() {
        return mAdministratorActivity;
    }
}
