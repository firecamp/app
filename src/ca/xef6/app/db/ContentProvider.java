package ca.xef6.app.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class ContentProvider extends android.content.ContentProvider {

    private static final String     AUTHORITY         = "ca.xef6.app.db";
    private static final String     BASE_PATH         = "events";
    public static final String      CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/event";
    public static final String      CONTENT_TYPE      = ContentResolver.CURSOR_DIR_BASE_TYPE + "/events";
    public static final Uri         CONTENT_URI       = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    private static final int        EVENT_ID          = 20;
    private static final int        EVENTS            = 10;

    private static final UriMatcher URI_MATCHER       = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH, EVENTS);
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH + "/#", EVENT_ID);
    }

    private DatabaseHelper          database;

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public boolean onCreate() {
        database = new DatabaseHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

}
