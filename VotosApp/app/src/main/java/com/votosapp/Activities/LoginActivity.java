package com.votosapp.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.votosapp.Controllers.UserController;
import com.votosapp.Handler.HttpHandler;
import com.votosapp.Models.User;
import com.votosapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements OnClickListener {

    //region UI Elements

    private Button btnIniciarSesion;
    private EditText edtCorreo;
    private EditText edtContraseña;
    private String strCorreo;
    private String strContraseña;
    private ProgressDialog pDialog;

    //endregion

    //region Variables

    private String url = "http://gestionusuariospolit.azurewebsites.net/api/users/LoginUser/";
    int length;

    //Tabla de UserType
    private int User_Type_Id;

    //Datos del Json de Usuario
    private int User_Id;
    private String Referente_Id;
    private String Name_Referente;
    private int Sector_Id;
    private String Name_Municipe;
    private String FirstName;
    private String LastName;
    private String Identification_Card;
    private String Profession;
    private String Birth_Date;
    private String Phone1;
    private String Phone2;
    private String Email;
    private String Address;
    private String Coords_Location;
    private String Have_Vehicle;
    private String Vehicle_Type;
    private String Vehicle_Plate;
    private String Password;
    private String Is_Leader;
    private String Is_False;
    private String Is_Available;
    private String Picture;
    private String Department_Id;
    private String City_Id;
    private String Zone_Id;
    private String Nombre_Tipo_Usuario;
    UserController db_Usuarios;

    //endregion

    //region OnCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db_Usuarios = new UserController(this);

        edtCorreo = (EditText) findViewById(R.id.edtCorreo);
        edtContraseña = (EditText) findViewById(R.id.edtContraseña);
        btnIniciarSesion = (Button) findViewById(R.id.btnIniciarSesion);
        btnIniciarSesion.setOnClickListener(this);
    }
    //endregion

    //region onClick Iniciar Sesión

    @Override
    public void onClick(View v) {
        strCorreo = edtCorreo.getText().toString();
        if (TextUtils.isEmpty(strCorreo)) {
            edtCorreo.setError("Llena este campo");
            return;
        }
        strContraseña = edtContraseña.getText().toString();
        if (TextUtils.isEmpty(strContraseña)) {
            edtContraseña.setError("Llena este campo");
            return;
        }

        if (isOnlineNet()) {
            new getUserLogin().execute();
        } else {
            db_Usuarios.abrirBaseDeDatos();
            ArrayList<User> list_u = new ArrayList<>();
            list_u = db_Usuarios.GetUserByEmailAndPassword(strCorreo, strContraseña);
            db_Usuarios.cerrar();
            ArrayList<User> lista_users = new ArrayList<>();
            db_Usuarios.abrirBaseDeDatos();
            lista_users = db_Usuarios.GetUsers(strCorreo, strContraseña);
            db_Usuarios.cerrar();


            if (lista_users.size() == 0) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Alerta");
                builder.setMessage("No se han registrado usuarios en el dispositivo, por favor accede a internet");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            } else if (list_u.size() == 0) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Alerta");
                builder.setMessage("Usuario o Contraseña incorrectos");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                for (User u : list_u) {
                    User_Id = u.getUser_Id();
                    Nombre_Tipo_Usuario = u.getName_User_Type();
                    FirstName = u.getFirstName();
                    LastName = u.getLastName();
                }
                Intent i = new Intent(LoginActivity.this, MapsActivity.class);
                Toast.makeText(getApplicationContext(), "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                i.putExtra("user_id", User_Id);
                i.putExtra("name_user_type", Nombre_Tipo_Usuario);
                i.putExtra("firstname", FirstName);
                i.putExtra("lastname", LastName);
                startActivity(i);
                finish();
            }

        }
    }

    //endregion


    //region getUserLogin -- Asynctask del login de Usuario
    private class getUserLogin extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Cargando...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String URL = url + strCorreo + "/" + strContraseña;
            String jsonStr = sh.makeServiceCall(URL);

            if (jsonStr != null) {
                try {
                    ArrayList<User> list_usuario = new ArrayList<>();
                    db_Usuarios.abrirBaseDeDatos();
                    list_usuario = db_Usuarios.GetUserByEmailAndPassword(strCorreo, strContraseña);
                    db_Usuarios.cerrar();

                    if (list_usuario.size() == 0) {
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        length = jsonObject.length();

                        User_Id = jsonObject.getInt("User_Id");
                        Referente_Id = jsonObject.getString("Referente_Id");
                        Name_Referente = jsonObject.getString("Name_Referente");
                        Sector_Id = jsonObject.getInt("Sector_Id");
                        Name_Municipe = jsonObject.getString("Name_Municipe");
                        FirstName = jsonObject.getString("FirstName");
                        LastName = jsonObject.getString("LastName");
                        Identification_Card = jsonObject.getString("Identification_Card");
                        Profession = jsonObject.getString("Profession");
                        Birth_Date = jsonObject.getString("Birth_Date");
                        Phone1 = jsonObject.getString("Phone1");
                        Phone2 = jsonObject.getString("Phone2");
                        Email = jsonObject.getString("Email");
                        Address = jsonObject.getString("Address");
                        Coords_Location = jsonObject.getString("Coords_Location");
                        Have_Vehicle = jsonObject.getString("Have_Vehicle");
                        Vehicle_Type = jsonObject.getString("Vehicle_Type");
                        Vehicle_Plate = jsonObject.getString("Vehicle_Plate");
                        Password = jsonObject.getString("Password");
                        Is_Leader = jsonObject.getString("Is_Leader");
                        Is_Available = jsonObject.getString("Is_Available");
                        Picture = jsonObject.getString("Picture");
                        int department_id = jsonObject.getInt("Department_Id");
                        City_Id = jsonObject.getString("City_Id");
                        Zone_Id = jsonObject.getString("Zone_Id");

                        User_Type_Id = jsonObject.getInt("User_Type_Id");
                        Nombre_Tipo_Usuario = jsonObject.getString("Name_User_Type");
                        ;

                        /*db_UserType.abrirBaseDeDatos();
                        ArrayList<UserType> list_userTypes = db_UserType.GetUserTypesByName(Name_User_Type);
                        db_UserType.cerrar();
                        if (list_userTypes.size() == 0) {
                            db_UserType.abrirBaseDeDatos();
                            db_UserType.InsertUserType(User_Type_Id, Name_User_Type);
                            db_UserType.cerrar();
                        }

                        db_UserType.abrirBaseDeDatos();
                        long User_Type_Id_Local = db_UserType.UserTypeIdLocalByRemote(User_Type_Id);
                        db_UserType.cerrar(); */


                        db_Usuarios.abrirBaseDeDatos();
                        db_Usuarios.InsertUser(User_Id, User_Type_Id, Nombre_Tipo_Usuario, Referente_Id, Name_Referente, Sector_Id, Name_Municipe,
                                FirstName, LastName, Identification_Card, Profession, Birth_Date, Phone1, Phone2, Email, Address,
                                Coords_Location, Have_Vehicle, Vehicle_Type, Vehicle_Plate, Password, Picture, Is_Leader, department_id);
                        db_Usuarios.cerrar();

                    } else {
                        length = list_usuario.size();
                        for (User user : list_usuario) {
                            User_Id = user.getUser_Id();
                            Nombre_Tipo_Usuario = user.getName_User_Type();
                            FirstName = user.getFirstName();
                            LastName = user.getLastName();
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (length == 0) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Alerta");
                builder.setMessage("Usuario o Contraseña incorrectos");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                Intent i = new Intent(LoginActivity.this, MapsActivity.class);
                Toast.makeText(getApplicationContext(), "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                i.putExtra("user_id", User_Id);
                i.putExtra("name_user_type", Nombre_Tipo_Usuario);
                i.putExtra("firstname", FirstName);
                i.putExtra("lastname", LastName);
                startActivity(i);
                finish();
            }
        }

    }

    //endregion


    //region Método de validación de conexión a internet
    public Boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");

            int val = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
    //endregion


}
