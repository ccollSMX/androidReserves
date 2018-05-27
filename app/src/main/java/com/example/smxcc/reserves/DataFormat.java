package com.example.smxcc.reserves;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataFormat {
    /**
     * TIMESTAMP 2 DATE AND HOUR (STRING[2])
     * @param data
     * @return
     */
    public static String[] formatData(String data){
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
