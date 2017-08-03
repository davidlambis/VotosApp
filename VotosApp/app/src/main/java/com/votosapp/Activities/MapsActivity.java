package com.votosapp.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.votosapp.Controllers.DepartamentoController;
import com.votosapp.Controllers.MunicipioController;
import com.votosapp.CustomClass.HintSpinnerAdapter;
import com.votosapp.Handler.HttpHandler;
import com.votosapp.Models.City;
import com.votosapp.Models.Department;
import com.votosapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    //region Info Mapa
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    //endregion Mapa

    //region Variables
    private String Name_User_Type;
    private String FirstName;
    private String LastName;
    //endregion

    //region UI elements
    /*ELEMENTOS DE SPINNERS */
    //Elementos de Spinner de Búsqueda
    private Spinner spinnerBusqueda;
    private ArrayList<String> itemsSpinnerBusqueda;
    private ArrayAdapter<String> adapterSpinnerBusqueda;
    int posBusqueda;
    //Elementos de Spinner de Líder
    private Spinner spinnerLider;
    private ArrayList<String> itemsSpinnerLider;
    private ArrayAdapter<String> adapterSpinnerLider;
    //Elementos de Spinner de Corregimiento
    private Spinner spinnerCorregimiento;
    private ArrayList<String> itemsSpinnerCorregimiento;
    private ArrayAdapter<String> adapterSpinnerCorregimiento;
    //Elementos de Spinner de Comuna
    private Spinner spinnerComuna;
    private ArrayList<String> itemsSpinnerComuna;
    private ArrayAdapter<String> adapterSpinnerComuna;
    //Elementos de Spinner de Vereda
    private Spinner spinnerVereda;
    private ArrayList<String> itemsSpinnerVereda;
    private ArrayAdapter<String> adapterSpinnerVereda;
    //Elementos de Spinner de Barrio
    private Spinner spinnerBarrio;
    private ArrayList<String> itemsSpinnerBarrio;
    private ArrayAdapter<String> adapterSpinnerBarrio;

    //region Departamentos y Municipios
    //Instancia del controlador de departamentos
    long Id_Departamento_Local;
    DepartamentoController db_departamentos;
    //Instancia del controlador de municipios
    MunicipioController db_municipios;

    private ProgressDialog pDialog;
    public String departamentoSeleccionado, ciudadSeleccionada;
    // URL JSON con datos de departamentos y municipios de Colombia
    private static String url = "http://54.237.155.47/iot/colombia.json";

    //Elementos de Spinner de Departamento
    private Spinner spinnerDepartamento;// spinnerDepartamento --> departamentos
    private ArrayList<String> itemsSpinnerDepartamento; // itemsSpinnerDepartamento --> spinnerDepartamentos
    private ArrayAdapter<String> adapterSpinnerDepartamento; // adapterSpinnerDepartamento --> adaptersDepartamentos
    //Elementos de Spinner de Municipio
    private Spinner spinnerMunicipio; // spinnerMunicipio --> ciudades
    private ArrayList<String> itemsSpinnerMunicipio; // itemsSpinnerMunicipio --> spinnerCiudades
    private ArrayAdapter<String> adapterSpinnerMunicipio; // adapterSpinnerMunicipio --> adaptersCiudades


    //endregion
    //endregion

    //region OnCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.ToolbarMap);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //toolbar.setTitle("Consultas y Mapa");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        //Declaración de elementos

        //Cónexion al controlador de Departamentos
        db_departamentos = new DepartamentoController(this);

        //Conexión al controlador de Municipios
        db_municipios = new MunicipioController(this);

        //Obtención de datos que provienen del LoginActivity
        Name_User_Type = getIntent().getExtras().getString("name_user_type");
        FirstName = getIntent().getExtras().getString("firstname");
        LastName = getIntent().getExtras().getString("lastname");
        toolbar.setTitle("Bienvenido, " + FirstName + " " + LastName);
        //Spinners
        spinnerBusqueda = (Spinner) findViewById(R.id.spinnerBusqueda);
        spinnerLider = (Spinner) findViewById(R.id.spinnerLider);
        spinnerDepartamento = (Spinner) findViewById(R.id.spinnerDepartamento);
        spinnerMunicipio = (Spinner) findViewById(R.id.spinnerMunicipio);
        spinnerCorregimiento = (Spinner) findViewById(R.id.spinnerCorregimiento);
        spinnerComuna = (Spinner) findViewById(R.id.spinnerComuna);
        spinnerVereda = (Spinner) findViewById(R.id.spinnerVereda);
        spinnerBarrio = (Spinner) findViewById(R.id.spinnerBarrio);

        /** Uso del spinner de búsqueda**/
        //Arreglo de Consultas para el candidato
        itemsSpinnerDepartamento = new ArrayList<String>();
        itemsSpinnerMunicipio = new ArrayList<String>();
        itemsSpinnerBusqueda = new ArrayList<String>();

        if (Name_User_Type.equals("Candidato")) {
            itemsSpinnerBusqueda.add(0, "Consultar líderes");
            itemsSpinnerBusqueda.add(1, "Consultar usuarios por Líder");
            itemsSpinnerBusqueda.add(2, "Consultar usuarios por Departamento");
            itemsSpinnerBusqueda.add(3, "Consultar usuarios por Municipio");
            itemsSpinnerBusqueda.add(4, "Consultar usuarios por Corregimiento");
            itemsSpinnerBusqueda.add(5, "Consultar usuarios por Comuna");
            itemsSpinnerBusqueda.add(6, "Consultar usuarios por Vereda");
            itemsSpinnerBusqueda.add(7, "Consultar usuarios por Barrio");

            adapterSpinnerBusqueda = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsSpinnerBusqueda);
            spinnerBusqueda.setAdapter(new HintSpinnerAdapter(adapterSpinnerBusqueda, R.layout.hint_row_item_busqueda, this));

            spinnerBusqueda.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    posBusqueda = position;
                    switch (posBusqueda) {
                        case 0:
                            borraSpinner();
                            break;
                        case 1:
                            borraSpinner();
                            break;
                        case 2:
                            spinnerLider.setVisibility(View.VISIBLE);
                            spinnerDepartamento.setVisibility(View.GONE);
                            spinnerMunicipio.setVisibility(View.GONE);
                            spinnerCorregimiento.setVisibility(View.GONE);
                            spinnerComuna.setVisibility(View.GONE);
                            spinnerVereda.setVisibility(View.GONE);
                            spinnerBarrio.setVisibility(View.GONE);
                            cargaSpinnerLider();
                            break;
                        case 3:
                            cargaSpinner();
                            cargaSpinnerDepartamento();
                            break;
                        case 4:
                            cargaSpinner();
                            cargaSpinnerDepartamento();
                            //cargaSpinnerMunicipio();
                            break;
                        case 5:
                            cargaSpinner();
                            cargaSpinnerDepartamento();
                            //cargaSpinnerMunicipio();
                            //cargaSpinnerCorregimiento();
                            break;
                        case 6:
                            cargaSpinner();
                            cargaSpinnerDepartamento();
                            //cargaSpinnerMunicipio();
                            //cargaSpinnerComuna();
                            break;
                        case 7:
                            cargaSpinner();
                            cargaSpinnerDepartamento();
                            //cargaSpinnerMunicipio();
                            //cargaSpinnerCorregimiento();
                            //cargaSpinnerVereda();
                            break;
                        case 8:
                            cargaSpinner();
                            cargaSpinnerDepartamento();
                            //cargaSpinnerMunicipio();
                            //cargaSpinnerComuna();
                            //cargaSpinnerBarrio();
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } else if (Name_User_Type.equals("Lider")) {
            itemsSpinnerBusqueda.add(0, "Consultar todos los usuarios");
            itemsSpinnerBusqueda.add(1, "Consultar usuarios por Departamento");
            itemsSpinnerBusqueda.add(2, "Consultar usuarios por Municipio");
            itemsSpinnerBusqueda.add(3, "Consultar usuarios por Corregimiento");
            itemsSpinnerBusqueda.add(4, "Consultar usuarios por Comuna");
            itemsSpinnerBusqueda.add(5, "Consultar usuarios por Vereda");
            itemsSpinnerBusqueda.add(6, "Consultar usuarios por Barrio");

            adapterSpinnerBusqueda = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsSpinnerBusqueda);
            spinnerBusqueda.setAdapter(new HintSpinnerAdapter(adapterSpinnerBusqueda, R.layout.hint_row_item_busqueda, this));

            spinnerBusqueda.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    posBusqueda = position;
                    switch (posBusqueda) {
                        case 0:
                            borraSpinner();
                            break;
                        case 1:
                            borraSpinner();
                            break;
                        case 2:
                            cargaSpinner();
                            cargaSpinnerDepartamento();
                            break;
                        case 3:
                            cargaSpinner();
                            cargaSpinnerDepartamento();
                            break;
                        case 4:
                            cargaSpinner();
                            cargaSpinnerDepartamento();
                            break;
                        case 5:
                            cargaSpinner();
                            cargaSpinnerDepartamento();
                            break;
                        case 6:
                            cargaSpinner();
                            cargaSpinnerDepartamento();
                            break;
                        case 7:
                            cargaSpinner();
                            cargaSpinnerDepartamento();
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    //endregion

    //region Métodos de los Spinners
    public void borraSpinner() {
        spinnerLider.setVisibility(View.GONE);
        spinnerDepartamento.setVisibility(View.GONE);
        spinnerMunicipio.setVisibility(View.GONE);
        spinnerCorregimiento.setVisibility(View.GONE);
        spinnerComuna.setVisibility(View.GONE);
        spinnerVereda.setVisibility(View.GONE);
        spinnerBarrio.setVisibility(View.GONE);
    }

    public void cargaSpinner() {
        spinnerLider.setVisibility(View.GONE);
        spinnerDepartamento.setVisibility(View.VISIBLE);
        spinnerMunicipio.setVisibility(View.GONE);
        spinnerCorregimiento.setVisibility(View.GONE);
        spinnerComuna.setVisibility(View.GONE);
        spinnerVereda.setVisibility(View.GONE);
        spinnerBarrio.setVisibility(View.GONE);
    }


    public void cargaSpinnerLider() {
        itemsSpinnerLider = new ArrayList<String>();
        itemsSpinnerLider.add(0, "Líder de Prueba");
        adapterSpinnerLider = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsSpinnerLider);
        spinnerLider.setAdapter(new HintSpinnerAdapter(adapterSpinnerLider, R.layout.hint_row_item_lider, this));
    }

    public void cargaSpinnerDepartamento() {
        if (isOnlineNet()) {
            new GetDepartamentos().execute();
        } else {
            itemsSpinnerDepartamento.clear();
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
                        }
                        if ((posBusqueda == 3 || posBusqueda == 4 || posBusqueda == 5 || posBusqueda == 6 || posBusqueda == 7 || posBusqueda == 8)
                                && position != 0) {
                            itemsSpinnerMunicipio.clear();
                            cargaSpinnerMunicipio();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            } else {
                Toast.makeText(this, "No hay Departamentos almacenados en el dispositivo, por favor accede a internet", Toast.LENGTH_SHORT).show();
                spinnerLider.setVisibility(View.GONE);
                spinnerDepartamento.setVisibility(View.GONE);
                spinnerMunicipio.setVisibility(View.GONE);
                spinnerCorregimiento.setVisibility(View.GONE);
                spinnerComuna.setVisibility(View.GONE);
                spinnerVereda.setVisibility(View.GONE);
                spinnerBarrio.setVisibility(View.GONE);
            }
        }

    }

    public void cargaSpinnerMunicipio() {
        if (isOnlineNet()) {
            new GetCiudadesByDepartamento().execute();
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
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            } else {
                Toast.makeText(this, "No hay Municipios almacenados en el dispositivo, por favor accede a internet", Toast.LENGTH_SHORT).show();
                spinnerLider.setVisibility(View.GONE);
                spinnerDepartamento.setVisibility(View.GONE);
                spinnerMunicipio.setVisibility(View.GONE);
                spinnerCorregimiento.setVisibility(View.GONE);
                spinnerComuna.setVisibility(View.GONE);
                spinnerVereda.setVisibility(View.GONE);
                spinnerBarrio.setVisibility(View.GONE);
            }

        }
    }

    public void cargaSpinnerCorregimiento() {
        itemsSpinnerCorregimiento = new ArrayList<String>();
        itemsSpinnerCorregimiento.add(0, "Corregimiento de Prueba");
        adapterSpinnerCorregimiento = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsSpinnerCorregimiento);
        spinnerCorregimiento.setAdapter(new HintSpinnerAdapter(adapterSpinnerCorregimiento, R.layout.hint_row_item_corregimiento, this));
    }

    public void cargaSpinnerComuna() {
        itemsSpinnerComuna = new ArrayList<String>();
        itemsSpinnerComuna.add(0, "Comuna de Prueba");
        adapterSpinnerComuna = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsSpinnerComuna);
        spinnerComuna.setAdapter(new HintSpinnerAdapter(adapterSpinnerComuna, R.layout.hint_row_item_comuna, this));
    }

    public void cargaSpinnerVereda() {
        itemsSpinnerVereda = new ArrayList<String>();
        itemsSpinnerVereda.add(0, "Vereda de Prueba");
        adapterSpinnerVereda = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsSpinnerVereda);
        spinnerVereda.setAdapter(new HintSpinnerAdapter(adapterSpinnerVereda, R.layout.hint_row_item_vereda, this));
    }

    public void cargaSpinnerBarrio() {
        itemsSpinnerBarrio = new ArrayList<String>();
        itemsSpinnerBarrio.add(0, "Barrio de Prueba");
        adapterSpinnerBarrio = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsSpinnerBarrio);
        spinnerBarrio.setAdapter(new HintSpinnerAdapter(adapterSpinnerBarrio, R.layout.hint_row_item_barrio, this));
    }


    //endregion

    //region Métodos del menú
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Asigna el menú que tiene una opción de cerrar sesión y un ícono para acceder a la actividad de reportes ReportesActivity
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Cuando se presiona cerrar sesión retorna al LoginActivity
            case R.id.cerrarSesion:
                Intent intent = new Intent(MapsActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion

    //region MAPA
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Tu Ubicación");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        mCurrLocationMarker.showInfoWindow();

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }
    //endregion

    //region TAREAS ASÍNCRONAS PARA DEPARTAMENTOS Y MUNICIPIOS

    //Tarea asíncrona para obtener el listado de departamentos de Colombia en base a una URL con los datos en formato JSON
    private class GetDepartamentos extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MapsActivity.this);
            pDialog.setMessage("Cargando...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);
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
                            String departamento = c.getString("departamento");
                            itemsSpinnerDepartamento.add(departamento);
                            db_departamentos.abrirBaseDeDatos();
                            ArrayList<Department> l_d = new ArrayList<>();
                            l_d = db_departamentos.GetDepartamentos();
                            db_departamentos.cerrar();
                            if (l_d.size() < jsonArray.length()) {
                                db_departamentos.abrirBaseDeDatos();
                                db_departamentos.InsertDepartamento(departamento);
                                db_departamentos.cerrar();

                                db_departamentos.abrirBaseDeDatos();
                                Id_Departamento_Local = db_departamentos.GetIdDepartamentoByName(departamento);
                                db_departamentos.cerrar();

                                ArrayList<String> list_municipios = new ArrayList<String>();
                                JSONArray jArray = c.getJSONArray("ciudades");
                                for (int j = 0; j < jArray.length(); j++) {
                                    list_municipios.add(jArray.getString(j));
                                    db_municipios.abrirBaseDeDatos();
                                    db_municipios.InsertMunicipio(Id_Departamento_Local, jArray.getString(j));
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
                    }
                    if ((posBusqueda == 3  || posBusqueda == 4 || posBusqueda == 5 || posBusqueda == 6 || posBusqueda == 7 || posBusqueda == 8)
                            && position != 0) {
                        itemsSpinnerMunicipio.clear();
                        cargaSpinnerMunicipio();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        }

    }

    //Tarea asíncrona para obtener los municipios del departamento seleccionado por el usuario y asignarlos en el spinner de municipios.
    private class GetCiudadesByDepartamento extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MapsActivity.this);
            pDialog.setMessage("Cargando...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            if (jsonStr != null) {
                try {

                    JSONArray jsonArray = new JSONArray(jsonStr);
                    db_departamentos.abrirBaseDeDatos();
                    long id_department = db_departamentos.GetIdDepartamentoByName(departamentoSeleccionado);
                    db_departamentos.cerrar();
                    db_municipios.abrirBaseDeDatos();
                    ArrayList<City> list_m = new ArrayList<>();
                    list_m = db_municipios.GetMunicipiosByIdDepartamento(id_department);
                    db_municipios.cerrar();
                    if (list_m.size() > 0) {
                        for (City c : list_m) {
                            String m_name = c.getName();
                            itemsSpinnerMunicipio.add(m_name);
                        }
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            if (c.getString("departamento").equals(departamentoSeleccionado)) {
                                JSONArray ciudades = c.getJSONArray("ciudades");
                                for (int j = 0; j < ciudades.length(); j++) {
                                    itemsSpinnerMunicipio.add(ciudades.getString(j));
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
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

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


    //region Método que restringe el retorno a la actividad anterior presionando el botón de regresar del móvil
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    //endregion
}
