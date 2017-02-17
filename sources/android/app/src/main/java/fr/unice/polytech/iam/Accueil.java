package fr.unice.polytech.iam;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import fr.unice.polytech.iam.contact.Contact;
import fr.unice.polytech.iam.utils.Macumba;
import fr.unice.polytech.iam.utils.MyVector;

public class Accueil extends AppCompatActivity {

    private List<Contact> listContacts;
    private ListView lvContacts;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
        //writeStorage();
        String str = Macumba.readData(getApplicationContext(), "circle messenger");
        Log.w("FILE!!!", "Into the read: " + str);

        String[] permissions = {Manifest.permission.READ_CONTACTS, Manifest.permission.READ_SMS, Manifest.permission.READ_CALL_LOG};
        ActivityCompat.requestPermissions(this, permissions, 200);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 200:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED && grantResults[2]==PackageManager.PERMISSION_GRANTED){
                    listContacts = new ContactFetcher(this, getContentResolver()).fetchAll();
                    Macumba.setContactTypeFile(getApplicationContext(), listContacts);
                    Collections.sort(listContacts);
                    lvContacts = (ListView) findViewById(R.id.listContact);
                    ContactsAdapter adapterContacts = new ContactsAdapter(this, listContacts);
                    lvContacts.setAdapter(adapterContacts);

                    final Button exportButton = (Button) findViewById(R.id.sendToServer);
                    exportButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                //Log.w("IDANDROID : ", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                                String idUnique = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                                String contactsJson = getJSONData();
                                Macumba.writeData(getApplicationContext(), "circle messenger", contactsJson);

                                String ret = "";

                                ExportData exportData = new ExportData();
                                ret = exportData.execute(contactsJson, idUnique).get();
                                if ("OK".equals(ret)) {
                                    Log.w("EQUALS","OK");
                                    //Toast.makeText(getApplicationContext(), "Export data OK", Toast.LENGTH_LONG);
                                } else {
                                    Log.w("EQUALS","KO");
                                    //Toast.makeText(getApplicationContext(), "Export data FAILED", Toast.LENGTH_LONG);
                                }

                                String data = "";
                                for (Contact c : listContacts) {
                                    if (c.getContactType() != Contact.ContactType.NONE) {
                                        for (MyVector v : Macumba.createVectors(c)) {
                                            data += v.toString() + '\n';
                                        }
                                    }
                                }
                                //Log.w("data : ",data);

                                SendDataToServer query = new SendDataToServer();
                                ret = query.execute(data, idUnique).get();

                                if ("OK".equals(ret)) {
                                    Toast.makeText(getApplicationContext(), "Send data to server OK", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Send data to server FAILED", Toast.LENGTH_SHORT).show();
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    Button importButton = (Button) findViewById(R.id.importFromServer);
                    importButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String idUnique = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                            Log.w("MY_ID", idUnique);
                            ImportData query = new ImportData();
                            try {
                                String ret = query.execute(idUnique).get();
                                Log.w("RETOUR", ret);
                                Macumba.setContactTypeFromJSONArray(listContacts, new JSONArray(ret));
                                String json = getJSONData();
                                Macumba.writeData(getApplicationContext(), "circle messenger", json);
                                Toast.makeText(getApplicationContext(), "Import OK", Toast.LENGTH_SHORT).show();

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                break;
        }
    }

    public String getJSONData() {
        JSONArray jsonArray = new JSONArray();
        for (Contact contact : listContacts) {
            if (contact.hasType()) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("num", contact.getNumbers().get(0).getNumber());
                    jsonObject.put("type", contact.getContactType().name());
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return jsonArray.toString();
    }

}
