package com.example.smxcc.reserves;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;


public class ReservarActivity extends AppCompatActivity {
    ConstraintLayout parent;
    Spinner spinnerObjectes;
    Spinner spinnerRecursos;

    Button buttonObjecte;
    Button buttonFerReserva;
    Button buttonInici;
    Button buttonIniciH;
    Button buttonFi;
    Button buttonFiH;

    TextView textViewTitolObjecte;
    TextView textViewObjecte;
    TextView textViewInici;
    TextView textViewFi;

    EditText editTextInici;
    EditText editTextFi;
    EditText editTextIniciH;
    EditText editTextFiH;

    CalendarView calendarView;
    TimePicker timeView;
    LinearLayout fons;
    LinearLayout linearLayoutRecursos;
    boolean data;

    //MEMORIA OBJECTES RECURSOS
    Objecte memObjecte;
    ArrayList<Recurs> memRecursos;

    String url;
    String serveiWeb;
    String usuari ="";

    RequestQueue queue;

    @Override
    protected void onResume(){
        carregarFragmentMap();
        super.onResume();
    }

    public void carregarFragmentMap(){
        FrameLayout div = (FrameLayout) findViewById(R.id.frameLayout);
        SupportMapFragment b = SupportMapFragment.newInstance();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.frameLayout, new MapsActivity()).commit();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservar);
        usuari = getIntent().getExtras().getString("usuari");

        memRecursos = new ArrayList<Recurs>();
        url = this.getString(R.string.ip);
        serveiWeb = url+"app_reserves.php";
        queue = Volley.newRequestQueue(this);

        inicialitzarViews();
        listenersViews();
        carregarDades();

        buttonFerReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comprovarInformacio()){
                    String auxUbicacio = "41.817570 3.067524 18";

                    String inici = DataFormat.formatDataTime(editTextInici.getText().toString(),editTextIniciH.getText().toString());
                    String fi = DataFormat.formatDataTime(editTextFi.getText().toString(),editTextFiH.getText().toString());
                    Reserva r = new Reserva(inici,fi,auxUbicacio);
                    Gson gsonObjecte = new Gson();

                    String jsonObjecte = gsonObjecte.toJson(memObjecte);
                    String jsonRecursos = gsonObjecte.toJson(memRecursos);
                    String jsonReserva = gsonObjecte.toJson(r);
                    Log.i("hola",jsonReserva);
                    Log.i("hola",jsonObjecte);
                    Log.i("hola",usuari);
                    Log.i("hola",jsonRecursos);

                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                            Request.Method.GET,
                            url+"app_reservar.php?reserva="+jsonReserva+"&usuari="+usuari+"&objecte="+jsonObjecte+"&recursos="+jsonRecursos,
                            null,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {

                                }
                            },
                            new Response.ErrorListener(){
                                @Override
                                public void onErrorResponse(VolleyError error){
                                    // Do something when error occurrer
                                    Log.i("hola",error.getMessage());
                                }
                            }
                    );

                    queue.add(jsonArrayRequest);
                }
            }
        });
    }

    public boolean comprovarInformacio(){
        if(editTextInici.getText().length()>0 && editTextIniciH.getText().length()>0 &&
                editTextFi.getText().length()>0 && editTextFiH.getText().length()>0 &&
                textViewObjecte.getVisibility()==View.VISIBLE){
            return true;
        }else{
            return false;
        }
    }

    public void inicialitzarViews(){
        parent = (ConstraintLayout) findViewById(R.id.constraintLayoutReservar);
        fons = (LinearLayout) findViewById(R.id.linearLayoutFons);
        linearLayoutRecursos = (LinearLayout) findViewById(R.id.linearLayoutRecursos);
        spinnerObjectes = (Spinner) findViewById(R.id.spinnerObjecte);
        spinnerRecursos = (Spinner) findViewById(R.id.spinnerRecursos);

        buttonObjecte = (Button) findViewById(R.id.buttonObjecte);
        buttonFerReserva = (Button) findViewById(R.id.buttonFerReserva);
        buttonInici = (Button) findViewById(R.id.buttonInici);
        buttonIniciH = (Button) findViewById(R.id.buttonIniciH);
        buttonFi = (Button) findViewById(R.id.buttonFi);
        buttonFiH = (Button) findViewById(R.id.buttonFiH);

        textViewInici = (TextView) findViewById(R.id.textViewInici);
        textViewFi = (TextView) findViewById(R.id.textViewInici);
        textViewTitolObjecte = (TextView) findViewById(R.id.textViewTitolObjecte);
        textViewObjecte = (TextView) findViewById(R.id.textViewObjecte);

        editTextInici = (EditText) findViewById(R.id.editTextInici);
        editTextFi = (EditText) findViewById(R.id.editTextFi);
        editTextIniciH = (EditText) findViewById(R.id.editTextIniciH);
        editTextFiH = (EditText) findViewById(R.id.editTextFiH);

        calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setFirstDayOfWeek(2);
        calendarView.setVisibility(View.GONE);
        calendarView.setMinDate(System.currentTimeMillis() - 1000);

        timeView = findViewById(R.id.timeView);
        timeView.setVisibility(View.GONE);
        timeView.setIs24HourView(true);
    }

    //**********AGRUPACIÓ DE TOTS ELS LISTENERS***************************************************\\
    public void listenersViews(){
        buttonInici.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarCalendari(true);
                data = true;
            }
        });

        buttonIniciH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeView.setVisibility(View.VISIBLE);
                intervalDateShow(false);
                data = true;
            }
        });

        buttonFi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarCalendari(true);
                data = false;
            }
        });

        buttonFiH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeView.setVisibility(View.VISIBLE);
                intervalDateShow(false);
                data = false;
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                if(data){
                    editTextInici.setText(day+"/"+(month+1)+"/"+year);
                }else{
                    editTextFi.setText(day+"/"+(month+1)+"/"+year);
                }
                mostrarCalendari(false);
            }
        });

        timeView.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Time t = new Time(hourOfDay,minute,00);
                if(data){
                    editTextIniciH.setText(t.toString());
                }else{
                    editTextFiH.setText(t.toString());
                }
            }
        });

        fons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarCalendari(false);
                timeView.setVisibility(View.GONE);
            }
        });

       spinnerObjectes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               if(position == 0){
                   //((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);
               }else{
                   memObjecte = (Objecte)parent.getItemAtPosition(position);
                   textViewObjecte.setText(memObjecte.toString().toUpperCase());
                   textViewObjecte.setVisibility(View.VISIBLE);
                   buttonObjecte.setVisibility(View.VISIBLE);
                   spinnerObjectes.setVisibility(View.GONE);
               }
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });

        spinnerRecursos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    //((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);
                }else{
                    final View fila = LayoutInflater.from(getApplicationContext())
                            .inflate(R.layout.row_recurs_reserva,linearLayoutRecursos,false);
                    TextView textViewRecursReserva = (TextView) fila.findViewById(R.id.textViewRecursReserva);
                    TextView textViewDescripcioReserva = (TextView) fila.findViewById(R.id.textViewDescripcioReserva);
                    Button buttonRecrusos = (Button) fila.findViewById(R.id.buttonRecursos);
                    final Recurs recAux = (Recurs) parent.getItemAtPosition(position);
                    memRecursos.add(recAux);

                    textViewRecursReserva.setText(recAux.getNom());
                    textViewDescripcioReserva.setText((recAux.getDescripcio()));


                    buttonRecrusos.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            memRecursos.remove(recAux);
                            linearLayoutRecursos.removeView(fila);
                        }
                    });

                    linearLayoutRecursos.addView(fila);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        buttonObjecte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                memObjecte = null;
                textViewObjecte.setVisibility(View.GONE);
                buttonObjecte.setVisibility(View.GONE);
                spinnerObjectes.setVisibility(View.VISIBLE);
                spinnerObjectes.setSelection(0);
            }
        });
    }
    //**********FINAL AGRUPACIÓ DE TOTS ELS LISTENERS*********************************************\\

    //**********AMAGA O VISUALITZA PARTS DE L'INTERFICIE GRAFICA PER VEURE BE EL CALENDARI********\\
    public void mostrarCalendari(boolean condicio){
        if(condicio){
            calendarView.setVisibility(View.VISIBLE);
            calendarView.setFocusable(true);
            intervalDateShow(false);
        }else {
            calendarView.setVisibility(View.GONE);
            intervalDateShow(true);
        }
    }

    public void intervalDateShow(boolean show){
        if(show){
            textViewInici.setVisibility(View.VISIBLE);
            textViewFi.setVisibility(View.VISIBLE);
            editTextInici.setVisibility(View.VISIBLE);
            editTextFi.setVisibility(View.VISIBLE);
            buttonInici.setVisibility(View.VISIBLE);
            buttonIniciH.setVisibility(View.VISIBLE);
            buttonFi.setVisibility(View.VISIBLE);
            buttonFiH.setVisibility(View.VISIBLE);
        }else{
            textViewInici.setVisibility(View.INVISIBLE);
            textViewFi.setVisibility(View.INVISIBLE);
            editTextInici.setVisibility(View.INVISIBLE);
            editTextFi.setVisibility(View.INVISIBLE);
            buttonInici.setVisibility(View.INVISIBLE);
            buttonIniciH.setVisibility(View.INVISIBLE);
            buttonFi.setVisibility(View.INVISIBLE);
            buttonFiH.setVisibility(View.INVISIBLE);
        }
    }
    public void carregarDades(){
        //carregar dades objecte i recurs
        carregarDadesObjecte();
        carregarDadesRecursos();
    }

    public void carregarDadesObjecte(){
        final ArrayAdapter<Objecte> spinnerArrayAdapter = new ArrayAdapter<Objecte>(this,R.layout.spinner_item);
        spinnerArrayAdapter.add(new Objecte("Tria l'objecte...","funciona la prova"));


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                serveiWeb +"?objectes",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Process the JSON
                        try{
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject res = response.getJSONObject(i);
                                Objecte objecte = new Objecte(res.getInt("id"),res.getString("nom"),
                                        res.getString("descripcio"));
                                spinnerArrayAdapter.add(objecte);
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurrer

                    }
                }
        );

        // Add JsonArrayRequest to the RequestQueue
        queue.add(jsonArrayRequest);
        spinnerObjectes.setAdapter(spinnerArrayAdapter);
    }

    public void carregarDadesRecursos(){
        final ArrayAdapter<Recurs> spinnerArrayAdapterRec = new ArrayAdapter<Recurs>(this,R.layout.spinner_item);
        spinnerArrayAdapterRec.add(new Recurs("Tria el recurs..."));

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                serveiWeb +"?recursos",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Process the JSON
                        try{
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject res = response.getJSONObject(i);
                                Recurs rec = new Recurs(res.getInt("id"),res.getString("nom"),
                                        res.getString("descripcio"),res.getInt("stock"));
                                spinnerArrayAdapterRec.add(rec);
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurrer

                    }
                }
        );

        // Add JsonArrayRequest to the RequestQueue
        spinnerRecursos.setAdapter(spinnerArrayAdapterRec);
        queue.add(jsonArrayRequest);

    }
}
