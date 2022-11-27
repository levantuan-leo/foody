package hcmute.edu.vn.foody.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.foody.CartActivity;
import hcmute.edu.vn.foody.R;
import hcmute.edu.vn.foody.SearchActivity;
import hcmute.edu.vn.foody.adapter.FoodAllAdapter;
import hcmute.edu.vn.foody.adapter.FoodRecommendedAdapter;
import hcmute.edu.vn.foody.adapter.RestaurantAdapter;
import hcmute.edu.vn.foody.db.Database;
import hcmute.edu.vn.foody.entity.Food;
import hcmute.edu.vn.foody.entity.Restaurant;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private RecyclerView recommendRecyclerView, allMenuRecyclerView;
    private RecyclerView restaurantRecyclerView;

    private FoodAllAdapter allMenuAdapter;
    private FoodRecommendedAdapter recommendAdapter;
    private RestaurantAdapter resAdapter;

    private ArrayList<Food> foods, recommends;
    private ArrayList<Restaurant> restaurants;

    private View view;

    private EditText editTextSearch;
    private ImageView imageViewCart;

    Database database;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        mapping();
        initUI();

        editTextSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    Intent i = new Intent(view.getContext(), SearchActivity.class);
                    startActivity(i);
                    editTextSearch.clearFocus();
                }
            }
        });

        imageViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), CartActivity.class);
                startActivity(i);
            }
        });

        return view;
    }

    private void mapping() {
        editTextSearch = view.findViewById(R.id.editText_search);
        imageViewCart = view.findViewById(R.id.imageView_cart);
    }

    private void initUI() {
        //init database
        database = new Database(view.getContext(), "foody.sqlite", null, 1);

        foods = new ArrayList<>();
        recommends = new ArrayList<>();
        restaurants = new ArrayList<>();

        // get all Restaurants;
        Cursor dataRestaurants = database.GetData("SELECT * FROM Restaurant");
        restaurants.clear();
        while (dataRestaurants.moveToNext()) {
            int id = dataRestaurants.getInt(0);
            String name = dataRestaurants.getString(1);
            String address = dataRestaurants.getString(2);
            int image = dataRestaurants.getInt(3);

            restaurants.add(new Restaurant(id, name, address, image));
        }

        // get recommend food
        Cursor dataRecommends = database.GetData("SELECT * FROM Food ORDER BY Price DESC LIMIT 6");
        foods.clear();
        while (dataRecommends.moveToNext()) {
            int id = dataRecommends.getInt(0);
            Restaurant restaurant = null;
            Cursor restaurantObj = database.GetData("SELECT * FROM Restaurant WHERE Id=" + dataRecommends.getInt(1));
            while (restaurantObj.moveToNext()) {
                restaurant = new Restaurant(
                        restaurantObj.getInt(0),
                        restaurantObj.getString(1),
                        restaurantObj.getString(2),
                        restaurantObj.getInt(3));
            }
            String name = dataRecommends.getString(2);
            int price = dataRecommends.getInt(3);
            int image = dataRecommends.getInt(4);

            recommends.add(new Food(id, restaurant, name, price,image));
        }

        // get all foods
        Cursor dataFoods = database.GetData("SELECT * FROM Food");
        foods.clear();
        while (dataFoods.moveToNext()) {
            int id = dataFoods.getInt(0);
            Restaurant restaurant = null;
            Cursor restaurantObj = database.GetData("SELECT * FROM Restaurant WHERE Id=" + dataFoods.getInt(1));
            while (restaurantObj.moveToNext()) {
                restaurant = new Restaurant(
                        restaurantObj.getInt(0),
                        restaurantObj.getString(1),
                        restaurantObj.getString(2),
                        restaurantObj.getInt(3));
            }
            String name = dataFoods.getString(2);
            int price = dataFoods.getInt(3);
            int image = dataFoods.getInt(4);

            foods.add(new Food(id, restaurant, name, price,image));
        }

        getRestaurants(restaurants);
        getRecommendedData(recommends);
        getAllMenu(foods);
    }

    private void getRestaurants(List<Restaurant> restaurantList) {
        restaurantRecyclerView = view.findViewById(R.id.restaurant_recycler);
        resAdapter = new RestaurantAdapter(view.getContext(), R.layout.items_restaurant_recycler, restaurantList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);

        restaurantRecyclerView.setLayoutManager(layoutManager);
        restaurantRecyclerView.setAdapter(resAdapter);
    }

    private void getRecommendedData(List<Food> recommendList) {
        recommendRecyclerView = view.findViewById(R.id.recommended_recycler);
        recommendAdapter = new FoodRecommendedAdapter(view.getContext(), R.layout.items_recommended_recycler, recommendList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);

        recommendRecyclerView.setLayoutManager(layoutManager);
        recommendRecyclerView.setAdapter(recommendAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getAllMenu(List<Food> allMenuList) {
        allMenuRecyclerView = view.findViewById(R.id.all_menu_recycler);
        allMenuAdapter = new FoodAllAdapter(view.getContext(), R.layout.items_allmenu_recycler, allMenuList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);

        allMenuRecyclerView.setLayoutManager(layoutManager);
        allMenuRecyclerView.setAdapter(allMenuAdapter);
        allMenuAdapter.notifyDataSetChanged();

    }
}