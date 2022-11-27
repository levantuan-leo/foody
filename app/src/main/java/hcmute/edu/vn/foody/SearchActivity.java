package hcmute.edu.vn.foody;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import hcmute.edu.vn.foody.adapter.SearchAdapter;
import hcmute.edu.vn.foody.db.Database;
import hcmute.edu.vn.foody.entity.Food;
import hcmute.edu.vn.foody.entity.Restaurant;

public class SearchActivity extends AppCompatActivity {

    private ImageView imageViewBack;
    private EditText editTextSearch;
    private ListView listViewSearch;

    private ArrayList<Food> foods;

    SearchAdapter adapter;
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        database = new Database(this, "foody.sqlite", null, 1);

        foods = new ArrayList<>();

        imageViewBack = findViewById(R.id.imageView_back);
        editTextSearch = findViewById(R.id.editText_search);
        listViewSearch = findViewById(R.id.listView_search);

        editTextSearch.requestFocus();

        adapter = new SearchAdapter(this, R.layout.items_search, foods);
        listViewSearch.setAdapter(adapter);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                foods.clear();
                if (!editTextSearch.getText().toString().equals("")) {
                    Cursor dataFoods = database.GetData("SELECT * FROM Food WHERE Name LIKE '%" + editTextSearch.getText().toString() + "%'");
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

                        foods.add(new Food(id, restaurant, name, price, image));
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // get previous page
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}