package com.votosapp.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.votosapp.Controllers.UserController;
import com.votosapp.CustomClass.HintSpinnerAdapter;
import com.votosapp.Handler.HttpHandler;
import com.votosapp.Models.City;
import com.votosapp.Models.Department;
import com.votosapp.Models.User;
import com.votosapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.view.View.OnClickListener;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, OnClickListener {

    //region Info Mapa
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    //endregion Mapa

    //region Variables
    Marker marker;
    List<Marker> markers = new ArrayList<>();
    ArrayList<LatLng> positions = new ArrayList<>();
    ArrayList<List<String>> datos = new ArrayList<List<String>>();
    ArrayList<String> datosList = new ArrayList<String>();
    ArrayList<User> lista_user = new ArrayList<>();
    private int User_Id;
    private String Name_User_Type;
    private String FirstName;
    private String LastName;
    private String Ciudad;
    private String Cedula;
    private String Telefono;
    private String Direccion;
    private String Latitud;
    private String Longitud;
    private int lengthJson;
    private int departamento_id;
    private String Name_Sector;
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
    int Id_Departamento;
    int Id_City;
    int Id_Zone;
    long Id_Departamento_Local;
    DepartamentoController db_departamentos;
    //Instancia del controlador de municipios
    MunicipioController db_municipios;

    UserController db_usuarios;

    private ProgressDialog pDialog;
    public String departamentoSeleccionado, ciudadSeleccionada, corregimientoSeleccionado;
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

    //Botón de Busqueda;
    private Button btnBusqueda;

    //ScrollView
    private ScrollView scrollView;

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

        //Conexión al controlador de usuarios
        db_usuarios = new UserController(this);

        //Obtención de datos que provienen del LoginActivity
        User_Id = getIntent().getExtras().getInt("user_id");
        Name_User_Type = getIntent().getExtras().getString("name_user_type");
        FirstName = getIntent().getExtras().getString("firstname");
        LastName = getIntent().getExtras().getString("lastname");
        toolbar.setTitle("Bienvenido, " + FirstName + " " + LastName);

        //Botón Búsqueda
        btnBusqueda = (Button) findViewById(R.id.btnBuscar);
        btnBusqueda.setOnClickListener(this);
        //ScrollView
        scrollView = (ScrollView) findViewById(R.id.scrollView);
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
        itemsSpinnerCorregimiento = new ArrayList<String>();
        itemsSpinnerBusqueda = new ArrayList<String>();


        if (Name_User_Type.equals("Candidato")) {
            itemsSpinnerBusqueda.add(0, "Consultar líderes");
            itemsSpinnerBusqueda.add(1, "Consultar usuarios por Líder");
            itemsSpinnerBusqueda.add(2, "Consultar líderes por Departamento");
            itemsSpinnerBusqueda.add(3, "Consultar líderes por Municipio");
            itemsSpinnerBusqueda.add(4, "Consultar líderes por Corregimiento");
            itemsSpinnerBusqueda.add(5, "Consultar líderes por Comuna");
            itemsSpinnerBusqueda.add(6, "Consultar líderes por Vereda");
            itemsSpinnerBusqueda.add(7, "Consultar líderes por Barrio");

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

    //region OnClick Botón de Búsqueda
    @Override
    public void onClick(View v) {
        if (Name_User_Type.equals("Candidato")) {
            if (posBusqueda == 1) {
                cargaReferentes();
            } else if (posBusqueda == 3) {
                cargaUsuariosByDepartamento();
            } else if (posBusqueda == 4) {
                cargaUsuariosByMunicipio();
            } else if (posBusqueda == 5) {
                cargaUsuariosByCorregimiento();
            }
        } else if (Name_User_Type.equals("Lider")) {
            if (posBusqueda == 1) {
                cargaReferentes();
            } else if (posBusqueda == 2) {
                cargaUsuariosByDepartamento();
            } else if (posBusqueda == 3) {
                cargaUsuariosByMunicipio();
            } else if (posBusqueda == 4) {
                cargaUsuariosByCorregimiento();
            }
        }
    }

    //endregion

    //region Métodos de carga Consultas

    public void cargaReferentes() {
        if (markers.size() > 0) {
            for (Marker m : markers) {
                m.remove();
            }
        }
        positions.clear();
        if (isOnlineNet()) {
            new GetReferentes().execute();
        } else {
            db_usuarios.abrirBaseDeDatos();
            String id = String.valueOf(User_Id);
            ArrayList<User> list_usuarios = db_usuarios.GetUserReferenteByIdRemote(id);
            db_usuarios.cerrar();

            if (list_usuarios.size() > 0) {
                for (User u : list_usuarios) {
                    String firstName = u.getFirstName();
                    String lastname = u.getLastName();
                    String ciudad = u.getName_Municipe();
                    String cedula = u.getIdentification_Card();
                    String telefono = u.getPhone1();
                    String direccion = u.getAddress();
                    String latitud = u.getCoords_Location();
                    String longitud = u.getPicture();

                    positions.add(new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud)));
                    User user = new User();
                    user.setFirstName(firstName);
                    user.setLastName(lastname);
                    user.setCoords_Location(ciudad);
                    user.setIdentification_Card(cedula);
                    user.setPhone1(telefono);
                    user.setAddress(direccion);

                    lista_user.add(user);
                }

                int i = 0;
                for (LatLng position : positions) {
                    String firstname = lista_user.get(i).getFirstName();
                    String lastname = lista_user.get(i).getLastName();
                    String ciudad = lista_user.get(i).getCoords_Location();
                    String cedula = lista_user.get(i).getIdentification_Card();
                    String telefono = lista_user.get(i).getPhone1();
                    String direccion = lista_user.get(i).getAddress();
                    i = i + 1;

                    marker = mMap.addMarker(
                            new MarkerOptions()
                                    .position(position)
                                    .title(firstname + " " + lastname)
                                    .snippet("Ciudad: " + ciudad + "\n" + "Cédula: " + cedula + "\n" +
                                            "Teléfono: " + telefono + "\n" + "Dirección: " + direccion)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    );
                    markers.add(marker);


                }

                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(2.9, -75)));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(5));

            } else {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setTitle("Alerta");
                builder.setMessage("No hay usuarios registrados en el dispositivo");
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

    public void cargaUsuariosByDepartamento() {
        if (markers.size() > 0) {
            for (Marker m : markers) {
                m.remove();
            }
        }
        positions.clear();
        if (isOnlineNet()) {
            new GetUsersByDepartamentoId().execute();
        } else {
            db_usuarios.abrirBaseDeDatos();
            String id = String.valueOf(User_Id);
            int id_department = db_departamentos.GetIdDepartamentoByName(departamentoSeleccionado);
            ArrayList<User> list_usuarios = db_usuarios.GetUserReferenteByIdRemoteAndDepartamentoId(id, id_department);
            db_usuarios.cerrar();

            if (list_usuarios.size() > 0) {
                for (User u : list_usuarios) {
                    String firstName = u.getFirstName();
                    String lastname = u.getLastName();
                    String ciudad = u.getName_Municipe();
                    String cedula = u.getIdentification_Card();
                    String telefono = u.getPhone1();
                    String direccion = u.getAddress();
                    String latitud = u.getCoords_Location();
                    String longitud = u.getPicture();

                    positions.add(new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud)));
                    User user = new User();
                    user.setFirstName(firstName);
                    user.setLastName(lastname);
                    user.setCoords_Location(ciudad);
                    user.setIdentification_Card(cedula);
                    user.setPhone1(telefono);
                    user.setAddress(direccion);

                    lista_user.add(user);
                }

                int i = 0;
                for (LatLng position : positions) {
                    String firstname = lista_user.get(i).getFirstName();
                    String lastname = lista_user.get(i).getLastName();
                    String ciudad = lista_user.get(i).getCoords_Location();
                    String cedula = lista_user.get(i).getIdentification_Card();
                    String telefono = lista_user.get(i).getPhone1();
                    String direccion = lista_user.get(i).getAddress();
                    i = i + 1;

                    marker = mMap.addMarker(
                            new MarkerOptions()
                                    .position(position)
                                    .title(firstname + " " + lastname)
                                    .snippet("Ciudad: " + ciudad + "\n" + "Cédula: " + cedula + "\n" +
                                            "Teléfono: " + telefono + "\n" + "Dirección: " + direccion)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    );
                    markers.add(marker);


                }

                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(2.9, -75)));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(5));

            } else {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setTitle("Alerta");
                builder.setMessage("No hay usuarios para este departamento registrados en el dispositivo");
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

    public void cargaUsuariosByMunicipio() {
        if (markers.size() > 0) {
            for (Marker m : markers) {
                m.remove();
            }
        }
        positions.clear();
        if (isOnlineNet()) {
            new GetUsersByMunicipioId().execute();
        }
    }

    public void cargaUsuariosByCorregimiento() {
        if (markers.size() > 0) {
            for (Marker m : markers) {
                m.remove();
            }
        }
        positions.clear();
        if (isOnlineNet()) {
            //TODO
            //new GetUsersByCorregimiento().execute();
        }
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
            itemsSpinnerDepartamento.clear();
            new GetAllDepartamentosApp().execute();
            //new GetDepartamentos().execute();
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
                        if(Name_User_Type.equals("Candidato")) {
                            if ((posBusqueda == 4 || posBusqueda == 5 || posBusqueda == 6 || posBusqueda == 7 || posBusqueda == 8)
                                    && position != 0) {
                                itemsSpinnerMunicipio.clear();
                                cargaSpinnerMunicipio();
                            }
                        }
                        if(Name_User_Type.equals("Lider")) {
                            if ((posBusqueda == 3 || posBusqueda == 4 || posBusqueda == 5 || posBusqueda == 6 || posBusqueda == 7)
                                    && position != 0) {
                                itemsSpinnerMunicipio.clear();
                                cargaSpinnerMunicipio();
                            }
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
        if (isOnlineNet()) {
            new GetCorregimientosByCityApp().execute();

        }
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

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(getApplicationContext());
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(getApplicationContext());
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(getApplicationContext());
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
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


    //region TAREAS ASÍNCRONAS DE MARCADORES

    //Tarea asíncrona para obtener los referentes

    private class GetReferentes extends AsyncTask<Void, Void, Void> {

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

            String urlRef = "http://gestionusuariospolit.azurewebsites.net/api/users/GetUserReferentes/" + User_Id;
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(urlRef);

            if (jsonStr != null) {
                try {
                    JSONArray jsonArray = new JSONArray(jsonStr);
                    lengthJson = jsonArray.length();

                    db_usuarios.abrirBaseDeDatos();
                    String id = String.valueOf(User_Id);
                    ArrayList<User> list_usuarios = db_usuarios.GetUserReferenteByIdRemote(id);
                    db_usuarios.cerrar();

                    if (list_usuarios.size() == jsonArray.length()) {
                        for (User u : list_usuarios) {
                            String firstName = u.getFirstName();
                            String lastname = u.getLastName();
                            String ciudad = u.getName_Municipe();
                            String cedula = u.getIdentification_Card();
                            String telefono = u.getPhone1();
                            String direccion = u.getAddress();
                            String latitud = u.getCoords_Location();
                            String longitud = u.getPicture();

                            positions.add(new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud)));
                            User user = new User();
                            user.setFirstName(firstName);
                            user.setLastName(lastname);
                            user.setCoords_Location(ciudad);
                            user.setIdentification_Card(cedula);
                            user.setPhone1(telefono);
                            user.setAddress(direccion);

                            lista_user.add(user);
                        }
                    } else {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            User user = new User();
                            JSONObject r = jsonArray.getJSONObject(i);

                            int User_Id = r.getInt("User_Id");
                            long User_Type_Id = r.getLong("User_Type_Id");
                            String Name_User_Type = r.getString("Name_User_Type");
                            String Referente_Id = r.getString("Referente_Id");
                            String Name_Referente = r.getString("Name_Referente");
                            int Sector_Id = r.getInt("Sector_Id");
                            String Name_Municipe = r.getString("Name_Municipe");
                            String firstname = r.getString("FirstName");
                            String lastname = r.getString("LastName");
                            String Identification_Card = r.getString("Identification_Card");
                            String Profession = r.getString("Profession");
                            String BirthDate = r.getString("Birth_Date");
                            String Phone2 = r.getString("Phone2");
                            String Email = r.getString("Email");
                            String Coords_Location = r.getString("Coords_Location");
                            String Have_Vehicle = r.getString("Have_Vehicle");
                            String Vehicle_Type = r.getString("Vehicle_Type");
                            String Vehicle_Plate = r.getString("Vehicle_Plate");
                            String Password = r.getString("Password");
                            String Picture = r.getString("Picture");
                            String Is_Leader = r.getString("Is_Leader");
                            String latitud = r.getString("Latitude_Sector");
                            String longitud = r.getString("Longitude_Sector");
                            String ciudad = r.getString("Name_Ciudad");
                            String cedula = r.getString("Identification_Card");
                            String telefono = r.getString("Phone1");
                            String direccion = r.getString("Address");
                            int department_id = r.getInt("Department_Id");
                            positions.add(new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud)));

                            db_usuarios.abrirBaseDeDatos();
                            ArrayList<User> list_users = db_usuarios.GetUserByIdRemote(User_Id);
                            db_usuarios.cerrar();

                            if (list_users.size() == 0) {

                                //TODO En COORDS LOCATION ESTOY GUARDANDO LATITUD y EN PICTURE LONGITUD, HAY QUE AÑADIR ESOS CAMPOS
                                db_usuarios.abrirBaseDeDatos();
                                db_usuarios.InsertUser(User_Id, User_Type_Id, Name_User_Type, Referente_Id, Name_Referente, Sector_Id,
                                        Name_Municipe, firstname, lastname, Identification_Card, Profession, BirthDate, telefono, Phone2,
                                        Email, direccion, latitud, Have_Vehicle, Vehicle_Type, Vehicle_Plate, Password, longitud,
                                        Is_Leader, department_id);
                                db_usuarios.cerrar();
                            }

                            user.setFirstName(firstname);
                            user.setLastName(lastname);
                            user.setCoords_Location(ciudad);
                            user.setIdentification_Card(cedula);
                            user.setPhone1(telefono);
                            user.setAddress(direccion);

                            lista_user.add(user);

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

            if (positions.size() == 0) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setTitle("Alerta");
                builder.setMessage("No hay usuarios registrados");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {

                int i = 0;
                for (LatLng position : positions) {
                    String firstname = lista_user.get(i).getFirstName();
                    String lastname = lista_user.get(i).getLastName();
                    String ciudad = lista_user.get(i).getCoords_Location();
                    String cedula = lista_user.get(i).getIdentification_Card();
                    String telefono = lista_user.get(i).getPhone1();
                    String direccion = lista_user.get(i).getAddress();
                    i = i + 1;

                    marker = mMap.addMarker(
                            new MarkerOptions()
                                    .position(position)
                                    .title(firstname + " " + lastname)
                                    .snippet("Ciudad: " + ciudad + "\n" + "Cédula: " + cedula + "\n" +
                                            "Teléfono: " + telefono + "\n" + "Dirección: " + direccion)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    );
                    markers.add(marker);


                }

                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(2.9, -75)));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(5));

            }

        }

    }


    private class GetUsersByDepartamentoId extends AsyncTask<Void, Void, Void> {

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

            String urlUsersByDepartamento = "http://gestionusuariospolit.azurewebsites.net/api/users/GetUserByDepartments/" + User_Id + "/" + Id_Departamento;
            String jsonStr = sh.makeServiceCall(urlUsersByDepartamento);

            if (jsonStr != null) {
                try {

                    JSONArray jsonArray = new JSONArray(jsonStr);

                    db_usuarios.abrirBaseDeDatos();
                    String id = String.valueOf(User_Id);
                    int id_departamento = Id_Departamento;
                    ArrayList<User> list_usuarios = db_usuarios.GetUserReferenteByIdRemoteAndDepartamentoId(id, id_departamento);
                    db_usuarios.cerrar();

                    if (list_usuarios.size() == jsonArray.length()) {
                        for (User u : list_usuarios) {
                            String firstName = u.getFirstName();
                            String lastname = u.getLastName();
                            String ciudad = u.getName_Municipe();
                            String cedula = u.getIdentification_Card();
                            String telefono = u.getPhone1();
                            String direccion = u.getAddress();
                            String latitud = u.getCoords_Location();
                            String longitud = u.getPicture();

                            positions.add(new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud)));
                            User user = new User();
                            user.setFirstName(firstName);
                            user.setLastName(lastname);
                            user.setCoords_Location(ciudad);
                            user.setIdentification_Card(cedula);
                            user.setPhone1(telefono);
                            user.setAddress(direccion);

                            lista_user.add(user);
                        }
                    } else {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            User user = new User();
                            JSONObject r = jsonArray.getJSONObject(i);

                            int User_Id = r.getInt("User_Id");
                            long User_Type_Id = r.getLong("User_Type_Id");
                            String Name_User_Type = r.getString("Name_User_Type");
                            String Referente_Id = r.getString("Referente_Id");
                            String Name_Referente = r.getString("Name_Referente");
                            int Sector_Id = r.getInt("Sector_Id");
                            String Name_Municipe = r.getString("Name_Municipe");
                            String firstname = r.getString("FirstName");
                            String lastname = r.getString("LastName");
                            String Identification_Card = r.getString("Identification_Card");
                            String Profession = r.getString("Profession");
                            String BirthDate = r.getString("Birth_Date");
                            String Phone2 = r.getString("Phone2");
                            String Email = r.getString("Email");
                            String Coords_Location = r.getString("Coords_Location");
                            String Have_Vehicle = r.getString("Have_Vehicle");
                            String Vehicle_Type = r.getString("Vehicle_Type");
                            String Vehicle_Plate = r.getString("Vehicle_Plate");
                            String Password = r.getString("Password");
                            String Picture = r.getString("Picture");
                            String Is_Leader = r.getString("Is_Leader");
                            String latitud = r.getString("Latitude_Sector");
                            String longitud = r.getString("Longitude_Sector");
                            String ciudad = r.getString("Name_Ciudad");
                            String cedula = r.getString("Identification_Card");
                            String telefono = r.getString("Phone1");
                            String direccion = r.getString("Address");
                            int department_id = r.getInt("Department_Id");
                            positions.add(new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud)));

                            db_usuarios.abrirBaseDeDatos();
                            ArrayList<User> list_users = db_usuarios.GetUserByIdRemote(User_Id);
                            db_usuarios.cerrar();

                            if (list_users.size() == 0) {

                                //TODO En COORDS LOCATION ESTOY GUARDANDO LATITUD y EN PICTURE LONGITUD, HAY QUE AÑADIR ESOS CAMPOS
                                db_usuarios.abrirBaseDeDatos();
                                db_usuarios.InsertUser(User_Id, User_Type_Id, Name_User_Type, Referente_Id, Name_Referente, Sector_Id,
                                        Name_Municipe, firstname, lastname, Identification_Card, Profession, BirthDate, telefono, Phone2,
                                        Email, direccion, latitud, Have_Vehicle, Vehicle_Type, Vehicle_Plate, Password, longitud,
                                        Is_Leader, department_id);
                                db_usuarios.cerrar();
                            }

                            user.setFirstName(firstname);
                            user.setLastName(lastname);
                            user.setCoords_Location(ciudad);
                            user.setIdentification_Card(cedula);
                            user.setPhone1(telefono);
                            user.setAddress(direccion);

                            lista_user.add(user);

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

            if (positions.size() == 0) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setTitle("Alerta");
                builder.setMessage("No hay usuarios registrados para este departamento");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {

                int i = 0;
                for (LatLng position : positions) {
                    String firstname = lista_user.get(i).getFirstName();
                    String lastname = lista_user.get(i).getLastName();
                    String ciudad = lista_user.get(i).getCoords_Location();
                    String cedula = lista_user.get(i).getIdentification_Card();
                    String telefono = lista_user.get(i).getPhone1();
                    String direccion = lista_user.get(i).getAddress();
                    i = i + 1;

                    marker = mMap.addMarker(
                            new MarkerOptions()
                                    .position(position)
                                    .title(firstname + " " + lastname)
                                    .snippet("Ciudad: " + ciudad + "\n" + "Cédula: " + cedula + "\n" +
                                            "Teléfono: " + telefono + "\n" + "Dirección: " + direccion)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    );
                    markers.add(marker);


                }

                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(2.9, -75)));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(5));
            }

        }


    }


    private class GetUsersByMunicipioId extends AsyncTask<Void, Void, Void> {

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

            String urlUsersByMunicipio = "http://gestionusuariospolit.azurewebsites.net/api/users/GetUserByCities/" + User_Id + "/" + Id_City;
            String jsonStr = sh.makeServiceCall(urlUsersByMunicipio);

            if (jsonStr != null) {
                try {

                    JSONArray jsonArray = new JSONArray(jsonStr);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        User user = new User();
                        FirstName = jsonObject.getString("FirstName");
                        LastName = jsonObject.getString("LastName");
                        Latitud = jsonObject.getString("Latitude_Sector");
                        Longitud = jsonObject.getString("Longitude_Sector");
                        Ciudad = jsonObject.getString("Name_Ciudad");
                        Cedula = jsonObject.getString("Identification_Card");
                        Telefono = jsonObject.getString("Phone1");
                        Direccion = jsonObject.getString("Address");
                        positions.add(new LatLng(Double.parseDouble(Latitud), Double.parseDouble(Longitud)));

                        user.setFirstName(FirstName);
                        user.setLastName(LastName);
                        user.setCoords_Location(Ciudad);
                        user.setIdentification_Card(Cedula);
                        user.setPhone1(Telefono);
                        user.setAddress(Direccion);

                        lista_user.add(user);
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

            int i = 0;
            for (LatLng position : positions) {
                String firstname = lista_user.get(i).getFirstName();
                String lastname = lista_user.get(i).getLastName();
                String ciudad = lista_user.get(i).getCoords_Location();
                String cedula = lista_user.get(i).getIdentification_Card();
                String telefono = lista_user.get(i).getPhone1();
                String direccion = lista_user.get(i).getAddress();
                i = i + 1;

                marker = mMap.addMarker(
                        new MarkerOptions()
                                .position(position)
                                .title(firstname + " " + lastname)
                                .snippet("Ciudad: " + ciudad + "\n" + "Cédula: " + cedula + "\n" +
                                        "Teléfono: " + telefono + "\n" + "Dirección: " + direccion)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                );
                markers.add(marker);


            }

            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(2.9, -75)));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(5));

        }


    }

/*
    private class GetUsersByCorregimiento extends AsyncTask<Void, Void, Void> {

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

            //TODO
            //String urlUsersByCorregimiento = "http://gestionusuariospolit.azurewebsites.net/api/users/GetUserByZones/" + User_Id + "/" + ;
            //String jsonStr = sh.makeServiceCall(urlUsersByCorregimiento);

        }





    }
*/


    //endregion


    //region TAREAS ASÍNCRONAS PARA DEPARTAMENTOS Y MUNICIPIOS

    private class GetAllDepartamentosApp extends AsyncTask<Void, Void, Void> {

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

            String urlDepartamentos = "http://gestionusuariospolit.azurewebsites.net/api/Departments/DepartmentsAll";
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
                    }
                    if(Name_User_Type.equals("Candidato")) {
                        if ((posBusqueda == 4 || posBusqueda == 5 || posBusqueda == 6 || posBusqueda == 7 || posBusqueda == 8)
                                && position != 0) {
                            itemsSpinnerMunicipio.clear();
                            cargaSpinnerMunicipio();
                        }
                    }
                    if(Name_User_Type.equals("Lider")) {
                        if ((posBusqueda == 3 || posBusqueda == 4 || posBusqueda == 5 || posBusqueda == 6 || posBusqueda == 7)
                                && position != 0) {
                            itemsSpinnerMunicipio.clear();
                            cargaSpinnerMunicipio();
                        }
                    }
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
            pDialog = new ProgressDialog(MapsActivity.this);
            pDialog.setMessage("Cargando...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            HttpHandler sh = new HttpHandler();

            String urlMunicipio = "http://gestionusuariospolit.azurewebsites.net/api/Departments/DepartmentsAll";
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
                        if (posBusqueda == 5) {
                            cargaSpinnerCorregimiento();
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        }


    }


    private class GetCorregimientosByCityApp extends AsyncTask<Void, Void, Void> {

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

            String urlZone = "http://gestionusuariospolit.azurewebsites.net/api/Departments/GetZonesByCities/" + Id_City;
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(urlZone);

            if (jsonStr != null) {
                try {

                    JSONArray jsonArray = new JSONArray(jsonStr);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        int id_zone = c.getInt("Zone_Id");
                        String Name = c.getString("Name");
                        JSONObject object = c.getJSONObject("Sector_Type");
                        Name_Sector = object.getString("Name");

                        if (Name_Sector.equals("Corregimiento")) {
                            itemsSpinnerCorregimiento.add(Name);
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

            if (itemsSpinnerCorregimiento.size() == 0) {
                Toast.makeText(getApplicationContext(), "No hay Corregimientos registrados en este municipio", Toast.LENGTH_SHORT).show();
            }
            if (Name_Sector.equals("Corregimiento")) {

                spinnerCorregimiento.setVisibility(View.VISIBLE);
                adapterSpinnerCorregimiento = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, itemsSpinnerCorregimiento);
                spinnerCorregimiento.setAdapter(new HintSpinnerAdapter(adapterSpinnerCorregimiento, R.layout.hint_row_item_corregimiento, getApplicationContext()));

                spinnerCorregimiento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //Obtiene el dato del municipio seleccionado
                        int positionC = position - 1;
                        if (positionC != -1) {
                            corregimientoSeleccionado = itemsSpinnerCorregimiento.get(positionC);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }

        }


    }


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
                                // db_departamentos.InsertDepartamento(departamento);
                                db_departamentos.cerrar();

                                db_departamentos.abrirBaseDeDatos();
                                Id_Departamento_Local = db_departamentos.GetIdDepartamentoByName(departamento);
                                db_departamentos.cerrar();

                                ArrayList<String> list_municipios = new ArrayList<String>();
                                JSONArray jArray = c.getJSONArray("ciudades");
                                for (int j = 0; j < jArray.length(); j++) {
                                    list_municipios.add(jArray.getString(j));
                                    db_municipios.abrirBaseDeDatos();
                                    //db_municipios.InsertMunicipio(Id_Departamento_Local, jArray.getString(j));
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
