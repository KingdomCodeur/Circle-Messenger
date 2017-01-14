package fr.unice.polytech.iam;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.unice.polytech.iam.contact.Contact;
import fr.unice.polytech.iam.utils.Macumba;
import fr.unice.polytech.iam.utils.MyVector;

public class Accueil extends AppCompatActivity {

    private List<Contact> listContacts;
    private ListView lvContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);


        String[] permissions = {Manifest.permission.READ_CONTACTS, Manifest.permission.READ_SMS, Manifest.permission.READ_CALL_LOG};
        ActivityCompat.requestPermissions(this, permissions, 200);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 200:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED && grantResults[2]==PackageManager.PERMISSION_GRANTED){
                    listContacts = new ContactFetcher(this, getContentResolver()).fetchAll();
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
                            for (Contact c : listContacts) {
                                if (c.getContactType() != Contact.ContactType.NONE) {
                                    for (MyVector v : Macumba.createVectors(c)) {
                                        data += v.toString() + '\n';
                                    }
                                }
                            }
                            //Log.w("data : ",data);

                            SendDataToServer query = new SendDataToServer();
                            query.execute(data, idUnique);
                            Log.w("SENNNNNND", "YAY");
                        }
                    });
                }
                break;
        }
    }
}
