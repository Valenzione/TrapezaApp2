package com.trapezateam.trapeza;

import android.app.Fragment;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

public class DishesFragment extends Fragment {
    final int MENU_EDIT = 4;
    final int MENU_DELETE = 5;

    String[] data = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"};

    GridView gvDishes;
    ArrayAdapter<String> adapterDishes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dishes_fragment, container, false);
        return view;
    }


      @Override
      public void onViewCreated(View view, Bundle savedInstanceState) {
          // TODO Auto-generated method stub
          super.onViewCreated(view, savedInstanceState);
          adapterDishes = new ArrayAdapter<String>(getActivity(), R.layout.square_button,R.id.squareButton, data);
          gvDishes = (GridView) getView().findViewById(R.id.gvDishes);
          gvDishes.setAdapter(adapterDishes);

      }

   /*   private View addAbstractItem(String name, int ButtonStyle, View.OnClickListener listener) {
          GridLayout gridLayout = (GridLayout) getView().findViewById(R.id.dish_fragment_layout);
          GridLayout.LayoutParams param = new GridLayout.LayoutParams();
          int viewCount = gridLayout.getChildCount();
          param.columnSpec = GridLayout.spec(viewCount % 5);
          param.rowSpec = GridLayout.spec(viewCount / 5);
          Button b = new Button(getActivity(), null, ButtonStyle);
          b.setOnClickListener(listener);
          b.setText(name);
          b.setLayoutParams(param);
          gridLayout.addView(b);
          return b;
      }

      private void addDish(Dish d) {
          View.OnClickListener categoryListener = new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  //TODO Add method
              }
          };
          View b = addAbstractItem(d.getName(), R.attr.catButtonStyle, categoryListener);
          registerForContextMenu(b);
      }

      private void addAddButton() {
          View.OnClickListener categoryListener = new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  GridLayout gridLayout = (GridLayout) getView().findViewById(R.id.dish_fragment_layout);
                  gridLayout.removeViewAt(gridLayout.getChildCount() - 1);
                  addDish(new Dish("Keleman", 1000));
                  addAddButton();
              }
          };
          addAbstractItem("+", R.attr.catButtonStyle, categoryListener);
      }
  */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, MENU_EDIT, 0, "Edit");
        menu.add(0, MENU_DELETE, 0, "Delete");
    }
}