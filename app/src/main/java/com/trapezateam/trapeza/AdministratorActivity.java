package com.trapezateam.trapeza;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.trapezateam.trapeza.database.Category;
import com.trapezateam.trapeza.database.Dish;
import com.trapezateam.trapeza.database.User;

public class AdministratorActivity extends AppCompatActivity {

    private static final String TAG = "AdministratorActivity";

    private static final int MENU_IDENTIFIER = 0;
    private static final int STATISTICS_IDENTIFIER = 1;
    private static final int ADD_DISH_IDENTIFIER = 2;
    private static final int ADD_CATEGORY_IDENTIFIER = 3;
    private static final int STAFF_IDENTIFIER = 4;
    private static final int ADD_USER_IDENTIFIER = 5;
    private static final int LOG_OUT_IDENTIFIER = 6;

    private static final String[] FRAGMENT_TAGS = {"MENU", "STATISTICS", "ADD_DISH", "ADD_CATEGORY", "STAFF", "ADD_USER"};


    Drawer.Result drawerResult;

    AdministratorActivityFragment mCurrentFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.administrator_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerResult = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withHeader(R.layout.drawer_header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Управление блюдами и категориями").withIcon(FontAwesome.Icon.faw_tasks).withIdentifier(MENU_IDENTIFIER),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_statistics).withIcon(FontAwesome.Icon.faw_area_chart).withIdentifier(STATISTICS_IDENTIFIER),
                        new PrimaryDrawerItem().withName("Управление персоналом").withIcon(FontAwesome.Icon.faw_wheelchair).withIdentifier(STAFF_IDENTIFIER),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("Добавить нового пользователя").withIcon(FontAwesome.Icon.faw_plus).withIdentifier(ADD_USER_IDENTIFIER),
                        new SecondaryDrawerItem().withName("Закончить смену").withIcon(FontAwesome.Icon.faw_power_off).withIdentifier(LOG_OUT_IDENTIFIER)
                )


                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            if (drawerItem.getIdentifier() == LOG_OUT_IDENTIFIER) {
                                SharedPreferencesHelper helper = new SharedPreferencesHelper(AdministratorActivity.this);
                                helper.removeToken();
//                                android.os.Process.killProcess(android.os.Process.myPid());
                                finish();
                                return;
                            }
                            startFragment(drawerItem.getIdentifier(), true);
                        }
                    }
                }).build();

        startMenuFragment(true);
    }

    private void startFragment(int identifier, boolean newBackStack) {
        switch (identifier) {
            case STATISTICS_IDENTIFIER:
                startStatisticsFragment(newBackStack);
                break;
            case MENU_IDENTIFIER:
                startMenuFragment(newBackStack);
                break;
            case STAFF_IDENTIFIER:
                startStaffManagementFragment(newBackStack);
                break;
            case ADD_USER_IDENTIFIER:
                startUserCreationFragment(newBackStack);
                break;
            default:
                startMenuFragment(newBackStack);
                break;
        }
    }

    public void startStatisticsFragment(boolean newBackStack) {
        startFragment(new StatisticsFragment(), newBackStack);
    }


    public void startMenuFragment(boolean newBackStack) {
        startFragment(new MenuFragment(), newBackStack);
    }

    public void startCategoryConfigurationFragment(boolean newBackStack) {
        startFragment(new CategoryConfigurationFragment(), newBackStack);
    }

    public void startDishConfigurationFragment(@Nullable Dish dish, @Nullable Integer categoryId, boolean newBackStack) {
        Bundle arguments = new Bundle();
        if (dish != null) {
            arguments.putParcelable(DishConfigurationFragment.KEY_DISH, dish);
        }
        if (categoryId != null) {
            arguments.putInt(DishConfigurationFragment.KEY_CATEGORY_ID, categoryId);
        }
        AdministratorActivityFragment fragment = new DishConfigurationFragment();
        fragment.setArguments(arguments);
        startFragment(fragment, newBackStack);
    }

    public void startStaffManagementFragment(boolean newBackStack) {
        startFragment(new StaffManagementFragment(), newBackStack);
    }

    public void startUserCreationFragment(boolean newBackStack) {
        startFragment(new UserCreationFragment(), newBackStack);
    }

    public void startUserConfigurationFragment(User user, boolean newBackStack) {
        UserConfigurationFragment fragment = new UserConfigurationFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(UserConfigurationFragment.KEY_USER, user);
        fragment.setArguments(arguments);
        startFragment(fragment, newBackStack);
    }


    private void startFragment(AdministratorActivityFragment fragment, boolean newBackStack) {
        if (newBackStack) {
            clearFragmentBackStack();
        }
        FragmentTransaction transaction = getFragmentManager().beginTransaction()
                .replace(R.id.dummy_fragment, fragment);
        if (!newBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
        mCurrentFragment = fragment;
    }

    private void clearFragmentBackStack() {
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }


    @Override
    public void onBackPressed() {
        if (drawerResult.isDrawerOpen()) {
            Log.i(TAG, "Drawer is open. Closing it...");
            drawerResult.closeDrawer();
            return;
        }

        if (mCurrentFragment.getClass() == MenuFragment.class) {
            MenuFragment fragment = (MenuFragment) mCurrentFragment;
            if (fragment.onBackPressed()) {
                return;
            }
        }

        super.onBackPressed();
    }


    public void startCategoryConfigurationFragment(@Nullable Category category, boolean newBackStack) {
        Bundle arguments = new Bundle();
        if (category != null) {
            arguments.putParcelable(CategoryConfigurationFragment.KEY_CATEGORY, category);
        }
        AdministratorActivityFragment fragment = new CategoryConfigurationFragment();
        fragment.setArguments(arguments);
        startFragment(fragment, newBackStack);
    }
}
