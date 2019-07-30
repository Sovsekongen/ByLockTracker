package p.vikpo.bylocktracker.helpers;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;

import java.util.List;

import p.vikpo.bylocktracker.R;

public class DialogPickIconAdapter extends ArrayAdapter<Integer>
{
    private List<Integer> colours;
    private Context context;

    public DialogPickIconAdapter(@NonNull Context context, @NonNull List<Integer> objects)
    {
        super(context, -1, objects);
        this.colours = objects;
        this.context = context;
    }

    public View getView(int pos, View convertView, ViewGroup parent)
    {
        View newView = convertView;
        if (newView == null)
        {
            newView = LayoutInflater.from(this.getContext()).inflate(R.layout.dialog_pick_list_item, parent, false);
        }

        ImageView emblem = newView.findViewById(R.id.dialog_pick_list_item_id);

        int item = getItem(pos);
        emblem.setImageDrawable(AppCompatResources.getDrawable(getContext(), item));

        return newView;
    }
}
