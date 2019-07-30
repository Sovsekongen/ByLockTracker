package p.vikpo.bylocktracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Arrays;

import p.vikpo.bylocktracker.R;
import p.vikpo.bylocktracker.helpers.DialogPickColourAdapter;
import p.vikpo.bylocktracker.helpers.DialogPickIconAdapter;

public class FragmentDialogList extends DialogFragment
{
    private Button dialog_pick_cancel_but;
    private GridView dialog_pick_list;
    private final String[] colour_list_array = {"#FF0000", "#FF7F00", "#FFFF00", "#7FFF00", "#00FF00", "#00FF7F",
            "#00FFFF", "#007FFF", "#0000FF", "#7F00FF", "#FF00FF", "#FF007F"};
    private final Integer[] icon_list_array = {R.drawable.android_bike, R.drawable.ic_brightness_1_black_48dp};
    private final ArrayList<String> colour_list = new ArrayList<>(Arrays.asList(colour_list_array));
    private final ArrayList<Integer> icon_list = new ArrayList<>(Arrays.asList(icon_list_array));
    private String colour = "null", title;
    private DialogPickColourAdapter listAdapterColour;
    private DialogPickIconAdapter listAdapterIcon;
    private int icon = 0;

    public interface OnColourPicked
    {
        void onColourPicked(String colour);
    }

    public interface OnIconPicked
    {
        void onIconPicked(int id);
    }

    public FragmentDialogList(){

    }

    @Override
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        listAdapterColour = new DialogPickColourAdapter(getContext(), colour_list);
        listAdapterIcon = new DialogPickIconAdapter(getContext(), icon_list);
    }

    public static FragmentDialogList newInstance(String title)
    {
        FragmentDialogList frag = new FragmentDialogList();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.dialog_pick_colour, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);

        dialog_pick_cancel_but = view.findViewById(R.id.dialog_pick_cancel_button);
        dialog_pick_list = view.findViewById(R.id.dialog_pick_colour_grid);

        if(title.equals("Pick Colour"))
        {
            dialog_pick_list.setAdapter(listAdapterColour);

            dialog_pick_list.setOnItemClickListener((parent, view1, position, id) ->
            {
                colour = colour_list.get((int) id);
                sendResultColour();
            });
        }
        else if(title.equals("Pick Icon"))
        {
            dialog_pick_list.setAdapter(listAdapterIcon);

            dialog_pick_list.setOnItemClickListener((parent, view1, position, id) ->
            {
                icon = icon_list.get((int) id);
                sendResultIcon();
            });
        }


        dialog_pick_cancel_but.setOnClickListener(v ->
        {
            getDialog().dismiss();
        });
    }

    public void sendResultColour()
    {
        if (title != null && title.equals("Pick Colour"))
        {
            OnColourPicked list = (OnColourPicked) getTargetFragment();
            if (colour.isEmpty())
            {
                list.onColourPicked("null");
            }
            else
            {
                list.onColourPicked(colour);
            }
        }

        dismiss();
    }

    public void sendResultIcon()
    {
        OnIconPicked list = (OnIconPicked) getTargetFragment();
        list.onIconPicked(icon);
        dismiss();
    }
}
