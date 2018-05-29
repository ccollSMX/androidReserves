package com.example.smxcc.reserves;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.ArrayList;


public class ReservarActivity extends AppCompatActivity {
    ConstraintLayout parent;
    Spinner spinnerObjectes;
    Button buttonObjecte;

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
    boolean data;

    //MEMORIA OBJECTES RECURSOS
    Objecte memObjecte;
    ArrayList<Recurs> memRecursos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservar);

        inicialitzarViews();
        listenersViews();
        carregarDades();
    }

    public void inicialitzarViews(){
        parent = (ConstraintLayout) findViewById(R.id.constraintLayoutReservar);
        fons = (LinearLayout) findViewById(R.id.linearLayoutFons);
        spinnerObjectes = (Spinner) findViewById(R.id.spinnerObjecte);
        buttonObjecte = (Button) findViewById(R.id.buttonObjecte);

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
        editTextInici.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarCalendari(true);
                data = true;
            }
        });

        editTextFi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarCalendari(true);
                data = false;
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                if(data){
                    editTextInici.setText(year+"/"+(month+1)+"/"+day);
                }else{
                    editTextFi.setText(year+"/"+(month+1)+"/"+day);
                }
                mostrarCalendari(false);
                timeView.setVisibility(View.VISIBLE);
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
               }else{
                   memObjecte = (Objecte)parent.getItemAtPosition(position);
                   textViewObjecte.setText(memObjecte.toString());
                   textViewObjecte.setVisibility(View.VISIBLE);
                   buttonObjecte.setVisibility(View.VISIBLE);
                   spinnerObjectes.setVisibility(View.GONE);
               }
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {
                Log.i("hola","no entra mai");
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

    //**********AMAGA O VISUALITZA PARTS DE L'INTERFICIE GRAFICA PER VEURE BE EL CALENDARI********\\
    public void mostrarCalendari(boolean condicio){
        if(condicio){
            calendarView.setVisibility(View.VISIBLE);
            calendarView.setFocusable(true);
            textViewInici.setVisibility(View.INVISIBLE);
            textViewFi.setVisibility(View.INVISIBLE);
            editTextInici.setVisibility(View.INVISIBLE);
            editTextFi.setVisibility(View.INVISIBLE);
        }else {
            calendarView.setVisibility(View.GONE);
            textViewInici.setVisibility(View.VISIBLE);
            textViewFi.setVisibility(View.VISIBLE);
            editTextInici.setVisibility(View.VISIBLE);
            editTextFi.setVisibility(View.VISIBLE);
        }
    }

    public void carregarDades(){
        //carregar dades objecte i recurs
        carregarDadesObjecte();
    }

    public void carregarDadesObjecte(){
        ArrayAdapter<Objecte> spinnerArrayAdapter = new ArrayAdapter<Objecte>(this,R.layout.spinner_item
                ,new Objecte[]{
                new Objecte("Tria l'objecte...","funciona la prova"),
                new Objecte("prova2", "si que funciona si")
        });

        spinnerObjectes.setAdapter(spinnerArrayAdapter);
    }
}
