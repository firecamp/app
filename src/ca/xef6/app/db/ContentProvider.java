package ca.xef6.app.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

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

    private void checkColumns(String[] projection) {
        /*String[] available = EventsTable.COLUMNS;
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(
                    Arrays.asList(available));
            // Check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException(
                        "Unknown columns in projection");
            }
        }*/
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = URI_MATCHER.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
        case EVENTS:
            rowsDeleted = sqlDB.delete(EventsTable.NAME, selection,
                                       selectionArgs);
            break;
        case EVENT_ID:
            String id = uri.getLastPathSegment();
            if (TextUtils.isEmpty(selection)) {
                rowsDeleted = sqlDB.delete(EventsTable.NAME, EventsTable.COLUMN_ID.name + "=" + id, null);
            } else {
                rowsDeleted = sqlDB.delete(EventsTable.NAME, EventsTable.COLUMN_ID.name + "=" + id + " and " + selection, selectionArgs);
            }
            break;
        default:
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = URI_MATCHER.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id = 0;
        switch (uriType) {
        case EVENTS:
            id = sqlDB.insert(EventsTable.NAME, null, values);
            break;
        default:
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public boolean onCreate() {
        database = new DatabaseHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Using SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        // Check if the caller has requested a column which does not exists
        checkColumns(projection);
        // Set the table
        queryBuilder.setTables(EventsTable.NAME);
        int uriType = URI_MATCHER.match(uri);
        switch (uriType) {
        case EVENTS:
            break;
        case EVENT_ID:
            queryBuilder.appendWhere(EventsTable.COLUMN_ID.name + "=" + uri.getLastPathSegment());
            break;
        default:
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        // Make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int uriType = URI_MATCHER.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
        case EVENTS:
            rowsUpdated = sqlDB.update(EventsTable.NAME, values, selection, selectionArgs);
            break;
        case EVENT_ID:
            String id = uri.getLastPathSegment();
            if (TextUtils.isEmpty(selection)) {
                rowsUpdated = sqlDB.update(EventsTable.NAME, values, EventsTable.COLUMN_ID.name + "=" + id, null);
            } else {
                rowsUpdated = sqlDB.update(EventsTable.NAME, values, EventsTable.COLUMN_ID.name + "=" + id + " and " + selection, selectionArgs);
            }
            break;
        default:
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;

    }

}
