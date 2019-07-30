package p.vikpo.bylocktracker.helpers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.graphics.drawable.DrawableWrapper;

import java.util.ArrayList;
import java.util.List;

import p.vikpo.bylocktracker.R;

public class DialogPickColourAdapter extends ArrayAdapter<String>
{
    private List<String> colours;
    private Context context;

    public DialogPickColourAdapter(@NonNull Context context, @NonNull List<String> objects)
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

        String item = getItem(pos);
        emblem.setImageDrawable(AppCompatResources.getDrawable(getContext(), R.drawable.ic_brightness_1_black_72dp));
        emblem.setColorFilter(Color.parseColor(item));

        return newView;
    }
}

