package com.votosapp.Controllers;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.votosapp.Database.SQLiteDBHelper;

public class ZoneController {

    private SQLiteDBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

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



}
