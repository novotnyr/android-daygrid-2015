package sk.upjs.ics.android.daygrid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import sk.upjs.ics.android.daygrid.sk.upjs.ics.android.util.Defaults;

import static sk.upjs.ics.android.daygrid.sk.upjs.ics.android.util.Defaults.DEFAULT_CURSOR_FACTORY;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "daygrid";

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, DEFAULT_CURSOR_FACTORY, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE coloredday ("
                + Database.ColoredDay._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Database.ColoredDay.YEAR + " INTEGER,"
                + Database.ColoredDay.MONTH + " INTEGER,"
                + Database.ColoredDay.DAY + " INTEGER,"
                + Database.ColoredDay.COLOR + " INTEGER"
                + ")";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // do nothing
    }
}
