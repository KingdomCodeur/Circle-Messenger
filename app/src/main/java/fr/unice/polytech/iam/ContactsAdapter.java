package fr.unice.polytech.iam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

//import com.example.colombet.R;

import java.util.List;

public class ContactsAdapter extends ArrayAdapter<Contact> {

    public ContactsAdapter(Context context, List<Contact> contacts) {
        super(context, 0, contacts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item
        Contact contact = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        View view = convertView;

        if (null == view) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.adapter_contact_item, parent, false);
        }

        // Populate the data into the template view using the data object
        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        TextView tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        TextView tvPhone = (TextView) view.findViewById(R.id.tvPhone);
        tvName.setText(contact.getName());
        tvEmail.setText("");
        tvPhone.setText("");

        if (contact.getEmails().size() > 0 && contact.getEmails().get(0) != null) {
            tvEmail.setText(contact.getEmails().get(0).getAddress());
        }
        if (contact.getNumbers().size() > 0 && contact.getNumbers().get(0) != null) {
            tvPhone.setText(contact.getNumbers().get(0).getNumber());
        }

        return view;
    }
}
