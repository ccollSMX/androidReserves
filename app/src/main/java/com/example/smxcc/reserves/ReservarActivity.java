package com.example.smxcc.reserves;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;


public class ReservarActivity extends AppCompatActivity {
    ConstraintLayout parent;
    TextView textViewTitolObjecte;
    TextView textViewInici;
    TextView textViewFi;
    EditText editTextInici;
    EditText editTextFi;
    CalendarView calendarView;
    boolean data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservar);

        inicialitzarViews();
        listenersViews();
    }

    public void inicialitzarViews(){
        parent = (ConstraintLayout) findViewById(R.id.constraintLayoutReservar);

        textViewInici = (TextView) findViewById(R.id.textViewInici);
        textViewFi = (TextView) findViewById(R.id.textViewFi);
        textViewTitolObjecte = (TextView) findViewById(R.id.textViewTitolObjecte);

        editTextInici = (EditText) findViewById(R.id.editTextInici);
        editTextFi = (EditText) findViewById(R.id.editTextFi);

        calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setFirstDayOfWeek(2);
        calendarView.setVisibility(View.GONE);
        calendarView.setMinDate(System.currentTimeMillis() - 1000);
    }

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
            }
        });
    }

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
}
