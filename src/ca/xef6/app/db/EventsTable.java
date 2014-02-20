package ca.xef6.app.db;

import android.database.sqlite.SQLiteDatabase;

class EventsTable {

    private static final TableColumn   COLUMN_ID   = new TableColumn("_id", "integer primary key autoincrement");
    private static final TableColumn   COLUMN_NAME = new TableColumn("name", "text not null");

    private static final TableColumn[] COLUMNS     = {
                                                   COLUMN_ID,
                                                   COLUMN_NAME,
                                                   };

    public static final String         NAME        = "events";

    public static void onCreate(SQLiteDatabase db) {
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
