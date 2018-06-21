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