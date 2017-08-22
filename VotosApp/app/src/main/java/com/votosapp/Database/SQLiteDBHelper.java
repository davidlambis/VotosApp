package com.votosapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDBHelper extends SQLiteOpenHelper {

    //Nombre de la base de datos y versión de la misma
    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;

    //region DATOS DE LAS TABLAS
    //Tabla de Sesión
    public static final String TABLE_NAME_SESION = "Sesion";
    public static final String COLUMN_ID_SESION = "Id_Sesion";
    public static final String COLUMN_ESTADO_SESION = "Estado_Sesion";
    public static final String COLUMN_USER_ID_SESION = "User_Id_Sesion";
    public static final String COLUMN_NAME_USER_TYPE_SESION = "Name_User_Type_Sesion";
    public static final String COLUMN_FIRSTNAME_SESION = "Firstname_Sesion";
    public static final String COLUMN_LASTNAME_SESION = "Lastname_Sesion";

    //Datos de la tabla de Departamento
    public static final String TABLE_NAME_DEPARTMENTS = "Departamentos";
    public static final String COLUMN_DEPARTMENT_ID_LOCAL = "Id_Departamento_Local";
    public static final String COLUMN_DEPARTMENT_ID = "Id_Departamento";
    public static final String COLUMN_DEPARTMENT_NAME = "Nombre_Departamento";

    //Datos de la tabla de Municipio
    public static final String TABLE_NAME_CITIES = "Municipios";
    public static final String COLUMN_CITY_ID_LOCAL = "Id_Municipio_Local";
    public static final String COLUMN_CITY_ID = "Id_Municipio";
    public static final String COLUMN_DEPARTMENT_ID_ON_CITY = "Id_Departamento";
    public static final String COLUMN_CITY_NAME = "Nombre_Municipio";

    //Datos de la tabla de Tipo Zona
    public static final String TABLE_NAME_ZONE_TYPE = "Tipo_Zona";
    public static final String COLUMN_TYPE_ZONE_ID_LOCAL = "Id_Tipo_Zona_Local";
    public static final String COLUMN_TYPE_ZONE_ID = "Id_Tipo_Zona";
    public static final String COLUMN_TYPE_ZONE_NAME = "Nombre_Tipo_Zona";

    //Datos de la tabla de Tipo Sector
    public static final String TABLE_NAME_SECTOR_TYPE = "Tipo_Sector";
    public static final String COLUMN_SECTOR_TYPE_ID_LOCAL = "Id_Tipo_Sector_Local";
    public static final String COLUMN_SECTOR_TYPE_ID = "Id_Tipo_Sector";
    public static final String COLUMN_SECTOR_TYPE_NAME = "Nombre_Tipo_Sector";

    //Datos de la tabla de Zona
    public static final String TABLE_NAME_ZONE = "Zona";
    public static final String COLUMN_ZONE_ID_LOCAL = "Id_Zona_Local";
    public static final String COLUMN_ZONE_ID = "Id_Zona";
    public static final String COLUMN_TYPE_ZONE_ID_ON_ZONE = "Id_Tipo_Zona";
    public static final String COLUMN_SECTOR_TYPE_ID_ON_ZONE = "Id_Tipo_Sector";
    public static final String COLUMN_CITY_ID_ON_ZONE = "Id_Municipio";
    public static final String COLUMN_ZONE_NAME = "Nombre_Zona";
    public static final String COLUMN_ZONE_DESCRIPTION = "Descripcion_Zona";
    public static final String COLUMN_ZONE_LATITUDE = "Latitud_Zona";
    public static final String COLUMN_ZONE_LONGITUDE = "Longitud_Zona";

    //Datos de la tabla de Sector
    public static final String TABLE_NAME_SECTOR = "Sector";
    public static final String COLUMN_SECTOR_ID_LOCAL = "Id_Sector_Local";
    public static final String COLUMN_SECTOR_ID = "Id_Sector";
    public static final String COLUMN_SECTOR_TYPE_ID_ON_SECTOR = "Id_Tipo_Sector";
    public static final String COLUMN_ZONE_ID_ON_SECTOR = "Id_Zona";
    public static final String COLUMN_SECTOR_NAME = "Nombre_Sector";
    public static final String COLUMN_SECTOR_DESCRIPTION = "Descripcion_Sector";
    public static final String COLUMN_SECTOR_LATITUDE = "Latitud_Sector";
    public static final String COLUMN_SECTOR_LONGITUDE = "Longitud_Sector";
    public static final String COLUMN_SECTOR_CITY_ID = "Sector_City_Id";

    //Datos de la tabla de Tipo de Usuario
    public static final String TABLE_NAME_USERTYPE = "Tipo_de_Usuario";
    public static final String COLUMN_USER_TYPE_ID_LOCAL = "Id_Tipo_Usuario_Local";
    public static final String COLUMN_USER_TYPE_ID = "Id_Tipo_Usuario";
    public static final String COLUMN_USER_TYPE_NAME = "Nombre_Tipo_Usuario";

    //Datos de la tabla de Usuario
    public static final String TABLE_NAME_USER = "Usuario";
    public static final String COLUMN_USER_ID_LOCAL = "Id_Usuario_Local";
    public static final String COLUMN_USER_ID = "Id_Usuario";
    public static final String COLUMN_USER_TYPE_ID_ON_USER = "Id_Tipo_Usuario";
    public static final String COLUMN_USER_TYPE_ID_REMOTE = "Id_Tipo_Usuario_Remote";
    public static final String COLUMN_REFERENTE_ID = "Id_Referente";
    public static final String COLUMN_REFERENTE_ID_LOCAL = "Id_Referente_Local";
    public static final String COLUMN_REFERENTE_NAME = "Nombre_Referente";
    public static final String COLUMN_SECTOR_ID_ON_USER = "Id_Sector";
    public static final String COLUMN_SECTOR_ID_REMOTE = "Id_Sector_Remote";
    public static final String COLUMN_MUNICIPE_NAME = "Nombre_Municipio";
    public static final String COLUMN_USER_FIRSTNAME = "Nombre_Usuario";
    public static final String COLUMN_USER_LASTNAME = "Apellido_Usuario";
    public static final String COLUMN_USER_IDENTIFICATION_CARD = "Cedula_Usuario";
    public static final String COLUMN_USER_PROFESSION = "Profesion_Usuario";
    public static final String COLUMN_USER_BIRTH_DATE = "Fecha_Nacimiento_Usuario";
    public static final String COLUMN_USER_PHONE1 = "Telefono_1_Usuario";
    public static final String COLUMN_USER_PHONE2 = "Telefono_2_Usuario";
    public static final String COLUMN_USER_EMAIL = "Correo_Usuario";
    public static final String COLUMN_USER_ADDRESS = "Direccion_Usuario";
    public static final String COLUMN_USER_COORDS_LOCATION = "Coordenadas_Ubicacion";
    public static final String COLUMN_USER_HAVE_VEHICLE = "Tiene_Vehiculo";
    public static final String COLUMN_USER_VEHICLE_TYPE = "Tipo_Vehiculo";
    public static final String COLUMN_USER_VEHICLE_PLATE = "Placa_Vehiculo";
    public static final String COLUMN_USER_PASSWORD = "Contraseña";
    public static final String COLUMN_USER_PICTURE = "Foto";
    public static final String COLUMN_USER_IS_LEADER = "Es_Lider";
    public static final String COLUMN_USER_NAME_USER_TYPE = "NombreTipoUsuario_User";
    public static final String COLUMN_USER_DEPARTMENT_ID = "Department_Id_User";
    public static final String COLUMN_USER_ZONE_ID = "Zone_Id_User";


    //endregion

    //region SINTAXIS SQL PARA CREAR TABLAS

    //Sintaxis SQL para crear la tabla de Sesión
    private static final String CREATE_TABLE_QUERY_SESION =
            "CREATE TABLE " + TABLE_NAME_SESION + " (" +
                    COLUMN_ID_SESION + " INTEGER PRIMARY KEY AUTOINCREMENT NULL , " +
                    COLUMN_ESTADO_SESION + " INTEGER NULL , " +
                    COLUMN_USER_ID_SESION + " INTEGER NULL , " +
                    COLUMN_NAME_USER_TYPE_SESION + " TEXT , " +
                    COLUMN_FIRSTNAME_SESION + " TEXT , " +
                    COLUMN_LASTNAME_SESION + " TEXT " + ")";

    //Sintaxix SQL para crear la tabla de Departamento
    private static final String CREATE_TABLE_QUERY_DEPARTAMENTS =
            "CREATE TABLE " + TABLE_NAME_DEPARTMENTS + " (" +
                    COLUMN_DEPARTMENT_ID_LOCAL + " INTEGER PRIMARY KEY AUTOINCREMENT NULL , " +
                    COLUMN_DEPARTMENT_ID + " INTEGER NULL, " +
                    COLUMN_DEPARTMENT_NAME + " TEXT " + ")";

    //Sintaxix SQL para crear la tabla de Departamento
    private static final String CREATE_TABLE_QUERY_CITIES =
            "CREATE TABLE " + TABLE_NAME_CITIES + " (" +
                    COLUMN_CITY_ID_LOCAL + " INTEGER PRIMARY KEY AUTOINCREMENT NULL , " +
                    COLUMN_CITY_ID + " INTEGER NULL , " +
                    COLUMN_DEPARTMENT_ID_ON_CITY + " INTEGER , " +
                    COLUMN_CITY_NAME + " TEXT , " +
                    "FOREIGN KEY(" + COLUMN_DEPARTMENT_ID_ON_CITY + ") REFERENCES " + TABLE_NAME_DEPARTMENTS + "(" + COLUMN_DEPARTMENT_ID_LOCAL + "));";

    //Sintaxis SQL para crear la tabla de Tipo Zona
    private static final String CREATE_TABLE_ZONE_TYPE =
            "CREATE TABLE " + TABLE_NAME_ZONE_TYPE + " (" +
                    COLUMN_TYPE_ZONE_ID_LOCAL + " INTEGER PRIMARY KEY AUTOINCREMENT NULL , " +
                    COLUMN_TYPE_ZONE_ID + " INTEGER NULL , " +
                    COLUMN_TYPE_ZONE_NAME + " TEXT " + ")";


    //Sintaxis SQL para crear la tabla de Tipo Sector
    private static final String CREATE_TABLE_SECTOR_TYPE =
            "CREATE TABLE " + TABLE_NAME_SECTOR_TYPE + " (" +
                    COLUMN_SECTOR_TYPE_ID_LOCAL + " INTEGER PRIMARY KEY AUTOINCREMENT NULL , " +
                    COLUMN_SECTOR_TYPE_ID + " INTEGER NULL , " +
                    COLUMN_SECTOR_TYPE_NAME + " TEXT " + ")";

    //Sintaxis SQL para crear la tabla de Zona
    private static final String CREATE_TABLE_ZONE =
            "CREATE TABLE " + TABLE_NAME_ZONE + " (" +
                    COLUMN_ZONE_ID_LOCAL + " INTEGER PRIMARY KEY AUTOINCREMENT NULL , " +
                    COLUMN_ZONE_ID + " INTEGER NULL , " +
                    COLUMN_TYPE_ZONE_ID_ON_ZONE + " INTEGER NULL , " +
                    COLUMN_CITY_ID_ON_ZONE + " INTEGER NULL , " +
                    COLUMN_SECTOR_TYPE_ID_ON_ZONE + " INTEGER NULL, " +
                    COLUMN_ZONE_NAME + " TEXT , " +
                    COLUMN_ZONE_DESCRIPTION + " TEXT , " +
                    COLUMN_ZONE_LATITUDE + " TEXT , " +
                    COLUMN_ZONE_LONGITUDE + " TEXT , " +
                    "FOREIGN KEY(" + COLUMN_TYPE_ZONE_ID_ON_ZONE + ") REFERENCES " + TABLE_NAME_ZONE_TYPE + "(" + COLUMN_TYPE_ZONE_ID_LOCAL + "));" +
                    "FOREIGN KEY(" + COLUMN_CITY_ID_ON_ZONE + ") REFERENCES " + TABLE_NAME_CITIES + "(" + COLUMN_CITY_ID_LOCAL + "));" +
                    "FOREIGN KEY(" + COLUMN_SECTOR_TYPE_ID_ON_ZONE + ") REFERENCES " + TABLE_NAME_SECTOR_TYPE + "(" + COLUMN_SECTOR_TYPE_ID_LOCAL + "));";


    //Sintaxis SQL para crear la tabla de Sector
    private static final String CREATE_TABLE_SECTOR =
            "CREATE TABLE " + TABLE_NAME_SECTOR + " (" +
                    COLUMN_SECTOR_ID_LOCAL + " INTEGER PRIMARY KEY AUTOINCREMENT NULL , " +
                    COLUMN_SECTOR_ID + " INTEGER NULL , " +
                    COLUMN_SECTOR_TYPE_ID_ON_SECTOR + " INTEGER NULL , " +
                    COLUMN_ZONE_ID_ON_SECTOR + " INTEGER NULL , " +
                    COLUMN_SECTOR_NAME + " TEXT , " +
                    COLUMN_SECTOR_DESCRIPTION + " TEXT , " +
                    COLUMN_SECTOR_LATITUDE + " TEXT , " +
                    COLUMN_SECTOR_LONGITUDE + " TEXT , " +
                    COLUMN_SECTOR_CITY_ID + " TEXT , " +
                    "FOREIGN KEY(" + COLUMN_SECTOR_TYPE_ID_ON_SECTOR + ") REFERENCES " + TABLE_NAME_SECTOR_TYPE + "(" + COLUMN_SECTOR_TYPE_ID_LOCAL + "));" +
                    "FOREIGN KEY(" + COLUMN_ZONE_ID_ON_SECTOR + ") REFERENCES " + TABLE_NAME_ZONE + "(" + COLUMN_ZONE_ID_LOCAL + "));";

    //Sintaxix SQL para crear la tabla de Tipo de Usuario
    private static final String CREATE_TABLE_USERTYPE =
            "CREATE TABLE " + TABLE_NAME_USERTYPE + " (" +
                    COLUMN_USER_TYPE_ID_LOCAL + " INTEGER PRIMARY KEY AUTOINCREMENT NULL , " +
                    COLUMN_USER_TYPE_ID + " INTEGER NULL , " +
                    COLUMN_USER_TYPE_NAME + " TEXT " + ")";

    //Sintaxis SQL para crear la tabla de Usuario
    private static final String CREATE_TABLE_USER =
            "CREATE TABLE " + TABLE_NAME_USER + " (" +
                    COLUMN_USER_ID_LOCAL + " INTEGER PRIMARY KEY AUTOINCREMENT NULL , " +
                    COLUMN_USER_ID + " INTEGER NULL , " +
                    COLUMN_USER_TYPE_ID_ON_USER + " INTEGER NULL, " +
                    COLUMN_USER_TYPE_ID_REMOTE + " INTEGER NULL, " +
                    COLUMN_USER_NAME_USER_TYPE + " TEXT , " +
                    COLUMN_REFERENTE_ID + " INTEGER NULL, " +
                    COLUMN_REFERENTE_ID_LOCAL + " INTEGER NULL, " +
                    COLUMN_REFERENTE_NAME + " TEXT , " +
                    COLUMN_SECTOR_ID_ON_USER + " INTEGER NULL, " +
                    COLUMN_SECTOR_ID_REMOTE + " INTEGER NULL, " +
                    COLUMN_MUNICIPE_NAME + " TEXT NULL, " +
                    COLUMN_USER_FIRSTNAME + " TEXT , " +
                    COLUMN_USER_LASTNAME + " TEXT , " +
                    COLUMN_USER_IDENTIFICATION_CARD + " TEXT , " +
                    COLUMN_USER_PROFESSION + " TEXT , " +
                    COLUMN_USER_BIRTH_DATE + " TEXT , " +
                    COLUMN_USER_PHONE1 + " TEXT , " +
                    COLUMN_USER_PHONE2 + " TEXT , " +
                    COLUMN_USER_EMAIL + " TEXT , " +
                    COLUMN_USER_ADDRESS + " TEXT , " +
                    COLUMN_USER_COORDS_LOCATION + " TEXT , " +
                    COLUMN_USER_HAVE_VEHICLE + " TEXT , " +
                    COLUMN_USER_VEHICLE_TYPE + " TEXT , " +
                    COLUMN_USER_VEHICLE_PLATE + " TEXT , " +
                    COLUMN_USER_PASSWORD + " TEXT , " +
                    COLUMN_USER_PICTURE + " TEXT , " +
                    COLUMN_USER_IS_LEADER + " TEXT , " +
                    COLUMN_USER_DEPARTMENT_ID + " INTEGER , " +
                    COLUMN_USER_ZONE_ID + " INTEGER , " +
                    "FOREIGN KEY(" + COLUMN_USER_TYPE_ID_ON_USER + ") REFERENCES " + TABLE_NAME_USERTYPE + "(" + COLUMN_USER_TYPE_ID_LOCAL + "));" +
                    "FOREIGN KEY(" + COLUMN_REFERENTE_ID_LOCAL + ") REFERENCES " + TABLE_NAME_USER + "(" + COLUMN_USER_ID_LOCAL + "));" +
                    "FOREIGN KEY(" + COLUMN_SECTOR_ID_ON_USER + ") REFERENCES " + TABLE_NAME_SECTOR + "(" + COLUMN_SECTOR_ID_LOCAL + "));";

    //endregion

    //region MÉTODOS BASE DE DATOS

    public SQLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUERY_SESION);
        db.execSQL(CREATE_TABLE_QUERY_DEPARTAMENTS);
        db.execSQL(CREATE_TABLE_QUERY_CITIES);
        db.execSQL(CREATE_TABLE_ZONE_TYPE);
        db.execSQL(CREATE_TABLE_SECTOR_TYPE);
        db.execSQL(CREATE_TABLE_ZONE);
        db.execSQL(CREATE_TABLE_SECTOR);
        db.execSQL(CREATE_TABLE_USERTYPE);
        db.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_USERTYPE);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_SECTOR);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_ZONE);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_SECTOR_TYPE);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_ZONE_TYPE);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_QUERY_CITIES);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_QUERY_DEPARTAMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_QUERY_SESION);
        onCreate(db);
    }
    //endregion
}
