package p.vikpo.bylocktracker.helpers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import p.vikpo.bylocktracker.R;

public class TrackerAdapter extends ArrayAdapter<Tracker>
{
    private ViewHolder viewHolder;
    private ColorFilter cf;
    private Context context;

    private static class ViewHolder
    {
        private TextView addressView;
        private TextView ownerName;
        private ImageView emblem;
    }

    public TrackerAdapter(@NonNull Context context, @NonNull List<Tracker> objects)
    {
        super(context, 0, objects);
        this.context = context;
    }

    public View getView(int pos, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.fragment_list_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.ownerName = (TextView) convertView.findViewById(R.id.ownerName);
            viewHolder.addressView = (TextView) convertView.findViewById(R.id.addressTextView);
            viewHolder.emblem = (ImageView) convertView.findViewById(R.id.picBike);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Tracker item = getItem(pos);
        if (item!= null)
        {
            viewHolder.ownerName.setText(String.format("%s", item.getBikeOwner()));
            viewHolder.addressView.setText(String.format("%s", item.getAddress()));
            viewHolder.emblem.setImageDrawable(AppCompatResources.getDrawable(context, item.getIconSource() ));
            if(item.getColour() != null)
            {
                int col = Color.parseColor(item.getColour());
                viewHolder.emblem.setColorFilter(col);
            }
        }

        return convertView;
    }
}
