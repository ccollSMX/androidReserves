package com.example.smxcc.reserves;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReservaActivity extends AppCompatActivity {
    RequestQueue queue;
    //String urlLogin = "http://10.0.2.2/empresa/app_reserves.php";
    String url = this.getString(R.string.ip);
    String urlLogin = url+"app_reserves.php";
    TextView textViewObjecte;
    TextView textViewDataInici;
    TextView textViewDataFi;
    TextView textViewHoraInici;
    TextView textViewHoraFi;
    TextView textViewReserva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva);

        queue = Volley.newRequestQueue(this);

        Reserva reserva = new Reserva(getIntent().getExtras().getInt("id"),getIntent().getExtras().getString("inici")
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
