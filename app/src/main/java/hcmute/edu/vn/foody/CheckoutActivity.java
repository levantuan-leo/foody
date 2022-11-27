package hcmute.edu.vn.foody;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.foody.db.Database;
import hcmute.edu.vn.foody.entity.Account;
import hcmute.edu.vn.foody.entity.OrderFood;

public class CheckoutActivity extends AppCompatActivity {

    private ArrayList<OrderFood> cartList;

    private TextView textViewAddress;
    private Button buttonPayment;

    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        database = new Database(this, "foody.sqlite", null, 1);

        Intent intent = getIntent();
        Gson gson = new Gson();

        textViewAddress = findViewById(R.id.textView_address);
        buttonPayment = findViewById(R.id.button_payment);

        Account account = gson.fromJson(
                getSharedPreferences("Account", MODE_PRIVATE).getString("myAccount", ""),
                Account.class);

        String json = intent.getStringExtra("myCart");
        cartList = gson.fromJson(json, new TypeToken<List<OrderFood>>() {
        }.getType());

        buttonPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment();
            }
        });

        textViewAddress.setText(account.getAddress());
    }

    private void payment() {
        for(OrderFood order: cartList){
            database.QueryData("UPDATE OrderFood SET Status=1 WHERE Id=" + order.getId());
        }
        Toast.makeText(this, "Create Order Successful!!!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
    }
}