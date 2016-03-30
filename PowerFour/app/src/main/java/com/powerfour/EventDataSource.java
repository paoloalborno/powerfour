package com.powerfour;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class EventDataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    //Evento per ora formato da ID + COMMENTO
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_COMMENT };

    public EventDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public PW4Event createPW4Event(String event) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_COMMENT, event);

        long insertId = database.insert(MySQLiteHelper.TABLE_COMMENTS, null, values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();

        PW4Event newPW4Event = cursorToEvent(cursor);
        cursor.close();

        return newPW4Event;
    }

    public void deletePW4Event(PW4Event PW4event) {
        long id = PW4event.getId();
        System.out.println("Event deleted ID: " + id);
        database.delete(MySQLiteHelper.TABLE_COMMENTS, MySQLiteHelper.COLUMN_ID + " = " + id, null);
    }

    public List<PW4Event> getAllComments() {
        List<PW4Event> allEvents = new ArrayList<PW4Event>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            PW4Event comment = cursorToEvent(cursor);
            allEvents.add(comment);
            cursor.moveToNext();
        }

        cursor.close();
        return allEvents;
    }

    private PW4Event cursorToEvent(Cursor cursor) {
        PW4Event PW4event = new PW4Event();
        PW4event.setId(cursor.getLong(0));
        PW4event.setComment(cursor.getString(1));
        return PW4event;
    }
}

