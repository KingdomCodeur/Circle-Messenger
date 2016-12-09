package com.example.colombet.circlemessenger;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Accueil extends AppCompatActivity {

    TextView tv;
    ListView lv;
    public List<String> sms = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        ArrayList<String> permissions = new ArrayList<String>();
        permissions.add(Manifest.permission.READ_CONTACTS);
        permissions.add(Manifest.permission.READ_SMS);
        checkPermissions(permissions);

        tv = (TextView)findViewById(R.id.debug);
        ArrayList<String> lstName = new ArrayList<String>();
        lv = (ListView)findViewById(R.id.listContact);

        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
        if(cur.getCount() > 0){
            while(cur.moveToNext()){
                //cur.getString(cur.getInt(cur.getColumnIndex(ContactsContract.Contacts._ID )))
                lstName.add(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                //tv.append(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME) )+ "\n");
            }
        }
        getAllSms();
        Collections.sort(lstName);
        ArrayAdapter<String> ad = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,lstName);
        lv.setAdapter(ad);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedFromList = (String) lv.getItemAtPosition(i);
                Toast toast = Toast.makeText(getApplicationContext(),selectedFromList,Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private void checkPermissions(List<String> perms){
        for (String s : perms) {
            if (ContextCompat.checkSelfPermission(this, s) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, s)) {
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{s}, 2);
                }
            }
        }
    }

    public List<String> getAllSms(){
        Uri uriSMSURI = Uri.parse("content://sms/inbox");
        Cursor cur = getContentResolver().query(uriSMSURI, null, null, null, null);

        while(cur.moveToNext()) {
            String address = cur.getString(cur.getColumnIndex("address"));
            String body = cur.getString(cur.getColumnIndexOrThrow("body"));
            Log.w("DEBUGSMS : ","Number: "+ address + " .Message : " + body); // on affiche tous les sms dans le debug
            sms.add("Number: "+ address + " .Message : " + body);
        }

        return sms;
    }


}
