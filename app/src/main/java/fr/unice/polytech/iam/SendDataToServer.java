package fr.unice.polytech.iam;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
            URL url = new URL("http://colombet-aoechat.rhcloud.com/recupData.php?data="+ URLEncoder.encode(strings[0]) + "&id=" + URLEncoder.encode(strings[1]));//+strings[0]);
            Log.w("DEBUG URL : ",url.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.getInputStream();
            /*
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer json = new StringBuffer(1024);
            String tmp = "";
            while ((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
            if (data.getInt("cod") != 200) {
                Log.w("Code retour : ", ""+data.getInt("cod"));
                return null;
            }
            return data.toString();*/

        } catch (MalformedURLException e) {
            Log.w("EROOOOOOOR","EROOOOOOR");
            e.printStackTrace();
        } catch (IOException e) {
            Log.w("EROOOOOOOR","EROOOOOOR");
            e.printStackTrace();
        }
        return "OK";
    }
}
