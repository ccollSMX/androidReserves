package com.example.smxcc.reserves;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReservaActivity extends AppCompatActivity {
    RequestQueue queue;

    String url;
    String serveiWeb;
    TextView textViewObjecte;
    TextView textViewDataInici;
    TextView textViewDataFi;
    TextView textViewHoraInici;
    TextView textViewHoraFi;
    TextView textViewReserva;
    LinearLayout linearLayourRecursos;
    Reserva reserva;

    @Override
    protected void onResume(){
        carregarRecursos(reserva.getId());
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva);

        url = this.getString(R.string.ip);
        serveiWeb = url+"app_reserves.php";

        queue = Volley.newRequestQueue(this);

        reserva = new Reserva(getIntent().getExtras().getInt("id"),getIntent().getExtras().getString("inici")
                ,getIntent().getExtras().getString("fi"),getIntent().getExtras().getString("ubicacio")
                ,getIntent().getExtras().getInt("aprovada"),getIntent().getExtras().getString("objecte")
                ,getIntent().getExtras().getString("descripcio"));

        carregarViews(reserva);

        String benvingudaHtml = "RESERVA Nº <span style=\"color: #303F9F\"'><b>"+reserva.getId()+"</b></span>";
        textViewReserva.setText(Html.fromHtml(benvingudaHtml));
    }

    public void carregarViews(Reserva r){
        textViewObjecte = (TextView) findViewById(R.id.textViewObjecte);
        textViewDataInici = (TextView) findViewById(R.id.textViewDataInici);
        textViewHoraInici = (TextView) findViewById(R.id.textViewHoraInici);
        textViewDataFi = (TextView) findViewById(R.id.textViewDataFi);
        textViewHoraFi = (TextView) findViewById(R.id.textViewHoraFi);
        textViewReserva = (TextView) findViewById(R.id.textViewReserva);
        linearLayourRecursos = (LinearLayout) findViewById(R.id.linearLayoutRecursos);

        String[] dataInici = new String[2];
        String[] dataFi = new String[2];

        dataInici = formatData(r.getInici());
        dataFi = formatData(r.getFi());

        textViewObjecte.setText(r.getNomObjecte());
        textViewDataInici.setText(dataInici[0]);
        textViewHoraInici.setText(dataInici[1]);
        textViewDataFi.setText(dataFi[0]);
        textViewHoraFi.setText(dataFi[1]);
    }

    public void carregarRecursos(Integer id){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                serveiWeb +"?reserva="+Integer.toString(id),
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
                                res.getString("nom");
                                res.getString("descripcio");
                                res.getInt("stock");

                                Reserva reserva = new Reserva(res.getInt("id"),res.getString("data_inici"),
                                        res.getString("data_fi"),res.getString("ubicacio"),
                                        res.getInt("aprovada"),res.getString("nom"),
                                        res.getString("descripcio"));

                                linearLayoutAddView(linearLayourRecursos);

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

    public void linearLayoutAddView(LinearLayout ll){
        View fila = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.row_recurs,ll,false);

        ll.addView(fila);
    }

    public String[] formatData(String data){
        String[] aux = new String[2];

        SimpleDateFormat readingFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormatData = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat outputFormatHora = new SimpleDateFormat("HH:mm");
        Date d = null;

        try {
            d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data);
        } catch (ParseException e) {e.printStackTrace();}

        aux[0] = outputFormatData.format(d);
        aux[1] = outputFormatHora.format(d);
        return aux;
    }
}
