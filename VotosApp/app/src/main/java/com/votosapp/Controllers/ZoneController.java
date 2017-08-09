package com.votosapp.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.votosapp.Database.SQLiteDBHelper;
import com.votosapp.Models.Zone;

import java.util.ArrayList;

public class ZoneController {

    private SQLiteDBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;
    int Id_Zone;

    public ZoneController(Context c) {
        context = c;
    }

    public ZoneController abrirBaseDeDatos() throws SQLException {
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void cerrar() {
        dbHelper.close();
    }

    public void InsertZone(int zone_id, int type_zone_id, int city_id, int sector_type_id, String name, String description,
                           String latitude, String longitude) {
        ContentValues values = new ContentValues();
        values.put(SQLiteDBHelper.COLUMN_ZONE_ID, zone_id);
        values.put(SQLiteDBHelper.COLUMN_TYPE_ZONE_ID_ON_ZONE, type_zone_id);
        values.put(SQLiteDBHelper.COLUMN_CITY_ID_ON_ZONE, city_id);
        values.put(SQLiteDBHelper.COLUMN_SECTOR_TYPE_ID_ON_ZONE, sector_type_id);
        values.put(SQLiteDBHelper.COLUMN_ZONE_NAME, name);
        values.put(SQLiteDBHelper.COLUMN_ZONE_DESCRIPTION, description);
        values.put(SQLiteDBHelper.COLUMN_ZONE_LATITUDE, latitude);
        values.put(SQLiteDBHelper.COLUMN_ZONE_LONGITUDE, longitude);
        database.insert(SQLiteDBHelper.TABLE_NAME_ZONE, null, values);
    }


    public ArrayList<Zone> GetZoneByCityId(int Id_City, int sector_type_id) {
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        String select = "select * from " + SQLiteDBHelper.TABLE_NAME_ZONE + " where " + SQLiteDBHelper.COLUMN_CITY_ID_ON_ZONE + " = '" + Id_City + "'" + " AND " + SQLiteDBHelper.COLUMN_SECTOR_TYPE_ID_ON_ZONE + " = '" + sector_type_id + "'";
        Cursor cursor = database.rawQuery(select, null);
        ArrayList<Zone> list = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                Zone zone = cursorToNote(cursor);
                list.add(zone);
            }
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return list;

    }

    public int GetIdZoneByName(String nombre_zone) {
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        String select = "select * from " + SQLiteDBHelper.TABLE_NAME_ZONE + " where " + SQLiteDBHelper.COLUMN_ZONE_NAME + " = '" + nombre_zone + "'";
        Cursor cursor = database.rawQuery(select, null);
        try {
            if (cursor.moveToFirst()) {
                Id_Zone = cursor.getInt(cursor.getColumnIndex(SQLiteDBHelper.COLUMN_ZONE_ID));
            }
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return Id_Zone;
    }


    public ArrayList<Zone> GetZoneById(int Id_Zone) {
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        String select = "select * from " + SQLiteDBHelper.TABLE_NAME_ZONE + " where " + SQLiteDBHelper.COLUMN_ZONE_ID + " = '" + Id_Zone + "'"; ;
        Cursor cursor = database.rawQuery(select, null);
        ArrayList<Zone> list = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                Zone zone = cursorToNote(cursor);
                list.add(zone);
            }
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return list;

    }

    /////Asignar datos de la base de datos al metodos Set
    private Zone cursorToNote(Cursor cursor) {
        Zone zone = new Zone();
        zone.setZone_Id_Local(cursor.getLong(0));
        zone.setZone_Id(cursor.getLong(1));
        zone.setType_Zone_id(cursor.getLong(2));
        zone.setCity_Id(cursor.getInt(3));
        zone.setSector_Type_Id(cursor.getInt(4));
        zone.setName(cursor.getString(5));
        zone.setDescription(cursor.getString(6));
        zone.setLatitude(cursor.getString(7));
        zone.setLongitude(cursor.getString(8));
        return zone;
    }

}
