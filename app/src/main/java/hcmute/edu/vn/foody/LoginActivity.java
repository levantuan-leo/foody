package hcmute.edu.vn.foody;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import hcmute.edu.vn.foody.db.Database;
import hcmute.edu.vn.foody.entity.Account;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;

    private Button buttonLogin;
    private Button buttonRegister;
    private TextView buttonForgotPassword;

    Database database;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        database = new Database(this, "foody.sqlite", null, 1);

        sharedPreferences = getSharedPreferences("Account", MODE_PRIVATE);

        // just check existing "email" or "password"
        if (sharedPreferences.contains("myAccount")) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        editTextEmail = findViewById(R.id.editText_email);
        editTextPassword = findViewById(R.id.editText_password);
        buttonLogin = findViewById(R.id.button_login);
        buttonRegister = findViewById(R.id.button_signUp);
        buttonForgotPassword = findViewById(R.id.button_forgotPassword);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        // Hide keyboard when not being focus on editText
        editTextEmail.setOnFocusChangeListener((view, b) -> hideKeyboard(view));
        editTextPassword.setOnFocusChangeListener((view, b) -> hideKeyboard(view));

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get Account
                String email = editTextEmail.getText().toString().trim();
                String pass = editTextPassword.getText().toString().trim();
                Cursor dataRestaurants = database.GetData("SELECT * FROM Account WHERE Email='" + email + "' AND Pass='" + pass + "'");

                if (dataRestaurants.getCount() > 0) {
                    while (dataRestaurants.moveToNext()) {
                        Account account = new Account(
                                dataRestaurants.getInt(0),    // Id
                                dataRestaurants.getString(1), // Email
                                dataRestaurants.getString(2), // Password
                                dataRestaurants.getString(3), // Address
                                dataRestaurants.getString(4), // Name
                                dataRestaurants.getString(5), // Avatar
                                dataRestaurants.getString(6), // Phone
                                dataRestaurants.getInt(7)     // Role
                        );

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(account);
                        editor.putString("myAccount", json);
                        editor.apply();
                    }
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Incorrect Email or Password!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
        buttonForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(i);
            }
        });
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}