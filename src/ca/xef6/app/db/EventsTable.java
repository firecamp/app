package ca.xef6.app.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

class EventsTable {

    public static final TableColumn   COLUMN_ID          = new TableColumn("_id", "integer primary key autoincrement");
    public static final TableColumn   COLUMN_NAME        = new TableColumn("name", "text not null");
    public static final TableColumn   COLUMN_DESCRIPTION = new TableColumn("description", "text not null");
    public static final TableColumn   COLUMN_AUTHOR      = new TableColumn("author", "text not null");
    public static final TableColumn   COLUMN_DATE        = new TableColumn("date", "text not null");
    public static final TableColumn   COLUMN_TIME        = new TableColumn("date", "text not null");
    public static final TableColumn   COLUMN_IMAGEURL    = new TableColumn("imageurl", "text not null");
    public static final TableColumn   COLUMN_LAT         = new TableColumn("lat", "real");
    public static final TableColumn   COLUMN_LNG         = new TableColumn("lng", "real");

    public static final TableColumn[] COLUMNS            = {
                                                         COLUMN_ID,
                                                         COLUMN_NAME,
                                                         COLUMN_DESCRIPTION,
                                                         COLUMN_AUTHOR,
                                                         COLUMN_DATE,
                                                         COLUMN_TIME,
                                                         COLUMN_IMAGEURL,
                                                         COLUMN_LAT,
                                                         COLUMN_LNG,
                                                         };

    public static final String        NAME               = "events";

    public static void onCreate(SQLiteDatabase db) {
        String createDatabase = "CREATE TABLE " + NAME + "(";
        for (int i = 0; i < COLUMNS.length; ++i) {
            createDatabase += COLUMNS[i].name + " " + COLUMNS[i].type;
            if (i != COLUMNS.length - 1) {
                createDatabase += ", ";
            }
        }
        createDatabase += ")";
        Log.w("EventsTable", "execSQL: \"" + createDatabase + "\"");
        db.execSQL(createDatabase);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NAME);
        onCreate(db);
    }

}
