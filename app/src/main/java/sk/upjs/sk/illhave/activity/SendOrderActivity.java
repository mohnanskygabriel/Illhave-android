package sk.upjs.sk.illhave.activity;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import lombok.val;
import sk.upjs.sk.illhave.AllProducts;
import sk.upjs.sk.illhave.R;
import sk.upjs.sk.illhave.async.CheckRestaurantTablePasswordAsyncTask;
import sk.upjs.sk.illhave.async.GetAllTablesNameAsyncTask;
import sk.upjs.sk.illhave.async.GetOrderByIdAsyncTask;
import sk.upjs.sk.illhave.async.SendOrderAsyncTask;
import sk.upjs.sk.illhave.entity.Order;
import sk.upjs.sk.illhave.entity.Product;
import sk.upjs.sk.illhave.entity.RestaurantTable;
import sk.upjs.sk.illhave.localdb.IllHaveContract;

public class SendOrderActivity extends AppCompatActivity {

    private TextView totalPriceTextView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_order);

        final Spinner spinner = findViewById(R.id.tablesSpinner);
        initializeRestaurantTables(spinner);

        final TextView passwordView = findViewById(R.id.table_password_edit_text);

        Button button = findViewById(R.id.send_order_button);
        totalPriceTextView = findViewById(R.id.total_price_text_view);
        val extras = getIntent().getExtras();
        ArrayList<Integer> productIdIntegerList = null;
        ArrayList<Integer> quantitiesList = null;
        if (extras != null) {
            productIdIntegerList = extras.getIntegerArrayList("productIds");
            quantitiesList = extras.getIntegerArrayList("quantities");
        }

        assert productIdIntegerList != null;
        val productIdIntegerListFinal = new ArrayList<Integer>(productIdIntegerList);
        Double celkovaCena = 0.0;
        assert quantitiesList != null;
        for (int i = 0; i < productIdIntegerListFinal.size(); i++) {
            celkovaCena += (quantitiesList.get(i) * AllProducts.allProducts.get(productIdIntegerListFinal.get(i) - 1).price());
        }

        val finalTotalPrice = String.valueOf(
                new BigDecimal(celkovaCena).setScale(2, RoundingMode.HALF_UP));
        totalPriceTextView.setText(
                getString(R.string.orderPrice, finalTotalPrice, getString(R.string.currency)));
        val finalQuantitesList = new ArrayList<Integer>(quantitiesList);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                val restaurantTableId = spinner.getSelectedItemId() + 1;
                val restaurantTable = spinner.getSelectedItem().toString();
                val password = String.valueOf(passwordView.getText());

                boolean passcheck = checkPassword(restaurantTable, password);
                if (passcheck) {

                    val productIdLongList = new LinkedList<Long>();
                    for (Integer integer : productIdIntegerListFinal) {
                        productIdLongList.add(Long.valueOf(integer));
                    }
                    val order = createOrder(productIdLongList, finalQuantitesList, restaurantTableId, restaurantTable, String.valueOf(password));
                    val createdOrder = sendOrder(order);
                    val fullOrder = getOrderById(createdOrder.id());
                    saveOrderToLocalDB(fullOrder.id(), fullOrder.createdDate(), Double.parseDouble(finalTotalPrice));
                } else {
                    Toast.makeText(
                            SendOrderActivity.this,
                            R.string.restaurantTablePasswordProblemMessage,
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void saveOrderToLocalDB(Long idFromExternalDB, Date orderCreatedDate, double totalPrice) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(IllHaveContract.Order.API_ID, idFromExternalDB);
        contentValues.put(IllHaveContract.Order.CREATED_DATE, orderCreatedDate.toString());
        contentValues.put(IllHaveContract.Order.TOTAL, totalPrice);
        AsyncQueryHandler queryHandler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onInsertComplete(int token, Object cookie, Uri uri) {
                Toast.makeText(
                        SendOrderActivity.this,
                        R.string.orderSaved,
                        Toast.LENGTH_SHORT).show();
            }
        };
        queryHandler.startInsert(
                0,
                idFromExternalDB,
                IllHaveContract.Order.CONTENT_URI,
                contentValues);
    }


    @Override
    protected void onPostResume() {
        val extras = getIntent().getExtras();
        ArrayList<Integer> productIdIntegerList = null;
        ArrayList<Integer> quantitiesList = null;
        if (extras != null) {
            productIdIntegerList = extras.getIntegerArrayList("productIds");
            quantitiesList = extras.getIntegerArrayList("quantities");
        }
        assert productIdIntegerList != null;
        val productIdIntegerListFinal = new ArrayList<Integer>(productIdIntegerList);
        double celkovaCena = 0.0;
        assert quantitiesList != null;
        for (int i = 0; i < productIdIntegerListFinal.size(); i++) {
            celkovaCena += (quantitiesList.get(i) * AllProducts.allProducts.get(productIdIntegerListFinal.get(i) - 1).price());
        }
        totalPriceTextView.setText(getString(
                R.string.orderPrice,
                String.valueOf(
                        new BigDecimal(celkovaCena).setScale(2, RoundingMode.HALF_UP)),
                getString(R.string.currency)));
        super.onPostResume();
    }

    private Order getOrderById(Long id) {
        Order createdOrder = null;
        try {
            createdOrder = new GetOrderByIdAsyncTask().execute(id).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return createdOrder;
    }

    private Order sendOrder(Order order) {
        Order createdOrder = null;
        try {
            createdOrder = new SendOrderAsyncTask().execute(order).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return createdOrder;
    }


    private boolean checkPassword(String restaurantTable, String password) {
        try {
            return new CheckRestaurantTablePasswordAsyncTask().
                    execute(restaurantTable, password).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Order createOrder(List<Long> productIdList, List<Integer> quantitiesList, long restaurantTableId, String restaurantTable, String password) {
        val products = new LinkedList<Product>();
        int i = 0;
        for (Long productId : productIdList) {
            for (int j = 1; j <= quantitiesList.get(i); j++) {
                products.add(Product.builder().
                        id(productId).
                        build());
            }
            i++;
        }
        return Order.
                builder().
                restaurantTable(
                        RestaurantTable.builder().
                                id(restaurantTableId).
                                name(restaurantTable).
                                password(password).build()).
                product(products).
                build();
    }

    private void initializeRestaurantTables(Spinner spinner) {
        try {
            String[] tables = new GetAllTablesNameAsyncTask().execute().get();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, tables);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
