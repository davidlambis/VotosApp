package com.votosapp.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.votosapp.Database.SQLiteDBHelper;
import com.votosapp.Models.Department;

import java.util.ArrayList;

public class DepartamentoController {
    private SQLiteDBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;
    int Id_Departamento;
    long Id_Departamento_Local;

    public DepartamentoController(Context c) {
        context = c;
    }

    public DepartamentoController abrirBaseDeDatos() throws SQLException {
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void cerrar() {
        dbHelper.close();
    }

    public void InsertDepartamento(String nombreDepartamento , int id_Departamento) {
        ContentValues values = new ContentValues();
        values.put(SQLiteDBHelper.COLUMN_DEPARTMENT_ID, id_Departamento);
        values.put(SQLiteDBHelper.COLUMN_DEPARTMENT_NAME, nombreDepartamento);
        database.insert(SQLiteDBHelper.TABLE_NAME_DEPARTMENTS, null, values);
    }

    public ArrayList<Department> GetDepartamentos() {
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        String select = "select * from " + SQLiteDBHelper.TABLE_NAME_DEPARTMENTS;
        Cursor cursor = database.rawQuery(select, null);
        ArrayList<Department> list = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                Department departamento = cursorToNote(cursor);
                list.add(departamento);
            }
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return list;

    }

    public int GetIdDepartamentoByName(String NombreDepartamento) {
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        String select = "select * from " + SQLiteDBHelper.TABLE_NAME_DEPARTMENTS + " where " + SQLiteDBHelper.COLUMN_DEPARTMENT_NAME + " = '" + NombreDepartamento + "'";
        Cursor cursor = database.rawQuery(select, null);
        try {
            if (cursor.moveToFirst()) {
                Id_Departamento = cursor.getInt(cursor.getColumnIndex(SQLiteDBHelper.COLUMN_DEPARTMENT_ID));
            }
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return Id_Departamento;
    }

    public long GetIdDepartamentoLocalByName(String NombreDepartamento) {
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        String select = "select * from " + SQLiteDBHelper.TABLE_NAME_DEPARTMENTS + " where " + SQLiteDBHelper.COLUMN_DEPARTMENT_NAME + " = '" + NombreDepartamento + "'";
        Cursor cursor = database.rawQuery(select, null);
        try {
            if (cursor.moveToFirst()) {
                Id_Departamento_Local = cursor.getLong(cursor.getColumnIndex(SQLiteDBHelper.COLUMN_DEPARTMENT_ID_LOCAL));
            }
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return Id_Departamento_Local;
    }

    /////Asignar datos de la base de datos al metodos Set
    private Department cursorToNote(Cursor cursor) {
        Department departamento = new Department();
        departamento.setDepartment_Id_Local(cursor.getLong(0));
        departamento.setDepartment_Id(cursor.getInt(1));
        departamento.setName(cursor.getString(2));
        return departamento;
    }

}
