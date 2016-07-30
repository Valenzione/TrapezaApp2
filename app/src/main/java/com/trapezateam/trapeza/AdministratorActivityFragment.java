package com.trapezateam.trapeza;

import android.app.Fragment;
import android.content.Context;

/**
 * Created by ilgiz on 7/30/16.
 */
public abstract class AdministratorActivityFragment extends Fragment {

    private AdministratorActivity mAdministratorActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(getActivity().getClass() != AdministratorActivity.class) {
            throw new IllegalStateException("Why is this fragment not in AdministratorActivity?");
        }
        mAdministratorActivity = (AdministratorActivity) getActivity();
    }

    public AdministratorActivity getAdministratorActivity() {
        return mAdministratorActivity;
    }
}
