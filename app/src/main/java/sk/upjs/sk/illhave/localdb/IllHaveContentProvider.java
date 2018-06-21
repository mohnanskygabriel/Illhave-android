package sk.upjs.sk.illhave.localdb;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import static sk.upjs.sk.illhave.localdb.IllHaveContract.Order.CONTENT_URI;

public class IllHaveContentProvider extends ContentProvider {

    private DatabaseOpenHelper databaseOpenHelper;

    @Override
    public boolean onCreate() {
        databaseOpenHelper = new DatabaseOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = databaseOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(IllHaveContract.Order.TABLE_NAME, null,
                null, null, null, null, null);
        return cursor;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = databaseOpenHelper.getWritableDatabase();
        String id = uri.getLastPathSegment();
        String[] whereArgs = {id};
        int affectedRows = db.delete(
                IllHaveContract.Order.TABLE_NAME,
                IllHaveContract.Order._ID + "=?",
                whereArgs);
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return affectedRows;
    }


    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        ContentValues cv = new ContentValues();
        cv.put(IllHaveContract.Order.API_ID, values.getAsString(IllHaveContract.Order.API_ID));
        cv.put(IllHaveContract.Order.CREATED_DATE, values.getAsString(IllHaveContract.Order.CREATED_DATE));
        cv.put(IllHaveContract.Order.TOTAL, values.getAsString(IllHaveContract.Order.TOTAL));

        SQLiteDatabase db = databaseOpenHelper.getWritableDatabase();
        final long id = db.insert(IllHaveContract.Order.TABLE_NAME, null, cv);
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return Uri.withAppendedPath(CONTENT_URI, String.valueOf(id));
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}