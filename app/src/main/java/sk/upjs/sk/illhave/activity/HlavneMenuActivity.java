package sk.upjs.sk.illhave.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import sk.upjs.sk.illhave.R;

public class HlavneMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hlavne_menu);

        /****************CLEAR DATABASE SQLITE******************/
        /*SQLiteDatabase db = new DatabaseOpenHelper(this).getWritableDatabase();
        String clearDBQuery = "DELETE FROM " + IllHaveContract.Order.TABLE_NAME;
        db.execSQL(clearDBQuery);*/


        Button jedalnyListokTabbed = findViewById(R.id.jedalnyListokTabbedButton);
        jedalnyListokTabbed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        HlavneMenuActivity.this, JedalnyListokTabbedActivity.class);
                startActivity(intent);
            }
        });

        Button velkyJedalnyListokButton = findViewById(R.id.velkyJedalnyListokButton);
        velkyJedalnyListokButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        HlavneMenuActivity.this, VelkyListokActivity.class);
                startActivity(intent);
            }
        });

        Button mojeObjednavkyButton = (Button) findViewById(R.id.objednavkyButton);
        mojeObjednavkyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        HlavneMenuActivity.this, MojeObjednavkyActivity.class);
                startActivity(intent);
            }
        });
    }
}
