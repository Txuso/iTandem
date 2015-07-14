package Classes;

import android.app.Activity;
import android.content.ClipData;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.josurubio.itandem.R;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by josurubio on 17/03/15.
 */
public class NavigationAdapter extends BaseAdapter {
    private Activity activity;
    ArrayList<ItemObject> arrayitms;

    public NavigationAdapter (Activity activity, ArrayList<ItemObject> arrayitms){
        super();
        this.activity = activity;
        this.arrayitms = arrayitms;
    }

    //Returns the object Item_objct in the array list
    @Override
    public Object getItem(int position){
        return arrayitms.get(position);

    }

    public int getCount(){
        return arrayitms.size();

    }

    @Override
    public long getItemId(int position){
        return  position;

    }

    //we declare the static class that will represent the file
    public static class File{

        TextView title_itm;
        ImageView icon;
    }

    public View getView(int position, View converView, ViewGroup parent){
        File view;
        LayoutInflater inflator = activity.getLayoutInflater();
        if (converView == null){

            view = new File();
            //we create the item object and we get it from the array
            ItemObject itm = arrayitms.get(position);
            converView = inflator.inflate(R.layout.itm, null);
            //title
            view.title_itm = (TextView)converView.findViewById(R.id.title_item);
            //we set the title with the name that we got before from the object
            view.title_itm.setText(itm.getTitle());
            //Icon
            view.icon = (ImageView) converView.findViewById(R.id.icon);
            //we set the icon
            view.icon.setImageResource(itm.getIcon());
            converView.setTag(view);
        }
        else
            view = (File)converView.getTag();
        return converView;
    }



}
