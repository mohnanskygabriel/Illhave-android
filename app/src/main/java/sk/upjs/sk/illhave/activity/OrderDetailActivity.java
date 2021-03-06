package sk.upjs.sk.illhave.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

import lombok.val;
import sk.upjs.sk.illhave.R;
import sk.upjs.sk.illhave.async.GetOrderByIdAsyncTask;
import sk.upjs.sk.illhave.entity.Order;
import sk.upjs.sk.illhave.entity.Product;

public class OrderDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        val extras = getIntent().getExtras();
        Order order;
        if (extras != null) {
            order = getOrderById(extras.getLong("orderApiId"));
            if (order != null) {
                if (order.restaurantTable() != null) {
                    setRestaurantTableName(order.restaurantTable().name());
                }
                ListView productsListView = findViewById(R.id.order_detail_products_list_view);
                val products = order.product();
                val list = new LinkedList<String>();
                for (Product product : products) {
                    list.add(product.name() + "     " + product.price() + getResources().getString(R.string.currency));
                }
                ListAdapter adapter = new ArrayAdapter<>(
                        this, android.R.layout.simple_list_item_1, list);
                productsListView.setAdapter(adapter);
            } else {
                Toast.makeText(
                        OrderDetailActivity.this,
                        R.string.orderNotFound,
                        Toast.LENGTH_LONG).show();
            }
        }

    }

    private void setRestaurantTableName(String tableName) {
        TextView restaurantTableTextView =
                findViewById(R.id.order_detail_restaurant_table_value_text_view);
        restaurantTableTextView.setText(tableName);
    }

    private Order getOrderById(Long orderApiId) {
        Order order = null;
        try {
            order = new GetOrderByIdAsyncTask().execute(orderApiId).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return order;
    }
}
