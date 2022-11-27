package hcmute.edu.vn.foody;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import hcmute.edu.vn.foody.adapter.FoodAllAdapter;
import hcmute.edu.vn.foody.adapter.RestaurantAdapter;
import hcmute.edu.vn.foody.db.Database;
import hcmute.edu.vn.foody.entity.Food;
import hcmute.edu.vn.foody.entity.Restaurant;

public class RestaurantActivity extends AppCompatActivity {
    ImageView img_restaurant, img_saved, imageViewBack;
    TextView txt_name, txt_address;
    RatingBar rb;
    FoodAllAdapter adapter;
    ArrayList<Food> listFood;
    RecyclerView nearResRecycle, lv_food;
    ArrayList<Restaurant> nearList;
    RestaurantAdapter nearRestaurantAdapter;
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        map();
        dataInit();

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void map() {
        img_restaurant = findViewById(R.id.img_foody);
        txt_name = findViewById(R.id.txt_name_restaurant);
        lv_food = findViewById(R.id.lv_comment);
        rb = findViewById(R.id.ratingBar3);
        img_saved = findViewById(R.id.img_save_location);
        txt_address = findViewById(R.id.txt_restaurant_address);
        nearResRecycle = findViewById(R.id.near_restaurant_recycler);
        imageViewBack = findViewById(R.id.imageView_back);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getDataFoods(int id_res) {
        Cursor dataFoods = database.GetData("SELECT * FROM Food WHERE Id_res = " + id_res + "");
        listFood.clear();
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

            listFood.add(new Food(id, restaurant, name, price, image));
        }
        adapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getDataRestaurants(int id_res) {
        Cursor dataRestaurants = database.GetData("SELECT * FROM Restaurant WHERE Id <> " + id_res + " ORDER BY Id DESC LIMIT 5");
        nearList.clear();
        while (dataRestaurants.moveToNext()) {
            int id = dataRestaurants.getInt(0);
            String name = dataRestaurants.getString(1);
            String address = dataRestaurants.getString(2);
            int image = dataRestaurants.getInt(3);

            nearList.add(new Restaurant(id, name, address, image));
        }
        nearRestaurantAdapter.notifyDataSetChanged();
    }

    private void dataInit() {
        database = new Database(this, "foody.sqlite", null, 1);
        listFood = new ArrayList<>();
        nearList = new ArrayList<>();

        //Get restaurant info
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        String name = intent.getStringExtra("name");
        int image = intent.getIntExtra("image", 0);
        String address = intent.getStringExtra("address");

        //Set restaurant info
        txt_name.setText(name);
        img_restaurant.setImageResource(image);
        txt_address.setText(address);

        //Set food info
        adapter = new FoodAllAdapter(this, R.layout.items_allmenu_recycler, listFood);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        lv_food.setLayoutManager(layoutManager1);
        getDataFoods(id);
        lv_food.setAdapter(adapter);

        //Set other restaurants info
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        nearResRecycle.setLayoutManager(layoutManager);
        nearRestaurantAdapter = new RestaurantAdapter(this, R.layout.items_restaurant_recycler, nearList);
        getDataRestaurants(id);
        nearResRecycle.setAdapter(nearRestaurantAdapter);

        float rating = intent.getFloatExtra("rating", 4);

        txt_name.setText(name);
        img_restaurant.setImageResource(image);
        txt_address.setText(address);
        rb.setRating(rating);

    }
}