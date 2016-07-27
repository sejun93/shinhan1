package adapter;

/**
 * Created by user on 2016-07-21.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 8. * Created by yama on 2016-06-27.
 * 9.
 */
public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "contact", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE contacts (";
        sql += "id INTEGER PRIMARY KEY AUTOINCREMENT, ";
        sql += "company TEXT, ";
        sql += "department TEXT, ";
        sql += "service TEXT, ";
        sql += "grade TEXT, ";
        sql += "name TEXT, ";
        sql += "ph1 TEXT, ";
        sql += "ph2 TEXT, ";
        sql += "email TEXT, ";
        sql += "photo TEXT, ";
        sql += "memo TEXT);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
    }
}

