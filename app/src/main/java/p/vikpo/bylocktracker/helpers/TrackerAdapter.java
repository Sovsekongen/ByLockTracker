package p.vikpo.bylocktracker.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import p.vikpo.bylocktracker.R;

public class TrackerAdapter extends ArrayAdapter<Tracker>
{
    private ViewHolder viewHolder;

    private static class ViewHolder
    {
        private TextView itemView;
    }

    public TrackerAdapter(@NonNull Context context, @NonNull List<Tracker> objects)
    {
        super(context, 0, objects);
    }

    public View getView(int pos, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.fragment_list, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.itemView = (TextView) convertView.findViewById(R.id.listTextView);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Tracker item = getItem(pos);
        if (item!= null)
        {
            viewHolder.itemView.setText(String.format("%s", item.getBikeOwner()));
        }

        return convertView;
    }
}
