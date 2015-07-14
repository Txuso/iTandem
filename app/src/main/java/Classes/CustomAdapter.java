package Classes;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.josurubio.itandem.R;

/**
 * Created by josurubio on 05/04/15.
 * Custom Adapter for the language list
 */

public class CustomAdapter extends ArrayAdapter {
        Model[] modelItems = null;
        Context context;
public CustomAdapter(Context context, Model[] resource) {
        super(context, R.layout.languages_listview,resource);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.modelItems = resource;
        }
@Override
public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.languages_listview, parent, false);
        TextView name = (TextView) convertView.findViewById(R.id.languageName);
        final CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkbox);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb.isChecked())
                    modelItems[position].setValue(1);
                else if (isChecked == false)
                    modelItems[position].setValue(0);

            }
        });
        name.setText(modelItems[position].getName());
        if(modelItems[position].getValue() == 1)
            cb.setChecked(true);
        else
            cb.setChecked(false);
        return convertView;
        }
}
