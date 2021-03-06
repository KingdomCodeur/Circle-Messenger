package fr.unice.polytech.iam;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import fr.unice.polytech.iam.contact.Contact;
import fr.unice.polytech.iam.contact.PhoneCall;
import fr.unice.polytech.iam.utils.Macumba;
import fr.unice.polytech.iam.utils.MyVector;

public class ContactsAdapter extends ArrayAdapter<Contact> {

    HashMap<Integer,Spinner> holdSpin = new HashMap<>();

    public ContactsAdapter(Context context, List<Contact> contacts) {
        super(context, 0, contacts);
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {



        // Get the data item
        final Contact contact = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        View view = convertView;

        if (null == parent) {
            return view;
        }

        if (null == view) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.adapter_contact_item, parent, false);
        }

        // Populate the data into the template view using the data object
        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        TextView tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        TextView tvPhone = (TextView) view.findViewById(R.id.tvPhone);

        if (null == contact) {
            return view;
        }

        final ListView tvSMS = (ListView) view.findViewById(R.id.tvSMS);

        tvName.setText(contact.getName());
        tvEmail.setText("");
        tvPhone.setText("");

        if (contact.getEmails().size() > 0 && contact.getEmails().get(0) != null) {
            tvEmail.setText(contact.getEmails().get(0).getAddress());
        }
        if (contact.getNumbers().size() > 0 && contact.getNumbers().get(0) != null) {
            tvPhone.setText(contact.getNumbers().get(0).getNumber());
        }

        tvSMS.setAdapter(new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,contact.getSMS()));

        ImageView tri = (ImageView) view.findViewById(R.id.triforce);
        final ImageView icon = (ImageView) view.findViewById(R.id.iconType);
        final Spinner deroulante = (Spinner) view.findViewById(R.id.spinner);

        tri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.w("Mamamia : ", contact.getName());
                List<MyVector> vectors = Macumba.createVectors(contact);
                //Log.w("Mamamia : ", vectors.size() + " vecteurs");
                StringBuilder sb = new StringBuilder();
                for (MyVector vector : vectors) {
                    sb.append(vector.toString());
                    sb.append("\n");
                }
                //Log.w("Mamamia : ", sb.toString());

                JSONObject jsonObject = new JSONObject();
                sb = new StringBuilder();
                JSONArray jsonArray = new JSONArray();

                for (MyVector vector : vectors) {
                    jsonArray.put(vector.toJSONArrayWithoutType());
                }

                try {
                    jsonObject.put(contact.getNumbers().get(0).getNumber(), jsonArray);
                    AskForType query = new AskForType();
                    Log.w("SEND", jsonObject.toString());
                    String ret = query.execute(jsonObject.toString()).get();
                    Log.w("RRRRRRRRRR", ret);
                    jsonArray = new JSONArray(ret);
                    String type = jsonArray.getJSONObject(0).getString("type");
                    Double confiance = jsonArray.getJSONObject(0).getDouble("confiance");
                    Toast.makeText(getContext(), contact.getName() + " est un(e) " + type + " !" + "Avec une confiance de "+confiance+"%", Toast.LENGTH_LONG).show();
                    if (Contact.ContactType.AMI.name().toLowerCase().equals(type)) {
                        contact.setContactType(Contact.ContactType.AMI);
                        deroulante.setSelection(1);
                        icon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ami));
                    } else if (Contact.ContactType.FAMILLE.name().toLowerCase().equals(type)) {
                        contact.setContactType(Contact.ContactType.FAMILLE);
                        deroulante.setSelection(2);
                        icon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.famille));
                    } else if (Contact.ContactType.COLLEGUE.name().toLowerCase().equals(type)) {
                        contact.setContactType(Contact.ContactType.COLLEGUE);
                        deroulante.setSelection(3);
                        icon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.collegue));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        if(!holdSpin.containsKey(position)) {
            List choose = new ArrayList();
            choose.add("None");
            choose.add("Ami");
            choose.add("Famille");
            choose.add("Collegue");
            ArrayAdapter adapter = new ArrayAdapter(
                    getContext(),
                    android.R.layout.simple_spinner_item,
                    choose
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            deroulante.setAdapter(adapter);
            holdSpin.put(position,deroulante);
        }

        switch(contact.getContactType()) {
            case NONE:
                deroulante.setSelection(0);
                icon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.none));
                break;
            case AMI:
                deroulante.setSelection(1);
                icon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ami));
                break;
            case FAMILLE:
                deroulante.setSelection(2);
                icon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.famille));
                break;
            case COLLEGUE:
                deroulante.setSelection(3);
                icon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.collegue));
                break;
        }


        deroulante.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Log.w("DEROULANTE : ","CHANGED!!!"+((TextView) selectedItemView).getText()+" Name : "+contact.getName());
                String type = ((TextView) selectedItemView).getText().toString();
                switch (type) {
                    case "None":
                        contact.setContactType(Contact.ContactType.NONE);
                        icon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.none));
                        break;
                    case "Ami":
                        contact.setContactType(Contact.ContactType.AMI);
                        icon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ami));
                        break;
                    case "Famille":
                        contact.setContactType(Contact.ContactType.FAMILLE);
                        icon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.famille));
                        break;
                    case "Collegue":
                        contact.setContactType(Contact.ContactType.COLLEGUE);
                        icon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.collegue));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //Ne rentre jamais là
                //Log.w("DEROULANTE : ","SAME!!!");
            }

        });

        return view;
    }
}
