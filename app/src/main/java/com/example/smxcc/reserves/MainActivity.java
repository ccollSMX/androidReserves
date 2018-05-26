package com.example.smxcc.reserves;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;


public class MainActivity extends AppCompatActivity {
    EditText editTextUsuari;
    EditText editTextContrasenya;
    TextView textViewError;
    Button clickButton;
    String url = getResources().getString(R.string.ip);
    String urlLogin = url+"app_login.php";
    String contrasenya = "";
    String usuari = "";
    RequestQueue queue;
    ConstraintLayout mainLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(this);

        editTextUsuari = (EditText) findViewById(R.id.editTextUsuari);
        editTextContrasenya = (EditText) findViewById(R.id.editTextContrasenya);
        clickButton = (Button) findViewById(R.id.btnLogin);
        textViewError  = (TextView) findViewById(R.id.textViewError);
        mainLayout = (ConstraintLayout) findViewById(R.id.linearLayout);



        clickButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //Agafa la contrasenya del servei web
                    validarLogin(editTextUsuari.getText().toString(),editTextContrasenya.getText().toString());

                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
            }
        });

    }

    public void validarLogin(String usuari, String contrasenya) {
        final String auxUsuari = usuari;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, urlLogin+"?usuari="+auxUsuari+"&contrasenya="+contrasenya, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                            try {
                                if(response.getBoolean("contrasenya")){
                                    //OBRIR ACTIVITY
                                    new CountDownTimer(1500, 1000) {
                                        public void onTick(long millisUntilFinished) {
                                            textViewError.setVisibility(View.VISIBLE);
                                            textViewError.setTextColor(Color.BLACK);
                                            textViewError.setText("Iniciant sessió...");
                                        }

                                        public void onFinish() {
                                            iniciarSessio(auxUsuari);
                                        }
                                    }.start();
                                }else{
                                    //MOSTRAR ERROR
                                    new CountDownTimer(3500, 1000) {
                                        public void onTick(long millisUntilFinished) {
                                            textViewError.setVisibility(View.VISIBLE);
                                            textViewError.setTextColor(Color.parseColor("#ea000f"));
                                            textViewError.setText("Usuari o contrasenya incorrecte!");
                                        }

                                        public void onFinish() {
                                            textViewError.setVisibility(View.GONE);
                                        }
                                    }.start();
                                }
                            } catch (JSONException e) {}
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) { new CountDownTimer(3500, 1000) {

                        public void onTick(long millisUntilFinished) {
                            textViewError.setVisibility(View.VISIBLE);
                            textViewError.setTextColor(Color.parseColor("#ea000f"));
                            textViewError.setText("Problemes amb la connexio BD!");
                        }

                        public void onFinish() {
                            textViewError.setVisibility(View.GONE);
                        }
                    }.start();}
                });
        queue.add(jsonRequest);
    }

    public void iniciarSessio(String usuari){
        Intent intent = new Intent(this, IndexActivity.class);
        intent.putExtra("usuari",usuari);
        MainActivity.this.startActivity(intent);
    }
}



