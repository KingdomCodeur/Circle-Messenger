package com.example.colombet.circlemessenger;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.sip.SipAudioCall;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Accueil extends AppCompatActivity {

    TextView tv;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        2);
            }
        }


        tv = (TextView)findViewById(R.id.debug);
        //tv.setText("toot");
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
        ArrayAdapter<String> ad = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,lstName);
        lv.setAdapter(ad);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedFromList = (String) lv.getItemAtPosition(i);
                Toast toast = Toast.makeText(getApplicationContext(),selectedFromList,Toast.LENGTH_SHORT);
                //tv.setText(selectedFromList);
                toast.show();
            }
        });
    }


}
