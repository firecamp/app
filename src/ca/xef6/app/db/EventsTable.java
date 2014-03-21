package ca.xef6.app.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class EventsTable { // FIXME: Get rid of the duplication.

    static final TableColumn	   COLUMN_ID	  = new TableColumn("_id", "integer primary key autoincrement");
    static final TableColumn	   COLUMN_NAME	= new TableColumn("name", "text not null");
    static final TableColumn	   COLUMN_TYPE	= new TableColumn("type", "integer");
    static final TableColumn	   COLUMN_DESCRIPTION = new TableColumn("description", "text not null");
    static final TableColumn	   COLUMN_AUTHOR      = new TableColumn("author", "text not null");
    static final TableColumn	   COLUMN_DATE	= new TableColumn("date", "text not null");
    static final TableColumn	   COLUMN_TIME	= new TableColumn("time", "text not null");
    static final TableColumn	   COLUMN_IMAGEURL    = new TableColumn("imageurl", "text");
    static final TableColumn	   COLUMN_LAT	 = new TableColumn("lat", "real");
    static final TableColumn	   COLUMN_LNG	 = new TableColumn("lng", "real");
    static final TableColumn	   COLUMN_LOC_ADDRESS = new TableColumn("loc_address", "text");

    public static final String	 COL_ID	     = COLUMN_ID.name;
    public static final String	 COL_NAME	   = COLUMN_NAME.name;
    public static final String	 COL_TYPE	   = COLUMN_TYPE.name;
    public static final String	 COL_DESCRIPTION    = COLUMN_DESCRIPTION.name;
    public static final String	 COL_AUTHOR	 = COLUMN_AUTHOR.name;
    public static final String	 COL_DATE	   = COLUMN_DATE.name;
    public static final String	 COL_TIME	   = COLUMN_TIME.name;
    public static final String	 COL_IMAGEURL       = COLUMN_IMAGEURL.name;
    public static final String	 COL_LAT	    = COLUMN_LAT.name;
    public static final String	 COL_LNG	    = COLUMN_LNG.name;
    public static final String	 COL_LOC_ADDRESS    = COLUMN_LOC_ADDRESS.name;

    private static final TableColumn[] COLUMNS	    = {
							  COLUMN_ID,
							  COLUMN_NAME,
							  COLUMN_TYPE,
							  COLUMN_DESCRIPTION,
							  COLUMN_AUTHOR,
							  COLUMN_DATE,
							  COLUMN_TIME,
							  COLUMN_IMAGEURL,
							  COLUMN_LAT,
							  COLUMN_LNG,
							  COLUMN_LOC_ADDRESS
							  };

    public static final String[]       ALL_COLUMNS	= new String[COLUMNS.length];
    static {
	for (int index = 0; index < ALL_COLUMNS.length; ++index) {
	    ALL_COLUMNS[index] = COLUMNS[index].name;
	}
    }

    public static final String	 NAME	       = "events";

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
