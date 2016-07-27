package com.trapezateam.trapeza;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class AdministratorActivity extends AppCompatActivity {

    private static final String TAG = "AdministratorActivity";

    private static final int MENU_IDENTIFIER = 0;
    private static final int DISH_IDENTIFIER = 1;
    private static final int STATISTICS_IDENTIFIER = 2;
    private static final int ADD_DISH_IDENTIFIER = 3;
    private static final int ADD_CATEGORY_IDENTIFIER = 4;
    private static final int STAFF_IDENTIFIER = 5;

    Drawer.Result drawerResult;

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
                        new SecondaryDrawerItem().withName("Добавить блюдо").withIcon(FontAwesome.Icon.faw_plus).withIdentifier(ADD_DISH_IDENTIFIER),
                        new SecondaryDrawerItem().withName("Добавить категорию").withIcon(FontAwesome.Icon.faw_plus).withIdentifier(ADD_CATEGORY_IDENTIFIER)

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            startFragment(drawerItem.getIdentifier());
                        }
                    }
                }).build();

        startFragment(MENU_IDENTIFIER);
    }

    private void startFragment(int identifier) {
        Fragment replacementFragment;
        switch (identifier) {
            case STATISTICS_IDENTIFIER:
                replacementFragment = new StatisticsFragment();
                break;
            case DISH_IDENTIFIER:
                replacementFragment = new DishesFragment();
                break;
            case MENU_IDENTIFIER:
                replacementFragment = new MenuFragment();
                break;
            case ADD_CATEGORY_IDENTIFIER:
                replacementFragment = new CategoryConfigurationFragment();
                break;
            case ADD_DISH_IDENTIFIER:
                replacementFragment = new DishConfigurationFragment();
                break;
            case STAFF_IDENTIFIER:
                replacementFragment= new StaffManagementFragment();
                break;
            default:
                replacementFragment = new MenuFragment();
                break;
        }
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.dummy_fragment, replacementFragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onBackPressed() {
        if (drawerResult.isDrawerOpen()) {
            drawerResult.closeDrawer();
        } else {
            super.onBackPressed();
        }

    }


}
