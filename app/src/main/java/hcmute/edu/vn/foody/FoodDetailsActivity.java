package hcmute.edu.vn.foody;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import hcmute.edu.vn.foody.db.Database;
import hcmute.edu.vn.foody.entity.Account;

public class FoodDetailsActivity extends AppCompatActivity {

    // get data from intent and set to UI
    ImageView imageView, imageViewBack, imageViewCart, imageViewDecrease, imageViewIncrease;
    ShapeableImageView shapeableImageView;
    TextView itemName, itemPrice, itemRating, textViewResName, textViewResAddress, textViewQuantity;
    RatingBar ratingBar;

    Button buttonAddToCart;

    String name, rating, imageUrl, nameRes, adrRes;
    Integer price, image, imageRes, id;

    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        database = new Database(this, "foody.sqlite", null, 1);

        Intent intent = getIntent();

        id = intent.getIntExtra("id", 0);
        name = intent.getStringExtra("name");
        price = intent.getIntExtra("price", 0);
        rating = intent.getStringExtra("rating");
        //imageUrl = intent.getStringExtra("image");
        image = intent.getIntExtra("image", R.drawable.asiafood1);
        imageRes = intent.getIntExtra("image_res", R.drawable.restaurant);
        nameRes = intent.getStringExtra("name_res");
        adrRes = intent.getStringExtra("address_res");

        imageView = findViewById(R.id.imageView_picture);
        imageViewBack = findViewById(R.id.imageView_back);
        imageViewCart = findViewById(R.id.imageView_cart);
        buttonAddToCart = findViewById(R.id.button_addToCart);
        itemName = findViewById(R.id.name);
        itemPrice = findViewById(R.id.price);
        itemRating = findViewById(R.id.rating);
        ratingBar = findViewById(R.id.ratingBar);

        imageViewIncrease = findViewById(R.id.imageView_increase);
        imageViewDecrease = findViewById(R.id.imageView_decrease);
        textViewQuantity = findViewById(R.id.textView_quantity);

        shapeableImageView = findViewById(R.id.restaurant_image);
        textViewResName = findViewById(R.id.textView_nameRes);
        textViewResAddress = findViewById(R.id.textView_addressRes);

        // get image from a URL
        // Glide.with(getApplicationContext()).load(imageUrl).into(imageView);
        imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), image, null));
        itemName.setText(name);
        itemPrice.setText(String.format(Locale.getDefault(), "$%s", price));
        itemRating.setText(rating);
        ratingBar.setRating(Float.parseFloat(rating));
        shapeableImageView.setImageResource(imageRes);
        textViewResName.setText(nameRes);
        textViewResAddress.setText(adrRes);

        // get previous page
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // get to cart
        imageViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(i);
            }
        });

        // add to cart
        buttonAddToCart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                String json = getSharedPreferences("Account", MODE_PRIVATE).getString("myAccount", "");
                Account account = gson.fromJson(json, Account.class);

                Cursor food = database.GetData("SELECT * FROM OrderFood WHERE Status=0 AND Id_food=" + id);
                if (food.getCount() > 0) {
                    int quantity = 0;
                    int id_order = 0;
                    while (food.moveToNext()) {
                        id_order = food.getInt(0);
                        quantity = food.getInt(2);
                    }
                    database.QueryData("UPDATE OrderFood SET Quantity=" +
                            (quantity + Integer.parseInt(textViewQuantity.getText().toString())) + " WHERE Id=" + id_order);
                } else {
                    @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat(" hh:mm aa dd/MM/yyyy");
                    database.QueryData(String.format("INSERT INTO OrderFood VALUES(null, %d, %d, %d, %d, %d, '%s')",
                            id,
                            Integer.parseInt(textViewQuantity.getText().toString()),
                            price,
                            0,
                            account.getId(),
                            dateFormat.format(new Date())));
                }
                Toast.makeText(FoodDetailsActivity.this, "Added (" + name + ") to Cart", Toast.LENGTH_SHORT).show();
            }
        });

        imageViewIncrease.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View view) {
                textViewQuantity.setText(String.format("%02d", Integer.parseInt(textViewQuantity.getText().toString()) + 1));
            }
        });

        imageViewDecrease.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View view) {
                if (!textViewQuantity.getText().equals("01"))
                    textViewQuantity.setText(String.format("%02d", Integer.parseInt(textViewQuantity.getText().toString()) - 1));
            }
        });
    }
}