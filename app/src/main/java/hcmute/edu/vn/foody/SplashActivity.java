package hcmute.edu.vn.foody;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import hcmute.edu.vn.foody.db.Database;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends Activity {

    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        transparentStatusAndNavigation(this);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);

        dataInit();
    }

    private void dataInit() {
        databaseInit();

        // run once
        dataDelete();
        dataInsert();
    }

    public static void transparentStatusAndNavigation(Activity activity) {

        Window window = activity.getWindow();

        // make full transparent statusBar
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(window, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            int visibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            visibility = visibility | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            window.getDecorView().setSystemUiVisibility(visibility);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            int windowManager = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            windowManager = windowManager | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            setWindowFlag(window, windowManager, false);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }

    }

    private static void setWindowFlag(Window window, final int bits, boolean on) {
        WindowManager.LayoutParams winParams = window.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        window.setAttributes(winParams);
    }

    private void databaseInit() {
        //init database
        database = new Database(this, "foody.sqlite", null, 1);
        //init Users table
        database.QueryData("CREATE TABLE IF NOT EXISTS Account(" +
                "Id INTEGER PRIMARY KEY," +
                "Email VARCHAR(200)," +
                "Pass VARCHAR(30)," +
                "Address VARCHAR(300)," +
                "Name VARCHAR(200)," +
                "Avatar VARCHAR(500)," +
                "Phone VARCHAR(20)," +
                "Role INTEGER)");
        //init Restaurant table
        database.QueryData("CREATE TABLE IF NOT EXISTS Restaurant(" +
                "Id INTEGER PRIMARY KEY," +
                "Name VARCHAR(200)," +
                "Address VARCHAR(300)," +
                "Image INTEGER)");
        //init Food table
        database.QueryData("CREATE TABLE IF NOT EXISTS Food(" +
                "Id INTEGER PRIMARY KEY," +
                "Id_res INTEGER," +
                "Name VARCHAR(200)," +
                "Price INTEGER," +
                "Image INTEGER)");
        //init OrderFood table
        database.QueryData("CREATE TABLE IF NOT EXISTS OrderFood(" +
                "Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Id_food INTEGER," +
                "Quantity INTEGER," +
                "Price_total INTEGER," +
                "Status INTEGER," +
                "Id_account INTEGER," +
                "Time VARCHAR(50))");
    }

    private void dataInsert() {
        //Restaurant
        database.QueryData("INSERT INTO Restaurant VALUES(1, 'BBQ Best Restaurant', '123 Dong Da, Ha Noi', " + R.drawable.bbqres + " )");
        database.QueryData("INSERT INTO Restaurant VALUES(2, 'TeaMilk Best Restaurant', '1 Le Van Viet, Q9, TP. HCM', " + R.drawable.restaurant6 + " )");
        database.QueryData("INSERT INTO Restaurant VALUES(3, 'Bún Chị Bảy', '89 Phạm Văn Đồng, Q9, TP. HCM', " + R.drawable.restaurant10 + " )");
        database.QueryData("INSERT INTO Restaurant VALUES(4, 'Cơm sườn bì chả', '456 Võ Văn Kiệt, Q10, TP. HCM', " + R.drawable.restaurant7 + " )");
        database.QueryData("INSERT INTO Restaurant VALUES(5, 'Ăn vặt cô 3', '256 Võ Văn Ngân, Thu Duc, TP. HCM', " + R.drawable.restaurant9 + " )");
        database.QueryData("INSERT INTO Restaurant VALUES(6, 'Cơm tấm', '341A QL 1A, Thu Duc, TP. HCM', " + R.drawable.restaurant8 + " )");
        database.QueryData("INSERT INTO Restaurant VALUES(7, 'Phở Ngon', '25 Võ Văn Ngân, Thu Duc, TP. HCM', " + R.drawable.restaurant + " )");
        database.QueryData("INSERT INTO Restaurant VALUES(8, 'Gà rán PopEye', '12 Võ Văn Ngân, Thu Duc, TP. HCM', " + R.drawable.restaurant2 + " )");
        database.QueryData("INSERT INTO Restaurant VALUES(9, 'Chè 4 mùa', '10 Trường Chinh, Q12, TP. HCM', " + R.drawable.restaurant3 + " )");
        database.QueryData("INSERT INTO Restaurant VALUES(10, 'Bánh mỳ', '14 Phạm Văn Đồng TP. HCM', " + R.drawable.restaurant4 + " )");

        database.QueryData("INSERT INTO Food VALUES(1, 1, 'Cơm gà', 10, " + R.drawable.asiafood2 + ")");
        database.QueryData("INSERT INTO Food VALUES(2, 1, 'Beef Steak', 20, " + R.drawable.beefsteak + ")");
        database.QueryData("INSERT INTO Food VALUES(3, 1, 'Cơm sườn', 30, " + R.drawable.food_com_suon + ")");

        database.QueryData("INSERT INTO Food VALUES(4, 2, 'Pizza', 40, " + R.drawable.asiafood1 + ")");
        database.QueryData("INSERT INTO Food VALUES(5, 2, 'Bánh mỳ kẹp', 50, " + R.drawable.bread + ")");
        database.QueryData("INSERT INTO Food VALUES(6, 2, 'Pizza việt quất', 60, " + R.drawable.food1 + ")");

        database.QueryData("INSERT INTO Food VALUES(7, 3, 'Phở bò', 70, " + R.drawable.food_noodle + ")");
        database.QueryData("INSERT INTO Food VALUES(8, 3, 'Sallad', 80, " + R.drawable.food_salad + ")");
        database.QueryData("INSERT INTO Food VALUES(9, 3, 'Cá hồi chiên', 90, " + R.drawable.food_salmon + ")");

        //Account
        database.QueryData("INSERT INTO Account VALUES(1, 'tuan@gmail.com', '123', 'Q9, Ho Chi Minh', 'Le Van Tuan', 'https://res.cloudinary.com/dtsahwrtk/image/upload/v1635424277/samples/people/boy-snow-hoodie.jpg', '0357499653', 0)");

        //OrderFood
        database.QueryData("INSERT INTO OrderFood VALUES(null, 2, 2, 80, 2, 1, '10:00 AM 5/3/2022')");
        database.QueryData("INSERT INTO OrderFood VALUES(null, 2, 3, 150, 2, 2, '10:00 AM 5/3/2022')");
    }

    private void dataDelete() {
        //Restaurant
        database.QueryData("DELETE FROM Restaurant");
        //Food
        database.QueryData("DELETE FROM Food");
        //Users
        database.QueryData("DELETE FROM Account");
        //Comments
        database.QueryData("DELETE FROM OrderFood");
    }
}