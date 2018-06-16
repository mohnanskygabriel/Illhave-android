package sk.upjs.sk.illhave.localdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    public DatabaseOpenHelper(Context context) {
        super(context, "illhave", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTable());

        insertSampleEntry(db, 1, 2.55, "2017-8-8 12:00:00");
        insertSampleEntry(db, 2, 25.00, "9999-8-8 12:00:00");
    }

    private void insertSampleEntry(SQLiteDatabase db, Integer api_id, double total, String created_date) {
        ContentValues values = new ContentValues();
        values.put(IllHaveContract.Order.API_ID, 1);
        values.put(IllHaveContract.Order.TOTAL, 2.50);
        values.put(IllHaveContract.Order.CREATED_DATE, "2017-8-8 12:00:00");
        db.insert(IllHaveContract.Order.TABLE_NAME, null, values);
    }

    private String createTable() {
        String sqlTemplate = "CREATE TABLE %s ("
                + "%s INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "%s INTEGER,"
                + "%s String,"
                + "%s REAL)";
        return String.format(sqlTemplate,
                IllHaveContract.Order.TABLE_NAME,
                IllHaveContract.Order._ID,
                IllHaveContract.Order.API_ID,
                IllHaveContract.Order.CREATED_DATE,
                IllHaveContract.Order.TOTAL
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}