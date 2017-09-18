package com.votosapp.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.votosapp.Database.SQLiteDBHelper;
import com.votosapp.Models.Sector;

import java.util.ArrayList;

public class SectorController {

    private SQLiteDBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;
    int Id_Sector;

    public SectorController(Context c) {
        context = c;
    }

    public SectorController abrirBaseDeDatos() throws SQLException {
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void cerrar() {
        dbHelper.close();
    }

    public void InsertSector(int sector_id, int sector_type_id, int zone_id, String name, String description, String latitud, String longitud, int city_id) {
        ContentValues values = new ContentValues();
        values.put(SQLiteDBHelper.COLUMN_SECTOR_ID, sector_id);
        values.put(SQLiteDBHelper.COLUMN_SECTOR_TYPE_ID_ON_SECTOR, sector_type_id);
        values.put(SQLiteDBHelper.COLUMN_ZONE_ID_ON_SECTOR, zone_id);
        values.put(SQLiteDBHelper.COLUMN_SECTOR_NAME, name);
        values.put(SQLiteDBHelper.COLUMN_SECTOR_LATITUDE, latitud);
        values.put(SQLiteDBHelper.COLUMN_SECTOR_LONGITUDE, longitud);
        values.put(SQLiteDBHelper.COLUMN_SECTOR_CITY_ID, city_id);
        database.insert(SQLiteDBHelper.TABLE_NAME_SECTOR, null, values);
    }

    public ArrayList<Sector> GetSectorById(int Id_Sector) {
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        String select = "select * from " + SQLiteDBHelper.TABLE_NAME_SECTOR + " where " + SQLiteDBHelper.COLUMN_SECTOR_ID+ " = '" + Id_Sector + "'"; ;
        Cursor cursor = database.rawQuery(select, null);
        ArrayList<Sector> list = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                Sector sector = cursorToNote(cursor);
                list.add(sector);
            }
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return list;

    }

    public int GetIdSectorByName(String nombre_sector) {
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        String select = "select * from " + SQLiteDBHelper.TABLE_NAME_SECTOR + " where " + SQLiteDBHelper.COLUMN_SECTOR_NAME + " = '" + nombre_sector + "'";
        Cursor cursor = database.rawQuery(select, null);
        try {
            if (cursor.moveToFirst()) {
                Id_Sector = cursor.getInt(cursor.getColumnIndex(SQLiteDBHelper.COLUMN_SECTOR_ID));
            }
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return Id_Sector;
    }

    public ArrayList<Sector> GetSectorByCityId(int Id_City, int sector_type_id) {
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        String select = "select * from " + SQLiteDBHelper.TABLE_NAME_SECTOR + " where " + SQLiteDBHelper.COLUMN_SECTOR_CITY_ID + " = '" + Id_City + "'" + " AND " + SQLiteDBHelper.COLUMN_SECTOR_TYPE_ID_ON_SECTOR + " = '" + sector_type_id + "'";
        Cursor cursor = database.rawQuery(select, null);
        ArrayList<Sector> list = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                Sector sector = cursorToNote(cursor);
                list.add(sector);
            }
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return list;

    }

    public ArrayList<Sector> GetSectorsByCityId(int Id_City) {
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        String select = "select * from " + SQLiteDBHelper.TABLE_NAME_SECTOR + " where " + SQLiteDBHelper.COLUMN_SECTOR_CITY_ID + " = " + Id_City ;
        Cursor cursor = database.rawQuery(select, null);
        ArrayList<Sector> list = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                Sector sector = cursorToNote(cursor);
                list.add(sector);
            }
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return list;

    }


    /////Asignar datos de la base de datos al metodos Set
    private Sector cursorToNote(Cursor cursor) {
        Sector sector = new Sector();
        sector.setSector_Id_Local(cursor.getLong(0));
        sector.setSector_Id(cursor.getLong(1));
        sector.setSector_Type_id(cursor.getLong(2));
        sector.setZone_Id(cursor.getLong(3));
        sector.setName(cursor.getString(4));
        sector.setDescription(cursor.getString(5));
        sector.setLatitude(cursor.getString(6));
        sector.setLongitude(cursor.getString(7));
        return sector;
    }

}
