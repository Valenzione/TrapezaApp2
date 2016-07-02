package com.trapezateam.trapeza;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
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

public class AdministratorActivity extends AppCompatActivity {


    private static final int CATEGORY_IDENTIFIER = 0;
    private static final int DISH_IDENTIFIER = 1;
    private static final int STATISTICS_IDENTIFIER = 2;
    private static final String TAG = "AdministratorActivity";
    private static final int ADD_DISH_IDENTIFIER = 3;
    private static final int ADD_CATEGORY_IDENTIFIER = 4;
    Drawer.Result drawerResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.administrator_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawer.OnDrawerItemClickListener drawerListener = new Drawer.OnDrawerItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                Fragment replacementFragment;
                if (drawerItem != null) {
                    int identifier = drawerItem.getIdentifier();
                    switch (identifier) {
                        case STATISTICS_IDENTIFIER:
                            replacementFragment = new StatisticsFragment();
                            break;
                        case DISH_IDENTIFIER:
                            replacementFragment = new DishesFragment();
                            break;
                        case CATEGORY_IDENTIFIER:
                            replacementFragment = new CategoryFragment();
                            break;
                        case ADD_CATEGORY_IDENTIFIER:
                            replacementFragment = new CategoryConfigurationFragment();
                            break;
                        case ADD_DISH_IDENTIFIER:
                            replacementFragment = new DishConfigurationFragment();
                            break;
                        default:
                            replacementFragment = new CategoryFragment();
                            break;
                    }
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.dummy_fragment, replacementFragment);
                    fragmentTransaction.commit();
                }
            }
        };
        drawerResult = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withHeader(R.layout.drawer_header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_category_congiguration).withIcon(FontAwesome.Icon.faw_tasks).withIdentifier(CATEGORY_IDENTIFIER),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_dish_configration).withIcon(FontAwesome.Icon.faw_coffee).withIdentifier(DISH_IDENTIFIER),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_statistics).withIcon(FontAwesome.Icon.faw_area_chart).withIdentifier(STATISTICS_IDENTIFIER),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("Добавить блюдо").withIcon(FontAwesome.Icon.faw_plus).withIdentifier(ADD_DISH_IDENTIFIER),
                        new SecondaryDrawerItem().withName("Добавить категорию").withIcon(FontAwesome.Icon.faw_plus).withIdentifier(ADD_CATEGORY_IDENTIFIER)

                )
                .withOnDrawerItemClickListener(drawerListener).build();


        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.dummy_fragment, new CategoryFragment());
        fragmentTransaction.commit();
    }

    public void onCategoryButtonClicked(View v) {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CategoryConfigurationFragment newFragment = new CategoryConfigurationFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", ((SquareButton) v).getText().toString()); //TODO get other category info (dish pool and image)
        newFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.dummy_fragment, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    public void onDishButtonClicked(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DishConfigurationFragment newFragment = new DishConfigurationFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", ((SquareButton) v).getText().toString()); //TODO get other dish info (category and price)
        newFragment.setArguments(bundle);
        Log.d(TAG, bundle.getString("name")+"Clickedy");
        fragmentTransaction.replace(R.id.dummy_fragment, newFragment);
        fragmentTransaction.addToBackStack(null);
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
