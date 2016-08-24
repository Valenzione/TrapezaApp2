package com.trapezateam.trapeza;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ilgiz on 8/11/16.
 */
public class SharedPreferencesHelper {

    private static final String SHARED_PREFERENCES_NAME = "trapeza_shared_preferences";

    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_ROLE = "role";

    Context mContext;

    private SharedPreferences getPreferences() {
        return mContext.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public SharedPreferencesHelper(Context context) {
        mContext = context;
    }

    public void saveToken(String token) {
        SharedPreferences.Editor e = getPreferences().edit();
        e.putString(KEY_TOKEN, token);
        e.commit();
    }

    public void saveUserId(int userId) {
        SharedPreferences.Editor e = getPreferences().edit();
        e.putInt(KEY_USER_ID, userId);
        e.commit();
    }


    public void saveRole(int role) {
        SharedPreferences.Editor e = getPreferences().edit();
        e.putInt(KEY_ROLE, role);
        e.commit();
    }

    public String getToken() {
        return getPreferences().getString(KEY_TOKEN, null);
    }

    /**
     *
     * @return saved user id or <code>-1</code> if there was no saved user id
     */
    public int getUserId() {
        return getPreferences().getInt(KEY_USER_ID, -1);
    }

    /**
     *
     * @return save role or <code>-1</code> if there was no saved role
     */
    public int getRole() { return getPreferences().getInt(KEY_ROLE, -1);}

    public void removeToken() {
        saveToken(null);
    }

    public void removeUserId() {
        SharedPreferences.Editor e = getPreferences().edit();
        e.remove(KEY_USER_ID);
    }

    public boolean hasSavedProfile() {
        return getToken() != null;
    }


}
