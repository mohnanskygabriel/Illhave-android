package sk.upjs.sk.illhave.activity;

import android.Manifest;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
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

public class PotvrdenieObjednavkyActivity extends AppCompatActivity {

    private TextView celkovaCenaTextView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_potvrdenie_objednavky);
        ActivityCompat.requestPermissions(PotvrdenieObjednavkyActivity.this,
                new String[]{Manifest.permission.INTERNET},
                1);
        final Spinner spinner = findViewById(R.id.tablesSpinner);
        initializeRestaurantTables(spinner);

        final TextView passwordView = findViewById(R.id.tablePasswordInput);

        Button button = findViewById(R.id.sendOrder);
        celkovaCenaTextView = findViewById(R.id.celkovaCenaTextView);
        val extras = getIntent().getExtras();
        ArrayList<Integer> productIdIntegerList = null;
        ArrayList<Integer> quantitiesList = null;
        if (extras != null) {
            productIdIntegerList = extras.getIntegerArrayList("productIds");
            quantitiesList = extras.getIntegerArrayList("quantities");
        }
        val productIdIntegerListFinal = (ArrayList<Integer>) productIdIntegerList.clone();
        double celkovaCena = 0.0;
        for (int i = 0; i < productIdIntegerListFinal.size(); i++) {
            celkovaCena += (quantitiesList.get(i) * AllProducts.allProducts.get(productIdIntegerListFinal.get(i) - 1).price());
        }
        DecimalFormat df = new DecimalFormat("#.##");
        val finalCelkovaCena = df.format(celkovaCena);
        celkovaCenaTextView.setText("Cena objednávky: " + finalCelkovaCena + "€");
        val finalQuantitesList = (ArrayList<Integer>) quantitiesList.clone();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Click", "Clicked");
                val restaurantTableId = spinner.getSelectedItemId() + 1;
                val restaurantTable = spinner.getSelectedItem().toString();
                val password = String.valueOf(passwordView.getText());
                Log.e("TableId: ", String.valueOf(restaurantTableId));
                Log.e("Table: ", restaurantTable);
                Log.e("Password: ", password);

                boolean passcheck = checkPassword(restaurantTable, password);
                if (passcheck) {

                    val productIdLongList = new LinkedList<Long>();
                    if (extras != null) {
                        if (productIdIntegerListFinal != null) {
                            for (Integer integer : productIdIntegerListFinal) {
                                productIdLongList.add(Long.valueOf(integer));
                            }
                        }
                    }
                    Log.e("PasswordCheck: ", "PASS_OK");
                    val order = createOrder(productIdLongList, finalQuantitesList, restaurantTableId, restaurantTable, String.valueOf(password));
                    val createdOrder = sendOrder(order);
                    val fullOrder = getOrderById(createdOrder.id());
                    Log.e("Saved order:", fullOrder.toString());
                    saveOrderToLocalDB(fullOrder.id(), fullOrder.createdDate(), Double.parseDouble(finalCelkovaCena));
                } else {
                    Log.e("PasswordCheck: ", "PASS_NOT_OK");
                }
            }
        });
    }

    private void saveOrderToLocalDB(Long idFromExternalDB, Date orderCreatedDate, double celkovaCena) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(IllHaveContract.Order.API_ID, idFromExternalDB);
        contentValues.put(IllHaveContract.Order.CREATED_DATE, orderCreatedDate.toString());
        contentValues.put(IllHaveContract.Order.TOTAL, celkovaCena);
        AsyncQueryHandler queryHandler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onInsertComplete(int token, Object cookie, Uri uri) {
                Toast.makeText(
                        PotvrdenieObjednavkyActivity.this,
                        "Objednávka odoslaná a uložená do lokálného úložiska",
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
        val productIdIntegerListFinal = (ArrayList<Integer>) productIdIntegerList.clone();
        double celkovaCena = 0.0;
        for (int i = 0; i < productIdIntegerListFinal.size(); i++) {
            celkovaCena += (quantitiesList.get(i) * AllProducts.allProducts.get(productIdIntegerListFinal.get(i) - 1).price());
        }
        celkovaCenaTextView.setText("Cena objednávky: " + celkovaCena + "€");
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
