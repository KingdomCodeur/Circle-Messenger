package fr.unice.polytech.iam;

import android.Manifest;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.unice.polytech.iam.contact.Contact;
import fr.unice.polytech.iam.utils.Macumba;
import fr.unice.polytech.iam.utils.MyVector;

public class Accueil extends AppCompatActivity {

    private List<Contact> listContacts;
    private ListView lvContacts;

//    public List<String> sms = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        checkPermissions();

        listContacts = new ContactFetcher(this,getContentResolver()).fetchAll();
        Collections.sort(listContacts);
        lvContacts = (ListView) findViewById(R.id.listContact);
        ContactsAdapter adapterContacts = new ContactsAdapter(this, listContacts);
        lvContacts.setAdapter(adapterContacts);

        Button b = (Button) findViewById(R.id.sendToServer);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.w("IDANDROID : ", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                String idUnique = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                String data = "";
                for(Contact c : listContacts){
                    if(c.getContactType() != Contact.ContactType.NONE){
                        for(MyVector v: Macumba.createVectors(c)){
                            data += v.toString()+'\n';
                        }
                    }
                }
                //Log.w("data : ",data);

                SendDataToServer query = new SendDataToServer();
                query.execute(data,idUnique);
                Log.w("SENNNNNND","YAY");
            }
        });
    }

    private void checkPermissions(){
        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.READ_CONTACTS);
        permissions.add(Manifest.permission.READ_SMS);
        permissions.add(Manifest.permission.READ_CALL_LOG);
        permissions.add(Manifest.permission.INTERNET);
        permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    // Do nothing
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{ permission }, 2);
                }
            }
        }
    }


}
