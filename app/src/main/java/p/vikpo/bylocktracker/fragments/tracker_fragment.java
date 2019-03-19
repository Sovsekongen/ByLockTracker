package p.vikpo.bylocktracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import p.vikpo.bylocktracker.R;
import p.vikpo.bylocktracker.helpers.Tracker;

public class tracker_fragment extends Fragment
{

    private TextView longEdit, latEdit, batPerEdit, ownerNameEdit;

    public static tracker_fragment newInstance()
    {
        return new tracker_fragment();
    }

    @Override
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);

    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_tracker, container, false);

        longEdit = v.findViewById(R.id.longEdit);
        latEdit = v.findViewById(R.id.latEdit);
        batPerEdit = v.findViewById(R.id.batPerEdit);
        ownerNameEdit = v.findViewById(R.id.ownerNameEdit);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

    }


    public void addItem(Tracker trackedItem)
    {
        longEdit.setText(Double.toString(trackedItem.getLatLng().longitude));
        latEdit.setText(Double.toString(trackedItem.getLatLng().latitude));
        batPerEdit.setText(Double.toString(trackedItem.getBatteryPer()));
        ownerNameEdit.setText(trackedItem.getBikeOwner());
    }
}
