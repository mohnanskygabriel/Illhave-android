package sk.upjs.sk.illhave.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import lombok.val;
import sk.upjs.sk.illhave.R;
import sk.upjs.sk.illhave.async.ClearSQLiteTablesAsyncTask;
import sk.upjs.sk.illhave.localdb.IllHaveContract;

public class MyOrdersActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemLongClickListener {

    private static final int LOADER_ID = 3;
    private SimpleCursorAdapter adapter;
    private ListView ordersView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_orders_activity_toolbar);
        setSupportActionBar(myToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        ordersView = findViewById(R.id.my_orders_list_view);
        String[] from = {IllHaveContract.Order.TOTAL, IllHaveContract.Order.CREATED_DATE, IllHaveContract.Order.API_ID};
        int[] to = {R.id.row_my_order_total_text_view, R.id.row_my_order_date_text_view, R.id.row_my_order_api_id_text_view};
        adapter = new SimpleCursorAdapter(
                this, R.layout.row_my_orders_list_view, null, from, to, 0);
        ordersView.setAdapter(adapter);
        getLoaderManager().initLoader(LOADER_ID, Bundle.EMPTY, this);
        ordersView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                val item = (TextView) view.findViewById(R.id.row_my_order_api_id_text_view);
                Intent intent = new Intent(MyOrdersActivity.this, OrderDetailActivity.class);
                intent.putExtra("orderApiId", Long.valueOf(String.valueOf(item.getText())));
                startActivity(intent);
            }
        });

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id != LOADER_ID) {
            throw new IllegalStateException("Invalid Loader with ID: " + id);
        }
        CursorLoader loader = new CursorLoader(this);
        loader.setUri(IllHaveContract.Order.CONTENT_URI);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_orders_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_all:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.local_storage_clear_message);
                builder.setTitle(R.string.delete_all);
                builder.setCancelable(true);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        Dialog dialog = (Dialog) dialogInterface;
                        AsyncTask asyncTask = new ClearSQLiteTablesAsyncTask();
                        Dialog[] dialogs = new Dialog[1];
                        dialogs[0] = dialog;
                        asyncTask.execute(dialogs);
                        String[] from = {IllHaveContract.Order.TOTAL, IllHaveContract.Order.CREATED_DATE, IllHaveContract.Order.API_ID};
                        int[] to = {R.id.row_my_order_total_text_view, R.id.row_my_order_date_text_view, R.id.row_my_order_api_id_text_view};
                        adapter = new SimpleCursorAdapter(
                                dialog.getContext(), R.layout.row_my_orders_list_view, null, from, to, 0);
                        ordersView.setAdapter(adapter);

                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
