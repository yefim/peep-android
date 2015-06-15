package com.chat.peep;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by geoff on 6/10/15.
 */
public class NameAdapter extends ArrayAdapter<Name> {
    private int resource;

    public NameAdapter(Context context, int resource) {
        super(context, resource);
        this.resource = resource;
    }

    public NameAdapter(Context context, int resource, List<Name> names) {
        super(context, resource, names);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Name name = getItem(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(resource, parent, false);

        TextView mName = (TextView) rowView.findViewById(R.id.name);
        mName.setText(name.getName());

        return rowView;
    }
}
