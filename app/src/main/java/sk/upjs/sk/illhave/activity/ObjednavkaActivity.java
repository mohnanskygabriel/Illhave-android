package sk.upjs.sk.illhave.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import lombok.val;
import sk.upjs.sk.illhave.R;
import sk.upjs.sk.illhave.async.GetOrderByIdAsyncTask;
import sk.upjs.sk.illhave.entity.Order;

public class ObjednavkaActivity extends AppCompatActivity {

    Long idObjednavkyApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objednavka);
        ActivityCompat.requestPermissions(ObjednavkaActivity.this,
                new String[]{Manifest.permission.INTERNET},
                1);

        val extras = getIntent().getExtras();
        Order order = null;
        if (extras != null) {
            idObjednavkyApi = extras.getLong("idObjednavkyApi");
            Log.e("objednavka: ", idObjednavkyApi.toString());
            order = getOrderById(idObjednavkyApi);
            Log.e("objednavka: ", order.toString());
        }

        TextView objednavkaTextView = findViewById(R.id.objednavkaDetail_textView);
        objednavkaTextView.setText(order.toString());
    }

    private Order getOrderById(Long idObjednavkyApi) {
        Order order = null;
        try {
            order = new GetOrderByIdAsyncTask().execute(idObjednavkyApi).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return order;
    }
}
