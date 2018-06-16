package sk.upjs.sk.illhave.activity;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import sk.upjs.sk.illhave.R;
import sk.upjs.sk.illhave.localdb.IllHaveContract;

public class MojeObjednavkyActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemLongClickListener {

    private static final int LOADER_ID = 3;
    SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moje_objednavky);
        ListView objednavkyView = findViewById(R.id.mojeObjednakyListView);
        String[] from = {IllHaveContract.Order.TOTAL, IllHaveContract.Order.CREATED_DATE};
        int[] to = {R.id.objednavka_lokalna_total_textView, R.id.objednavka_lokalna_cas_textView};
        adapter = new SimpleCursorAdapter(
                this, R.layout.objednavka_lokalna, null, from, to, 0);
        objednavkyView.setAdapter(adapter);
        getLoaderManager().initLoader(LOADER_ID, Bundle.EMPTY, this);
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
}
