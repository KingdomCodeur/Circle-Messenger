package fr.unice.polytech.iam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import fr.unice.polytech.iam.contact.Contact;
import fr.unice.polytech.iam.contact.PhoneCall;

public class ContactsAdapter extends ArrayAdapter<Contact> {

    public ContactsAdapter(Context context, List<Contact> contacts) {
        super(context, 0, contacts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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

        tri.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                List<PhoneCall> l = contact.getPhoneCalls();
                if (null != l) {
                    Toast.makeText(getContext(), contact.getPhoneCalls().size() + " appels et " + contact.getSMS().size() + " sms.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "null == l", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*
        tri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //Permet l'affichage des sms mais bon quelques petits bugs ...
                Toast t = Toast.makeText(getContext(),"Affichage des sms...",Toast.LENGTH_SHORT);
                t.show();
                RelativeLayout tmp =(RelativeLayout)view.getParent();
                ListView tmpSMS = (ListView) tmp.findViewById(R.id.tvSMS);
                if(tmpSMS.getVisibility() == View.VISIBLE){
                    tmpSMS.setVisibility(View.GONE);
                }
                else {
                    tmpSMS.setVisibility(View.VISIBLE);
                }

                //tmpSMS.setMinimumHeight(100*tmpSMS.getChildCount());
                //Log.w("Nombre d'enfants : ",""+tmpSMS.getChildCount());
            }
        });
        //*/

        return view;
    }
}
