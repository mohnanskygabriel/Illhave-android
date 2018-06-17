package sk.upjs.sk.illhave.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import sk.upjs.sk.illhave.R;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

       /* SQLiteDatabase db = new DatabaseOpenHelper(this).getWritableDatabase();
        String clearDBQuery = "DELETE FROM " + IllHaveContract.Order.TABLE_NAME;
        db.execSQL(clearDBQuery);*/

        Button foodMenuButton = findViewById(R.id.food_menu_button);
        foodMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        MainMenuActivity.this, FoodMenuActivity.class);
                startActivity(intent);
            }
        });

        Button myOrdersButton = findViewById(R.id.my_orders_button);
        myOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        MainMenuActivity.this, MyOrdersActivity.class);
                startActivity(intent);
            }
        });
    }
}
