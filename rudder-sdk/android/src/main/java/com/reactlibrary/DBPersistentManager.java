package com.reactlibrary;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/*
 * Helper class for SQLite operations
 * */
class DBPersistentManager extends SQLiteOpenHelper {
    // SQLite database file name
    private static final String DB_NAME = "rl_persistence.db";
    // SQLite database version number
    private static final int DB_VERSION = 1;
    private static final String EVENTS_TABLE_NAME = "events";
    private static final String MESSAGE = "message";
    private static final String MESSAGE_ID = "id";
    private static final String UPDATED = "updated";

    /*
     * create table initially if not exists
     * */
    private void createSchema(SQLiteDatabase db) {
        db.execSQL(String.format(Locale.US, "CREATE TABLE IF NOT EXISTS '%s' ('%s' INTEGER " +
                        "PRIMARY KEY AUTOINCREMENT, '%s' TEXT NOT NULL, '%s' INTEGER NOT NULL)",
                EVENTS_TABLE_NAME, MESSAGE_ID, MESSAGE, UPDATED));
    }

    /*
     * save individual messages to DB
     * */
    void saveEvent(String messageJson) {
        ContentValues values = new ContentValues();
        values.put(MESSAGE, messageJson);
        values.put(UPDATED, System.currentTimeMillis());
        SQLiteDatabase database = getWritableDatabase();
        if (database.isOpen()) {
            database.execSQL(String.format(Locale.US, "INSERT INTO %s (%s, %s) VALUES ('%s', %d)",
                    EVENTS_TABLE_NAME, MESSAGE, UPDATED, messageJson.replaceAll("'", "\\\\\'"), System.currentTimeMillis()));
        } else {
            RudderLogger.logError("DBPersistentManager: saveEvent: database is not writable");
        }
    }

    /*
     * delete event with single messageId
     * */
    void clearEventFromDB(int messageId) {
        List<Integer> messageIds = new ArrayList<>();
        messageIds.add(messageId);
        clearEventsFromDB(messageIds);
    }

    /*
     * remove selected events from persistence database storage
     * */
    void clearEventsFromDB(List<Integer> messageIds) {
        // get writable database
        SQLiteDatabase database = getWritableDatabase();
        if (database.isOpen()) {
            // format CSV string from messageIds list
            StringBuilder builder = new StringBuilder();
            for (int index = 0; index < messageIds.size(); index++) {
                builder.append(messageIds.get(index));
                builder.append(",");
            }
            // remove last "," character
            builder.deleteCharAt(builder.length() - 1);
            // remove events
            database.execSQL(String.format(Locale.US, "DELETE FROM %s WHERE %s IN (%s)",
                    EVENTS_TABLE_NAME, MESSAGE_ID, builder.toString()));
        } else {
            RudderLogger.logError("DBPersistentManager: clearEventsFromDB: database is not " +
                    "writable");
        }
    }

    /*
     * retrieve `count` number of messages from DB and store messageIds and messages separately
     * */
    void fetchEventsFromDB(ArrayList<Integer> messageIds, ArrayList<String> messages, int count) {
        // clear lists if not empty
        if (!messageIds.isEmpty()) messageIds.clear();
        if (!messages.isEmpty()) messages.clear();

        // get readable database instance
        SQLiteDatabase database = getReadableDatabase();
        if (database.isOpen()) {
            Cursor cursor = database.rawQuery(String.format(Locale.US, "SELECT * FROM %s ORDER BY" +
                    " %s ASC LIMIT %d", EVENTS_TABLE_NAME, UPDATED, count), null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    messageIds.add(cursor.getInt(cursor.getColumnIndex(MESSAGE_ID)));
                    messages.add(cursor.getString(cursor.getColumnIndex(MESSAGE)));
                    cursor.moveToNext();
                }
            }
            cursor.close();
        } else {
            RudderLogger.logError("DBPersistentManager: fetchEventsFromDB: database is not " +
                    "readable");
        }
    }

    int getDBRecordCount() {
        // initiate count
        int count = -1;

        // get readable database instance
        SQLiteDatabase database = getReadableDatabase();
        if (database.isOpen()) {
            Cursor cursor = database.rawQuery(String.format(Locale.US, "SELECT count(*) FROM %s;"
                    , EVENTS_TABLE_NAME), null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    // result will be in the first position
                    count = cursor.getInt(0);
                    cursor.moveToNext();
                }
            }
            // release cursor
            cursor.close();
        } else {
            RudderLogger.logError("DBPersistentManager: fetchEventsFromDB: database is not " +
                    "readable");
        }

        return count;
    }

    private static DBPersistentManager instance;

    static DBPersistentManager getInstance(Application application) {
        if (instance == null) instance = new DBPersistentManager(application);
        return instance;
    }

    private DBPersistentManager(Application application) {
        super(application, DB_NAME, null, DB_VERSION);
        getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createSchema(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // basically do nothing
    }

    public void deleteAllEvents() {
        SQLiteDatabase database = getWritableDatabase();
        if (database.isOpen()) {
            // remove events
            database.execSQL(String.format(Locale.US, "DELETE FROM %s", EVENTS_TABLE_NAME));
        } else {
            RudderLogger.logError("DBPersistentManager: clearEventsFromDB: database is not " +
                    "writable");
        }
    }
}
