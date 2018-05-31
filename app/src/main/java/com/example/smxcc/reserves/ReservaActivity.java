package com.example.smxcc.reserves;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.SupportMapFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


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
        textViewObjecte = (TextView) findViewById(R.id.textViewTitolObjecte);
        textViewDataInici = (TextView) findViewById(R.id.textViewDataInici);
        textViewHoraInici = (TextView) findViewById(R.id.textViewHoraInici);
        textViewDataFi = (TextView) findViewById(R.id.textViewDataFi);
        textViewHoraFi = (TextView) findViewById(R.id.textViewHoraFi);
        textViewReserva = (TextView) findViewById(R.id.textViewReserva);
        linearLayourRecursos = (LinearLayout) findViewById(R.id.linearLayoutRecursos);

        String[] dataInici = new String[2];
        String[] dataFi = new String[2];

        dataInici = DataFormat.formatData(r.getInici());
        dataFi = DataFormat.formatData(r.getFi());

        textViewObjecte.setText(r.getNomObjecte());
        textViewDataInici.setText(dataInici[0]);
        textViewHoraInici.setText(dataInici[1]);
        textViewDataFi.setText(dataFi[0]);
        textViewHoraFi.setText(dataFi[1]);
    }

    @Override
    protected void onResume(){
        carregarRecursos(reserva.getId());
        carregarFragmentMap(reserva.getUbicacio());
        super.onResume();
    }

    public void carregarFragmentMap(String ubicacio){
        FrameLayout div = (FrameLayout) findViewById(R.id.frameLayoutMap);
        SupportMapFragment a = SupportMapFragment.newInstance();


        //Bundle bndle = new Bundle();
        //String[] ub = reserva.getUbicacio().split(" ");
        //bndle.putDouble("lat",Double.parseDouble(ub[0]));
        //bndle.putDouble("lon",Double.parseDouble(ub[1]));
        //bndle.putInt("Zoom",Integer.parseInt(ub[2]));

        getSupportFragmentManager().beginTransaction()
            .add(R.id.frameLayoutMap, new MapsActivity()).commit();
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
                                if(res.isNull("nom")){
                                    ConstraintLayout amagar = (ConstraintLayout) findViewById(R.id.constraintLayoutReserves);
                                    amagar.setVisibility(View.GONE);
                                }else{
                                    Recurs recurs = new Recurs(res.getString("nom"), res.getString("descripcio"),
                                            res.getDouble("quantiat"), res.getString("mesura")
                                            , res.getDouble("valor"));

                                    linearLayoutAddView(linearLayourRecursos,recurs);
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

    public void linearLayoutAddView(LinearLayout ll,Recurs rec){
        View fila = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.row_recurs,ll,false);

        TextView recurs = (TextView) fila.findViewById(R.id.textViewRecurs);
        TextView descripcio = (TextView) fila.findViewById(R.id.textViewDescripcio);
        TextView quantitat = (TextView) fila.findViewById(R.id.textViewQuantitat);
        TextView valor = (TextView) fila.findViewById(R.id.textViewValor);
        TextView mesura = (TextView) fila.findViewById(R.id.textViewMesura);

        recurs.setText(rec.getNom());
        descripcio.setText(rec.getDescripcio());
        quantitat.setText(Double.toString(rec.getQuantitat()));
        valor.setText("( "+Double.toString(rec.getValor()));
        mesura.setText(rec.getMesura()+" )");

        ll.addView(fila);
    }

}
