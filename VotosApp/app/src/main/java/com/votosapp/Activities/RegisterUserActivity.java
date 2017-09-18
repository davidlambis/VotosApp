package com.votosapp.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.votosapp.Controllers.DepartamentoController;
import com.votosapp.Controllers.MunicipioController;
import com.votosapp.Controllers.SectorController;
import com.votosapp.Controllers.UserController;
import com.votosapp.Controllers.ZoneController;
import com.votosapp.CustomClass.HintSpinnerAdapter;
import com.votosapp.Handler.HttpHandler;
import com.votosapp.Models.City;
import com.votosapp.Models.Department;
import com.votosapp.Models.Sector;
import com.votosapp.Models.Sesion;
import com.votosapp.Models.User;
import com.votosapp.Models.Zone;
import com.votosapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterUserActivity extends AppCompatActivity {

    //region Variables
    @Bind(R.id.ToolbarRegisterUser)
    Toolbar ToolbarRegisterUser;
    @Bind(R.id.edtNombres)
    EditText edtNombres;
    @Bind(R.id.edtApellidos)
    EditText edtApellidos;
    @Bind(R.id.edtCedula)
    EditText edtCedula;
    @Bind(R.id.edtProfesion)
    EditText edtProfesion;
    @Bind(R.id.edtTelefono1)
    EditText edtTelefono1;
    @Bind(R.id.edtTelefono2)
    EditText edtTelefono2;
    @Bind(R.id.edtDireccion)
    EditText edtDireccion;
    @Bind(R.id.swTieneVehiculo)
    SwitchCompat swTieneVehiculo;
    @Bind(R.id.edtPlaca)
    EditText edtPlaca;
    @Bind(R.id.edtEmail)
    EditText edtEmail;
    @Bind(R.id.edtPassword)
    EditText edtPassword;
    @Bind(R.id.spinnerDepartamento)
    Spinner spinnerDepartamento;
    @Bind(R.id.spinnerMunicipio)
    Spinner spinnerMunicipio;
    @Bind(R.id.spinnerZona)
    Spinner spinnerZona;
    @Bind(R.id.btnRegistrar)
    AppCompatButton btnRegistrar;
    @Bind(R.id.spinnerSector)
    Spinner spinnerSector;


    private String Name_User_Type;
    private ProgressDialog pDialog;
    public String departamentoSeleccionado, ciudadSeleccionada, zonaseleccionada, sectorseleccionado;
    int Id_Departamento;
    int Id_City, Id_Zone, Id_Sector ;
    long User_Type_Id;
    String Name_Sector, Name_Zone,fecha,latitud_sector , longitud_sector;

    String strUser_Type_Id, strUser_Id, strName_Referente, strNombres , strApellidos , strCedula , strProfesion, strTelefono1, strTelefono2, strEmail, strAddress, strPassword, strPlaca  ;

    int Estado_Sesion, User_Id;
    String FirstName, LastName;

    DepartamentoController db_departamentos;
    //Instancia del controlador de municipios
    MunicipioController db_municipios;

    ZoneController db_zones;

    SectorController db_sectors;

    UserController db_usuarios;

    //Elementos de Spinner de Departamento
    private ArrayList<String> itemsSpinnerDepartamento; // itemsSpinnerDepartamento --> spinnerDepartamentos
    private ArrayAdapter<String> adapterSpinnerDepartamento; // adapterSpinnerDepartamento --> adaptersDepartamentos
    //Elementos de Spinner de Municipio
    private ArrayList<String> itemsSpinnerMunicipio; // itemsSpinnerMunicipio --> spinnerCiudades
    private ArrayAdapter<String> adapterSpinnerMunicipio; // adapterSpinnerMunicipio --> adaptersCiudades

    private ArrayList<String> itemsSpinnerZona;
    private ArrayAdapter<String> adapterSpinnerZona;

    private ArrayList<String> itemsSpinnerSector;
    private ArrayAdapter<String> adapterSpinnerSector;
    //endregion


    //region OnCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        ButterKnife.bind(this);

        db_usuarios = new UserController(this);
        ArrayList<Sesion> list_sesion = new ArrayList<>();
        db_usuarios.abrirBaseDeDatos();
        list_sesion = db_usuarios.GetSesion();
        db_usuarios.cerrar();
        if (list_sesion.size() > 0) {
            for (Sesion s : list_sesion) {
                Estado_Sesion = s.getEstado_Sesion();
                User_Id = s.getUser_Id_Sesion();
                strUser_Id = String.valueOf(User_Id);
                Name_User_Type = s.getName_User_Type_Sesion();
                FirstName = s.getFirstname_Sesion();
                LastName = s.getLastname_Sesion();
                strName_Referente = FirstName + " " + LastName;
            }
        }
        //Coloca título al toolbar y habilita el retorno a la actividad anterior
        Toolbar toolbar = (Toolbar) findViewById(R.id.ToolbarRegisterUser);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Name_User_Type.equals("Candidato")) {
            toolbar.setTitle("Registrar Líder");
            User_Type_Id = 2;
            strUser_Type_Id = String.valueOf(User_Type_Id);
            edtPassword.setVisibility(View.VISIBLE);
        }
        if (Name_User_Type.equals("Lider")) {
            toolbar.setTitle("Registrar Usuario");
            User_Type_Id = 3;
            strUser_Type_Id = String.valueOf(User_Type_Id);
            edtPassword.setVisibility(View.GONE);
        }

        //Cónexion al controlador de Departamentos
        db_departamentos = new DepartamentoController(this);

        //Conexión al controlador de Municipios
        db_municipios = new MunicipioController(this);

        db_zones = new ZoneController(this);

        db_sectors = new SectorController(this);



        itemsSpinnerDepartamento = new ArrayList<String>();
        itemsSpinnerMunicipio = new ArrayList<String>();
        itemsSpinnerZona = new ArrayList<String>();
        itemsSpinnerSector = new ArrayList<String>();
        cargaSpinnerDepartamento();
        spinnerMunicipio.setVisibility(View.GONE);


    }
    //endregion


    @OnClick(R.id.btnRegistrar)
    public void Registrar() {
        strNombres = edtNombres.getText().toString();
        strApellidos = edtApellidos.getText().toString();
        strCedula = edtCedula.getText().toString();
        strProfesion = edtProfesion.getText().toString();
        strTelefono1 = edtTelefono1.getText().toString();
        strTelefono2 = edtTelefono2.getText().toString();
        strEmail = edtEmail.getText().toString();
        strAddress = edtDireccion.getText().toString();
        strPlaca = edtPlaca.getText().toString();
        if (TextUtils.isEmpty(strNombres)) {
            edtNombres.setError("Llena este campo");
            return;
        }
        if (TextUtils.isEmpty(strApellidos)) {
            edtApellidos.setError("Llena este campo");
            return;
        }
        if (TextUtils.isEmpty(strCedula)) {
            edtCedula.setError("Llena este campo");
            return;
        }
        if (TextUtils.isEmpty(strProfesion)) {
            edtProfesion.setError("Llena este campo");
            return;
        }
        if (TextUtils.isEmpty(strTelefono1)) {
            edtTelefono1.setError("Llena este campo");
            return;
        }

        if (TextUtils.isEmpty(strEmail)) {
            edtEmail.setError("Llena este campo");
            return;
        }

        if (TextUtils.isEmpty(strAddress)) {
            edtDireccion.setError("Llena este campo");
            return;
        }

        if (Name_User_Type.equals("Candidato")) {
            strPassword = edtPassword.getText().toString();
            if (TextUtils.isEmpty(strPassword)) {
                edtPassword.setError("Llena este campo");
                return;
            }
        }
        if (spinnerDepartamento.getSelectedItem() == null ) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserActivity.this);
            builder.setTitle("Alerta");
            builder.setMessage("No Seleccionaste departamento");
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }
        if (spinnerMunicipio.getSelectedItem() == null ) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserActivity.this);
            builder.setTitle("Alerta");
            builder.setMessage("No Seleccionaste municipio");
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }
        if (spinnerZona.getSelectedItem() == null ) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserActivity.this);
            builder.setTitle("Alerta");
            builder.setMessage("No Seleccionaste zona");
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }
        if (spinnerSector.getSelectedItem() == null ) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserActivity.this);
            builder.setTitle("Alerta");
            builder.setMessage("No Seleccionaste sector");
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }
        registrarUsuario();

    }


    public void registrarUsuario(){
        if(isOnlineNet()){
            new registrarUsuario().execute("http://ec2-34-234-94-92.compute-1.amazonaws.com/api/Users");
            int Estado_Sincronizacion = 1;
            //TODO SABADO copiar el asynctask de get las zonas por ciudad, para traer la latitud y longitud del sector e insertarlo en coords_location y en picture
            //TODO Reemplazar el campo de coords_location y picture de null con la latitud y longitud que traiga.
            db_usuarios.abrirBaseDeDatos();
            db_usuarios.InsertUser(0, User_Type_Id, Name_User_Type, String.valueOf(User_Id), strName_Referente , Id_Sector , ciudadSeleccionada,
                    strNombres, strApellidos, strCedula, strProfesion, null , strTelefono1, strTelefono2, strEmail, strAddress, latitud_sector,
                    null, null, strPlaca, strPassword, longitud_sector, null , Id_Departamento, Id_Zone, Estado_Sincronizacion,Id_City);
            db_usuarios.cerrar();

        }else{
            int Estado_Sincronizacion = 0;
            db_sectors.abrirBaseDeDatos();
            ArrayList<Sector> sector = new ArrayList<>();
            sector = db_sectors.GetSectorById(Id_Sector);
            db_sectors.cerrar();
            for (Sector s : sector){
                latitud_sector = s.getLatitude();
                longitud_sector = s.getLongitude();
            }
            db_usuarios.abrirBaseDeDatos();
            //TODO traer la latitud y la longitud del sector e insertarlas en los campos de coords_location y picture que están null.
            db_usuarios.InsertUser(0, User_Type_Id, Name_User_Type, String.valueOf(User_Id), strName_Referente , Id_Sector , ciudadSeleccionada,
                    strNombres, strApellidos, strCedula, strProfesion, null , strTelefono1, strTelefono2, strEmail, strAddress, latitud_sector,
                    null, null, strPlaca, strPassword, longitud_sector, null , Id_Departamento, Id_Zone, Estado_Sincronizacion,Id_City);
            db_usuarios.cerrar();
            final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserActivity.this);
            builder.setMessage("Usuario registrado en el dispositivo correctamente");
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent i = new Intent(RegisterUserActivity.this, MapsActivity.class);
                    startActivity(i);
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        Log.i("URl", "" + myurl);
        myurl = myurl.replace(" ", "%20");
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 20000;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(90000 /* milliseconds */);
            conn.setConnectTimeout(95000 /* milliseconds */);

            //POSTEA
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            DateFormat dateFormatFH = new SimpleDateFormat("yyyy-MM-dd");
            Date dateFH = new Date();
            String fecha = dateFormatFH.format(dateFH);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("User_Type_Id",String.valueOf(User_Type_Id))
                    .appendQueryParameter("Referente_Id", String.valueOf(User_Id))
                    .appendQueryParameter("Name_Referente",strName_Referente)
                    .appendQueryParameter("Department_Id", String.valueOf(Id_Departamento))
                    .appendQueryParameter("City_Id", String.valueOf(Id_City))
                    .appendQueryParameter("Zone_Id", String.valueOf(Id_Zone))
                    .appendQueryParameter("Sector_Id", String.valueOf(Id_Sector))
                    .appendQueryParameter("Name_Municipe",ciudadSeleccionada )
                    .appendQueryParameter("FirstName", strNombres)
                    .appendQueryParameter("LastName", strApellidos)
                    .appendQueryParameter("Identification_Card",strCedula)
                    .appendQueryParameter("Profession", strProfesion)
                    .appendQueryParameter("Birth_Date",fecha)
                    .appendQueryParameter("Phone1", strTelefono1)
                    .appendQueryParameter("Phone2", strTelefono2)
                    .appendQueryParameter("Email",strEmail)
                    .appendQueryParameter("Address", strAddress )
                    .appendQueryParameter("Password",strPassword)
                  ;

            String query = builder.build().getEncodedQuery();

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();

            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("Respuesta", "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }




    // Lee un inputStream y lo convierte en un String .
    public String readIt(InputStream stream, int len) throws IOException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }


   private class registrarUsuario extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(RegisterUserActivity.this);
            pDialog.setMessage("Cargando...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return null;
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                // Dismiss the progress dialog
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserActivity.this);
                builder.setMessage("Usuario registrado éxitosamente");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i = new Intent(RegisterUserActivity.this, MapsActivity.class);
                        startActivity(i);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }else{
                final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserActivity.this);
                builder.setMessage("Error al registrar usuario");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i = new Intent(RegisterUserActivity.this, MapsActivity.class);
                        startActivity(i);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }

    }


    //region Carga de Spinners
    public void cargaSpinnerDepartamento() {
        if (isOnlineNet()) {
            itemsSpinnerDepartamento.clear();
            new GetAllDepartamentosApp().execute();
            //new GetDepartamentos().execute();
        } else {
            itemsSpinnerDepartamento.clear();
            spinnerMunicipio.setVisibility(View.GONE);
            spinnerZona.setVisibility(View.GONE);
            spinnerSector.setVisibility(View.GONE);
            ArrayList<Department> list_departamentos = new ArrayList<>();
            db_departamentos.abrirBaseDeDatos();
            list_departamentos = db_departamentos.GetDepartamentos();
            db_departamentos.cerrar();
            if (list_departamentos.size() > 0) {
                for (Department department : list_departamentos) {
                    String d = department.getName();
                    itemsSpinnerDepartamento.add(d);
                }
                adapterSpinnerDepartamento = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, itemsSpinnerDepartamento);
                spinnerDepartamento.setAdapter(new HintSpinnerAdapter(adapterSpinnerDepartamento, R.layout.hint_row_item_departamento, getApplicationContext()));

                spinnerDepartamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int position2 = position - 1;
                        if (position2 != -1) {
                            departamentoSeleccionado = itemsSpinnerDepartamento.get(position2);
                            spinnerZona.setVisibility(View.GONE);
                            spinnerSector.setVisibility(View.GONE);
                            db_departamentos.abrirBaseDeDatos();
                            Id_Departamento = db_departamentos.GetIdDepartamentoByName(departamentoSeleccionado);
                            db_departamentos.cerrar();
                            itemsSpinnerMunicipio.clear();
                            cargaSpinnerMunicipio();
                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            } else {
                final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserActivity.this);
                builder.setTitle("Alerta");
                builder.setMessage("No hay departamentos registrados en el dispositivo");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }

    }

    public void cargaSpinnerMunicipio() {
        if (isOnlineNet()) {
            new GetCitiesByDepartamentoApp().execute();
            //new GetCiudadesByDepartamento().execute();
        } else {
            db_departamentos.abrirBaseDeDatos();
            long id_departamento = db_departamentos.GetIdDepartamentoByName(departamentoSeleccionado);
            db_departamentos.cerrar();
            ArrayList<City> list_municipios = new ArrayList<>();
            db_municipios.abrirBaseDeDatos();
            list_municipios = db_municipios.GetMunicipiosByIdDepartamento(id_departamento);
            db_municipios.cerrar();

            if (list_municipios.size() > 0) {
                for (City municipio : list_municipios) {
                    String m = municipio.getName();
                    itemsSpinnerMunicipio.add(m);
                }
                spinnerMunicipio.setVisibility(View.VISIBLE);
                adapterSpinnerMunicipio = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, itemsSpinnerMunicipio);
                spinnerMunicipio.setAdapter(new HintSpinnerAdapter(adapterSpinnerMunicipio, R.layout.hint_row_item_municipio, getApplicationContext()));

                spinnerMunicipio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //Obtiene el dato del municipio seleccionado
                        int positionM = position - 1;
                        if (positionM != -1) {
                            ciudadSeleccionada = itemsSpinnerMunicipio.get(positionM);
                            spinnerZona.setVisibility(View.GONE);
                            spinnerSector.setVisibility(View.GONE);
                            db_municipios.abrirBaseDeDatos();
                            Id_City = db_municipios.GetIdMunicipioByName(ciudadSeleccionada);
                            db_municipios.cerrar();
                            itemsSpinnerZona.clear();
                            cargaSpinnerZona();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            } else {
                final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserActivity.this);
                builder.setTitle("Alerta");
                builder.setMessage("No hay municipios registrados en el dispositivo");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                spinnerMunicipio.setVisibility(View.GONE);
                Id_City = 0;


            }

        }
    }

    public void cargaSpinnerZona() {
        if (isOnlineNet()) {
            new GetZonasByCity().execute();
            //new GetCiudadesByDepartamento().execute();
        }else{
            db_municipios.abrirBaseDeDatos();
            int id_city = db_municipios.GetIdMunicipioByName(ciudadSeleccionada);
            db_municipios.cerrar();

            db_zones.abrirBaseDeDatos();
            ArrayList<Zone> list_zones = new ArrayList<>();
            list_zones = db_zones.GetZonesByCityId(id_city);
            db_zones.cerrar();

            if (list_zones.size() > 0) {
                for (Zone zone : list_zones) {
                    String z = zone.getName();
                    itemsSpinnerZona.add(z);
                }
                spinnerZona.setVisibility(View.VISIBLE);
                adapterSpinnerZona = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, itemsSpinnerZona);
                spinnerZona.setAdapter(new HintSpinnerAdapter(adapterSpinnerZona, R.layout.hint_row_item_zona, getApplicationContext()));

                spinnerZona.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //Obtiene el dato del municipio seleccionado
                        int positionC = position - 1;
                        if (positionC != -1) {
                            zonaseleccionada = itemsSpinnerZona.get(positionC);
                            db_zones.abrirBaseDeDatos();
                            Id_Zone = db_zones.GetIdZoneByName(zonaseleccionada);
                            db_zones.cerrar();
                            spinnerSector.setVisibility(View.GONE);
                            itemsSpinnerSector.clear();
                            cargaSpinnerSector();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            } else {
                final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserActivity.this);
                builder.setTitle("Alerta");
                builder.setMessage("No hay zonas registradas en el dispositivo");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                Id_Zone = 0;
            }
        }
    }


    public void cargaSpinnerSector() {
        if (isOnlineNet()) {
            new GetSectorsByZona().execute();
        }else {
            db_municipios.abrirBaseDeDatos();
            int id_city = db_municipios.GetIdMunicipioByName(ciudadSeleccionada);
            db_municipios.cerrar();

            ArrayList<Sector> list_sectores = new ArrayList<>();
            db_sectors.abrirBaseDeDatos();
            list_sectores = db_sectors.GetSectorsByCityId(id_city);
            db_sectors.cerrar();

            if (list_sectores.size() > 0) {
                for (Sector sector : list_sectores) {
                    String z = sector.getName();
                    itemsSpinnerSector.add(z);
                }
                spinnerSector.setVisibility(View.VISIBLE);
                adapterSpinnerSector = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, itemsSpinnerSector);
                spinnerSector.setAdapter(new HintSpinnerAdapter(adapterSpinnerSector, R.layout.hint_row_item_sector, getApplicationContext()));

                spinnerSector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //Obtiene el dato del municipio seleccionado
                        int positionV = position - 1;
                        if (positionV != -1) {
                            sectorseleccionado = itemsSpinnerSector.get(positionV);
                            db_sectors.abrirBaseDeDatos();
                            Id_Sector = db_sectors.GetIdSectorByName(sectorseleccionado);
                            db_sectors.cerrar();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            } else {
                final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserActivity.this);
                builder.setTitle("Alerta");
                builder.setMessage("No hay sectores registrados en el dispositivo");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                Id_Sector = 0;
                spinnerSector.setVisibility(View.GONE);
            }
        }
    }
    //endregion


    //region Tareas asíncronas departamentos y municipios
    private class GetAllDepartamentosApp extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(RegisterUserActivity.this);
            pDialog.setMessage("Cargando...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            String urlDepartamentos = "http://ec2-34-234-94-92.compute-1.amazonaws.com/api/Departments/DepartmentsAll";
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(urlDepartamentos);
            if (jsonStr != null) {
                try {

                    JSONArray jsonArray = new JSONArray(jsonStr);

                    ArrayList<Department> list_d = new ArrayList<>();
                    db_departamentos.abrirBaseDeDatos();
                    list_d = db_departamentos.GetDepartamentos();
                    db_departamentos.cerrar();
                    if (list_d.size() == jsonArray.length()) {
                        for (Department d : list_d) {
                            String d_name = d.getName();
                            itemsSpinnerDepartamento.add(d_name);
                        }
                    } else {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            int id_departamento = c.getInt("Department_Id");
                            String nombre_departamento = c.getString("Name");
                            itemsSpinnerDepartamento.add(nombre_departamento);

                            db_departamentos.abrirBaseDeDatos();
                            ArrayList<Department> l_d = new ArrayList<>();
                            l_d = db_departamentos.GetDepartamentos();
                            db_departamentos.cerrar();
                            if (l_d.size() < jsonArray.length()) {
                                db_departamentos.abrirBaseDeDatos();
                                db_departamentos.InsertDepartamento(nombre_departamento, id_departamento);
                                db_departamentos.cerrar();

                                db_departamentos.abrirBaseDeDatos();
                                long id_departamento_local = db_departamentos.GetIdDepartamentoLocalByName(nombre_departamento);
                                db_departamentos.cerrar();

                                ArrayList<String> list_municipios = new ArrayList<String>();
                                JSONArray jArray = c.getJSONArray("Cities");
                                for (int j = 0; j < jArray.length(); j++) {
                                    JSONObject m = jArray.getJSONObject(j);
                                    int City_Id = m.getInt("City_Id");
                                    String Name = m.getString("Name");
                                    db_municipios.abrirBaseDeDatos();
                                    db_municipios.InsertMunicipio(id_departamento_local, City_Id, Name);
                                    db_municipios.cerrar();
                                }
                            }
                        }
                    }

                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            // Pone los departamentos obtenidos en el spinner de departamentos de la actividad
            adapterSpinnerDepartamento = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, itemsSpinnerDepartamento);
            spinnerDepartamento.setAdapter(new HintSpinnerAdapter(adapterSpinnerDepartamento, R.layout.hint_row_item_departamento, getApplicationContext()));

            spinnerDepartamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //De acuerdo al departamento que seleccione el usuario ejecutará la tarea asíncrona para obtener los municipios del mismo.
                    int position2 = position - 1;
                    if (position2 != -1) {
                        departamentoSeleccionado = itemsSpinnerDepartamento.get(position2);
                        db_departamentos.abrirBaseDeDatos();
                        Id_Departamento = db_departamentos.GetIdDepartamentoByName(departamentoSeleccionado);
                        db_departamentos.cerrar();
                        itemsSpinnerMunicipio.clear();
                        cargaSpinnerMunicipio();

                    }
                    spinnerZona.setVisibility(View.GONE);
                    spinnerSector.setVisibility(View.GONE);


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });


        }

    }

    private class GetCitiesByDepartamentoApp extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(RegisterUserActivity.this);
            pDialog.setMessage("Cargando...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            HttpHandler sh = new HttpHandler();

            String urlMunicipio = "http://ec2-34-234-94-92.compute-1.amazonaws.com/api/Departments/DepartmentsAll";
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(urlMunicipio);

            if (jsonStr != null) {
                try {

                    JSONArray jsonArray = new JSONArray(jsonStr);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        if (c.getString("Name").equals(departamentoSeleccionado)) {
                            JSONArray ciudades = c.getJSONArray("Cities");
                            for (int j = 0; j < ciudades.length(); j++) {
                                JSONObject m = ciudades.getJSONObject(j);
                                int city_id = m.getInt("City_Id");
                                String nombre_municipio = m.getString("Name");
                                itemsSpinnerMunicipio.add(nombre_municipio);
                            }
                        }
                    }

                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (itemsSpinnerMunicipio.size() == 0) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserActivity.this);
                builder.setTitle("Alerta");
                builder.setMessage("No hay Municipios registrados en este departamento");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                spinnerMunicipio.setVisibility(View.GONE);
                Id_City = 0;
            } else {

                spinnerMunicipio.setVisibility(View.VISIBLE);
                //Los municipios del departamento seleccionado son asignados al spinner de municipios
                adapterSpinnerMunicipio = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, itemsSpinnerMunicipio);
                spinnerMunicipio.setAdapter(new HintSpinnerAdapter(adapterSpinnerMunicipio, R.layout.hint_row_item_municipio, getApplicationContext()));

                spinnerMunicipio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //Obtiene el dato del municipio seleccionado
                        int positionM = position - 1;
                        if (positionM != -1) {
                            ciudadSeleccionada = itemsSpinnerMunicipio.get(positionM);
                            db_municipios.abrirBaseDeDatos();
                            Id_City = db_municipios.GetIdMunicipioByName(ciudadSeleccionada);
                            db_municipios.cerrar();
                            //itemsSpinnerCorregimiento.clear();
                            itemsSpinnerZona.clear();
                            spinnerSector.setVisibility(View.GONE);
                            cargaSpinnerZona();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

            }
        }

    }

    private class GetZonasByCity extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(RegisterUserActivity.this);
            pDialog.setMessage("Cargando...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            String urlZonas = "http://ec2-34-234-94-92.compute-1.amazonaws.com/api/Departments/GetZonesByCities/" + Id_City;
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(urlZonas);
            if (jsonStr != null) {
                try {

                    JSONArray jsonArray = new JSONArray(jsonStr);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        int id_zone = c.getInt("Zone_Id");
                        int type_zone_id = c.getInt("Type_Zone_id");
                        int id_city = Id_City;
                        int sector_type_id = c.getInt("Sector_Type_id");
                        String Name = c.getString("Name");
                        String description = c.getString("Description");
                        String latitude = c.getString("Latitude");
                        String longitude = c.getString("Longitude");

                        /*JSONObject object = c.getJSONObject("Sector_Type");
                        Name_Sector = object.getString("Name"); */

                        /*JSONObject object = c.getJSONObject("Zone_Type");
                        Name_Zone = object.getString("Name"); */

                        //Id_Zone = id_zone;
                        db_zones.abrirBaseDeDatos();
                        ArrayList<Zone> list_zones = new ArrayList<>();
                        list_zones = db_zones.GetZoneById(id_zone);
                        db_zones.cerrar();

                        if (list_zones.size() == 0) {
                            db_zones.abrirBaseDeDatos();
                            db_zones.InsertZone(id_zone, type_zone_id, id_city, sector_type_id, Name, description, latitude, longitude);
                            db_zones.cerrar();
                        }
                        itemsSpinnerZona.add(Name);

                    }


                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (itemsSpinnerZona.size() == 0) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserActivity.this);
                builder.setTitle("Alerta");
                builder.setMessage("No hay Zonas registradas en este municipio");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                spinnerZona.setVisibility(View.GONE);
                Id_City = 0;
                Id_Zone = 0;
            } else {


                spinnerZona.setVisibility(View.VISIBLE);
                adapterSpinnerZona = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, itemsSpinnerZona);
                spinnerZona.setAdapter(new HintSpinnerAdapter(adapterSpinnerZona, R.layout.hint_row_item_zona, getApplicationContext()));

                spinnerZona.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //Obtiene el dato del municipio seleccionado
                        int positionC = position - 1;
                        if (positionC != -1) {
                            zonaseleccionada = itemsSpinnerZona.get(positionC);
                            db_zones.abrirBaseDeDatos();
                            Id_Zone = db_zones.GetIdZoneByName(zonaseleccionada);
                            db_zones.cerrar();
                            itemsSpinnerSector.clear();
                            cargaSpinnerSector();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }

        }

    }

    private class GetSectorsByZona extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(RegisterUserActivity.this);
            pDialog.setMessage("Cargando...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            String urlZonas = "http://ec2-34-234-94-92.compute-1.amazonaws.com/api/Departments/GetZonesByCities/" + Id_City;
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(urlZonas);
            if (jsonStr != null) {
                try {

                    JSONArray jsonArray = new JSONArray(jsonStr);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        int id_zone = c.getInt("Zone_Id");
                        int type_zone_id = c.getInt("Type_Zone_id");
                        int id_city = Id_City;
                        int sector_type_id = c.getInt("Sector_Type_id");
                        String Name = c.getString("Name");
                        String description = c.getString("Description");
                        String latitude = c.getString("Latitude");
                        String longitude = c.getString("Longitude");


                        if (Name.equals(zonaseleccionada)) {
                            JSONArray jArray = c.getJSONArray("Sectors");
                            for (int j = 0; j < jArray.length(); j++) {
                                JSONObject d = jArray.getJSONObject(j);
                                Id_Sector = d.getInt("Sector_Id");
                                int tipo_sector_id = d.getInt("Sector_Type_id");
                                int zone_id = d.getInt("Zone_Id");
                                String name_sector = d.getString("Name");
                                String descripcion = d.getString("Description");
                                latitud_sector = d.getString("Latitude");
                                longitud_sector = d.getString("Longitude");


                                db_sectors.abrirBaseDeDatos();
                                ArrayList<Sector> list_sectors = new ArrayList<>();
                                list_sectors = db_sectors.GetSectorById(Id_Sector);
                                db_sectors.cerrar();

                                if (list_sectors.size() == 0) {
                                    db_sectors.abrirBaseDeDatos();
                                    db_sectors.InsertSector(Id_Sector, tipo_sector_id, zone_id, name_sector, descripcion, latitud_sector, longitud_sector, Id_City);
                                    db_sectors.cerrar();
                                }
                                itemsSpinnerSector.add(name_sector);
                            }
                        }

                        /*JSONObject object = c.getJSONObject("Sector_Type");
                        Name_Sector = object.getString("Name"); */

                        /*JSONObject object = c.getJSONObject("Zone_Type");
                        Name_Zone = object.getString("Name"); */

                        //Id_Zone = id_zone;
                        /*db_zones.abrirBaseDeDatos();
                        ArrayList<Zone> list_zones = new ArrayList<>();
                        list_zones = db_zones.GetZoneById(id_zone);
                        db_zones.cerrar();

                        if (list_zones.size() == 0) {
                            db_zones.abrirBaseDeDatos();
                            db_zones.InsertZone(id_zone, type_zone_id, id_city, sector_type_id, Name, description, latitude, longitude);
                            db_zones.cerrar();
                        }
                        itemsSpinnerZona.add(Name);*/

                    }


                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (itemsSpinnerSector.size() == 0) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserActivity.this);
                builder.setTitle("Alerta");
                builder.setMessage("No hay Sectores registrados en esta zona");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                spinnerSector.setVisibility(View.GONE);
                Id_City = 0;
                Id_Zone = 0;
                Id_Sector = 0;
            } else {
                spinnerSector.setVisibility(View.VISIBLE);
                adapterSpinnerSector = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, itemsSpinnerSector);
                spinnerSector.setAdapter(new HintSpinnerAdapter(adapterSpinnerSector, R.layout.hint_row_item_sector, getApplicationContext()));

                spinnerSector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //Obtiene el dato del municipio seleccionado
                        int positionC = position - 1;
                        if (positionC != -1) {
                            sectorseleccionado = itemsSpinnerSector.get(positionC);
                            db_sectors.abrirBaseDeDatos();
                            Id_Sector = db_sectors.GetIdSectorByName(sectorseleccionado);
                            db_sectors.cerrar();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }

        }

    }
    //endregion


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.register_user_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, MapsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.action_syncr:
                if(isOnlineNet()){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserActivity.this);
                    builder.setMessage("¿ Deseas Subir los datos almacenados a la nube ? ");
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            db_usuarios.abrirBaseDeDatos();
                            int estado_sincronizacion = 0;
                            ArrayList<User> list_users = new ArrayList<>();
                            list_users = db_usuarios.GetUsersByEstadoSincronizacionFalse(estado_sincronizacion);
                            db_usuarios.cerrar();
                            if(list_users.size() > 0) {
                                //TODO Falta guardar Id_City
                                for (User u : list_users) {
                                    User_Type_Id = u.getUser_Type_Id_Remote();
                                    User_Id = Integer.parseInt(String.valueOf(u.getReferente_Id()));
                                    strName_Referente = u.getName_Referente();
                                    Id_Departamento = u.getDepartment_Id();
                                    Id_Zone = u.getId_Zone();
                                    Id_Sector = Integer.parseInt(String.valueOf(u.getSector_Id_Remote()));
                                    ciudadSeleccionada = u.getName_Municipe();
                                    strNombres = u.getFirstName();
                                    strApellidos = u.getLastName();
                                    strCedula = u.getIdentification_Card();
                                    strProfesion = u.getProfession();
                                    strTelefono1 = u.getPhone1();
                                    strTelefono2 = u.getPhone2();
                                    strEmail = u.getEmail();
                                    strAddress = u.getAddress();
                                    strPassword = u.getPassword();
                                    DateFormat dateFormatFH = new SimpleDateFormat("yyyy-MM-dd");
                                    Date dateFH = new Date();
                                    fecha = dateFormatFH.format(dateFH);
                                    Id_City = u.getId_City();

                                    new registrarUsuario().execute("http://ec2-34-234-94-92.compute-1.amazonaws.com/api/Users");
                                }
                            }
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();


                }else{
                    final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserActivity.this);
                    builder.setMessage("No se pueden subir los datos , no hay conexión a internet");
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //region Método de validación de conexión a internet
    public Boolean isOnlineNet() {

        try {
            Process p = Runtime.getRuntime().exec("ping -c 1 www.google.es");

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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(RegisterUserActivity.this, MapsActivity.class);
        startActivity(intent);
    }
}
