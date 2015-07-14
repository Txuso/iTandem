package Classes;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.josurubio.itandem.R;

/**
 * Created by josurubio on 08/04/15.
 */
public class CustomTandemListViewAdapter extends ArrayAdapter<TandemListRowItem> {

    Context context;

    public CustomTandemListViewAdapter(Context context, int resourceId,
                                 List<TandemListRowItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtNameAge;
        TextView txtKnownLang;
        TextView txtSpeakLang;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        TandemListRowItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.tandem_list_item, null);
            holder = new ViewHolder();
            holder.txtKnownLang = (TextView) convertView.findViewById(R.id.ISpeakText);
            holder.txtSpeakLang = (TextView) convertView.findViewById(R.id.IWantLearnText);
            holder.txtNameAge = (TextView) convertView.findViewById(R.id.NameAgeText);
            holder.imageView = (ImageView) convertView.findViewById(R.id.TandemIcon);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtKnownLang.setText(rowItem.getknownLang());
        holder.txtSpeakLang.setText(rowItem.getSpeakLang());
        holder.txtNameAge.setText(rowItem.getnameAge());
        holder.imageView.setImageDrawable(rowItem.getImageId());
        //holder.imageView.setBackground(rowItem.getImageId());

        return convertView;
    }

}
