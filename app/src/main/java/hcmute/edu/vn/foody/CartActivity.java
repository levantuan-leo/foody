package hcmute.edu.vn.foody;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;

import hcmute.edu.vn.foody.adapter.CartAdapter;
import hcmute.edu.vn.foody.db.Database;
import hcmute.edu.vn.foody.entity.Account;
import hcmute.edu.vn.foody.entity.Food;
import hcmute.edu.vn.foody.entity.OrderFood;
import hcmute.edu.vn.foody.entity.Restaurant;

public class CartActivity extends AppCompatActivity {

    private ImageView imageViewHome, imageViewBack;
    private ListView listViewCart;
    private CheckBox checkBoxAll;
    private Button buttonCheckout;
    private TextView textViewTotal;

    private ArrayList<OrderFood> cartList;
    private CartAdapter cartAdapter;

    Database database;
    private static int total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        database = new Database(this, "foody.sqlite", null, 1);

        cartList = new ArrayList<>();
        imageViewBack = findViewById(R.id.imageView_back);
        imageViewHome = findViewById(R.id.imageView_home);
        imageViewHome.setColorFilter(getResources().getColor(R.color.white));
        listViewCart = findViewById(R.id.listView_cart);
        checkBoxAll = findViewById(R.id.checkbox_all);
        buttonCheckout = findViewById(R.id.button_checkout);
        textViewTotal = findViewById(R.id.textView_totalPrice);

        cartAdapter = new CartAdapter(CartActivity.this, R.layout.items_cart, cartList);
        listViewCart.setAdapter(cartAdapter);

        getDataCart();

        total = 0;
        for(OrderFood order: cartList){
            total += order.getFood().getPrice() * order.getQuantity();
        }

        textViewTotal.setText(String.format(Locale.getDefault(), "$%d",total));

        checkBoxAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkAll(checkBoxAll.isChecked());
            }
        });

        buttonCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);

                Gson gson = new Gson();
                String json = gson.toJson(cartList);
                intent.putExtra("myCart", json);

                startActivity(intent);
            }
        });

        imageViewHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CartActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void checkAll(boolean isChecked) {
        View v;
        for (int i = 0; i < cartAdapter.getCount(); i++) {
            v = cartAdapter.getView(i, null, null);
            ((CheckBox) v.findViewById(R.id.checkbox)).setChecked(isChecked);
        }
    }

    private void getDataCart() {
        // get all foods
        Gson gson = new Gson();
        String json = getSharedPreferences("Account", MODE_PRIVATE).getString("myAccount", "");
        Account account = gson.fromJson(json, Account.class);

        Cursor dataOrderFoods = database.GetData("SELECT * FROM OrderFood WHERE Status = 0");
        cartList.clear();
        while (dataOrderFoods.moveToNext()) {
            int id = dataOrderFoods.getInt(0);

            Cursor foodObj = database.GetData("SELECT * FROM Food WHERE Id=" + dataOrderFoods.getInt(1));
            Food food = null;
            while (foodObj.moveToNext()) {
                Cursor restaurantObj = database.GetData("SELECT * FROM Restaurant WHERE Id=" + foodObj.getInt(1));
                Restaurant restaurant = null;
                while (restaurantObj.moveToNext()) {
                    restaurant = new Restaurant(
                            restaurantObj.getInt(0),
                            restaurantObj.getString(1),
                            restaurantObj.getString(2),
                            restaurantObj.getInt(3));
                }
                food = new Food(
                        foodObj.getInt(0),
                        restaurant,
                        foodObj.getString(2),
                        foodObj.getInt(3),
                        foodObj.getInt(4));
            }

            int quantity = dataOrderFoods.getInt(2);
            int price = dataOrderFoods.getInt(3);
            int status = dataOrderFoods.getInt(4);

            cartList.add(new OrderFood(id, food, quantity, price, status, account));
        }
        cartAdapter.notifyDataSetChanged();
    }

    public void updateTotal(int amount){
        total += amount;
        textViewTotal.setText(String.format(Locale.getDefault(), "$%d",total));
    }

    public void updateCartItem(int id, int amount) {
        database.QueryData("UPDATE OrderFood SET Quantity=" + amount + " WHERE Id=" + id);
    }

    public void deleteCartItem(int id) {
        database.QueryData("DELETE FROM OrderFood WHERE Id=" + id);
    }
}