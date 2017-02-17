package fr.unice.polytech.iam;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by colombet on 04/01/17.
 */

public class SendDataToServer extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url =  new URL("http://colombet-aoechat.rhcloud.com/recupData.php");
            //URL url =  new URL("http://10.188.6.183/CircleMessenger/recupData.php"); //POUR DEBUG EN LOCAL
            Log.w("DEBUG URL : ",url.toString());

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

//            connection.setReadTimeout(10000);
//            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            /*On cree les variables POST*/
            ContentValues values = new ContentValues();
            values.put("data",strings[0]);
            values.put("id",strings[1]);

            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

            writer.write(getQuery(values)); //On ecrit les variable POST
            writer.flush();
            writer.close();
            os.close();
            connection.getInputStream();
            return "OK";

        } catch (MalformedURLException e) {
            Log.w("EROOOOOOOR","EROOOOOOR");
            e.printStackTrace();
        } catch (IOException e) {
            Log.w("EROOOOOOOR","EROOOOOOR");
            e.printStackTrace();
        }
        return "";
    }

    /*Methode qui prend une liste de variable POST et creer la query correspondante ....*/
    private String getQuery(ContentValues params){
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for(String k : params.keySet()){
            if(first)
                first=false;
            else
                result.append("&");

            try {
                result.append(URLEncoder.encode(k,"UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(params.get(k).toString(),"UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        //Log.w("ICI : ",result.toString()); //DEBUG pour voir la requete qui est cree
        return result.toString();
    }
}
