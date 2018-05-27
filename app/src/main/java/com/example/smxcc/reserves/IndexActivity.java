package com.example.smxcc.reserves;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IndexActivity extends AppCompatActivity {
    TextView textViewBenvinguda;
    String usuari="";

    LinearLayout linearLayoutPendents;
    LinearLayout linearLayoutCurs;
    LinearLayout linearLayourFinalitzades;

    Button btnPendents;
    Button btnCurs;
    Button btnFinalitzades;

    ScrollView scrollPendents;
    ScrollView scrollCurs;
    ScrollView scrollFinalitzades;


    String url;
    String serveiWeb;

    RequestQueue queue;

    @Override
    protected void onResume(){
        carregarReserves(usuari,linearLayoutPendents,linearLayoutCurs,linearLayourFinalitzades);
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        url = this.getString(R.string.ip);
        serveiWeb = url+"app_reserves.php";

        queue = Volley.newRequestQueue(this);

        textViewBenvinguda = (TextView) findViewById(R.id.textViewBenvinguda);

        //linear layouts
        carregarLayouts();

        //buttons
        btnPendents = (Button) findViewById(R.id.btnPendents);
        btnCurs = (Button) findViewById(R.id.btnCurs);
        btnFinalitzades = (Button) findViewById(R.id.btnFinalitzades);

        //scroll
        scrollPendents = (ScrollView) findViewById(R.id.scrollPendents);
        scrollCurs = (ScrollView) findViewById(R.id.scrollCurs);
        scrollFinalitzades = (ScrollView) findViewById(R.id.scrollFinalitzades);

        //listener buttons
        btnPendents.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(btnPendents.getText().toString().equals("+")){
                    btnPendents.setText("-");
                    scrollPendents.setVisibility(View.VISIBLE);
                }else{
                    btnPendents.setText("+");
                    scrollPendents.setVisibility(View.GONE);
                }
            }
        });

        btnCurs.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(btnCurs.getText().toString().equals("+")){
                    btnCurs.setText("-");
                    scrollCurs.setVisibility(View.VISIBLE);
                }else{
                    btnCurs.setText("+");
                    scrollCurs.setVisibility(View.GONE);
                }
            }
        });

        btnFinalitzades.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(btnFinalitzades.getText().toString().equals("+")){
                    btnFinalitzades.setText("-");
                    scrollFinalitzades.setVisibility(View.VISIBLE);
                }else{
                    btnFinalitzades.setText("+");
                    scrollFinalitzades.setVisibility(View.GONE);
                }
            }
        });

        //missatge de benvinguda agafant l'usuari(pasat per startactivity)
        usuari = getIntent().getExtras().getString("usuari");
        String benvingudaHtml = "PANTALLA D'INICI <span style=\"color: #303F9F\"'><b>"+usuari.toUpperCase()+"</b></span>";
        textViewBenvinguda.setText(Html.fromHtml(benvingudaHtml));
        textViewBenvinguda.setVisibility(View.VISIBLE);
    }



    public void carregarReserves(String usuari,final LinearLayout linearLayoutPendents ,final LinearLayout linearLayoutCurs, final LinearLayout linearLayoutFinalitzades){
        linearLayoutPendents.removeAllViews();
        linearLayoutCurs.removeAllViews();
        linearLayoutFinalitzades.removeAllViews();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                serveiWeb +"?usuari="+usuari,
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
                                Reserva reserva = new Reserva(res.getInt("id"),res.getString("data_inici"),
                                        res.getString("data_fi"),res.getString("ubicacio"),
                                        res.getInt("aprovada"),res.getString("nom"),
                                        res.getString("descripcio"));

                                if(reserva.getAprovada()==0){
                                    linearLayoutAddView(linearLayoutPendents,reserva);
                                }else if(reserva.getAprovada()==1){
                                    linearLayoutAddView(linearLayoutCurs,reserva);
                                }else if(reserva.getAprovada()==2){
                                    linearLayoutAddView(linearLayoutFinalitzades,reserva);
                                }
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
    }

    public void linearLayoutAddView(LinearLayout ll, Reserva reserva){
        final Reserva res = reserva;

        View fila = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.row_reserva,ll,false);

        fila.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                veureReserva(res);
            }
        });

        TextView textViewObjecte = fila.findViewById(R.id.textViewObjecte);
        TextView textViewInici = fila.findViewById(R.id.textViewInici);
        TextView textViewFi = fila.findViewById(R.id.textViewFi);
        TextView textViewId = fila.findViewById(R.id.textViewId);
        TextView textViewIniciH = fila.findViewById(R.id.textViewIniciH);
        TextView textViewFiH = fila.findViewById(R.id.textViewFiH);

        String[] dataInici = new String[2];
        String[] dataFi = new String[2];

        dataInici = DataFormat.formatData(reserva.getInici());
        dataFi = DataFormat.formatData(reserva.getFi());

        //Actualitzar valors a l'interficie
        textViewInici.setText(dataInici[0]);
        textViewFi.setText(dataFi[0]);
        textViewIniciH.setText(dataInici[1]);
        textViewFiH.setText(dataFi[1]);


        textViewObjecte.setText(reserva.getNomObjecte());
        textViewId.setText("Nº "+Integer.toString(reserva.getId()));

        //Afegir fila al layout
        ll.addView(fila);
    }

    public void veureReserva(Reserva r){
        Intent intent = new Intent(this, ReservaActivity.class);
        intent.putExtra("objecte",r.nomObjecte);
        intent.putExtra("inici",r.inici);
        intent.putExtra("fi",r.fi);
        intent.putExtra("id",r.id);
        intent.putExtra("ubicacio",r.ubicacio);
        intent.putExtra("descripcio",r.descObjecte);
        intent.putExtra("aprovada",r.aprovada);
        IndexActivity.this.startActivity(intent);
    }

    public void carregarLayouts(){
        linearLayoutPendents = (LinearLayout) findViewById(R.id.linearLayoutPendents);
        linearLayoutCurs = (LinearLayout) findViewById(R.id.linearLayoutCurs);
        linearLayourFinalitzades = (LinearLayout) findViewById(R.id.linearLayoutFinalitzades);
    }
}
