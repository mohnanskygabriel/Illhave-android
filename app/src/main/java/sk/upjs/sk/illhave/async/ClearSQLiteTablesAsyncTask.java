package sk.upjs.sk.illhave.async;

import android.app.Dialog;
import android.database.SQLException;
import android.os.AsyncTask;

import sk.upjs.sk.illhave.localdb.DatabaseOpenHelper;
import sk.upjs.sk.illhave.localdb.IllHaveContract;

public class ClearSQLiteTablesAsyncTask extends AsyncTask<Dialog, Void, String> {
    @Override
    protected String doInBackground(Dialog... dialogs) {
        Dialog dialog = dialogs[0];
        try {
            new DatabaseOpenHelper(dialog.getContext()).getWritableDatabase().execSQL("delete from " + IllHaveContract.Order.TABLE_NAME);
        } catch (SQLException ex) {
            return null;
        }
        return "Completed";
    }
}
