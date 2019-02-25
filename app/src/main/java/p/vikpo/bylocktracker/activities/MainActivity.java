package p.vikpo.bylocktracker.activities;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import p.vikpo.bylocktracker.R;
import p.vikpo.bylocktracker.fragments.list_fragment;
import p.vikpo.bylocktracker.fragments.map_fragment;
import p.vikpo.bylocktracker.fragments.settings_fragment;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends FragmentActivity
{

    private BottomNavigationView botNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botNavView = findViewById(R.id.bottomNavigationMenu);

        botNavView.setOnNavigationItemSelectedListener(item ->
        {
            Fragment frag = null;
            switch (item.getItemId())
            {
                case R.id.map_bottom_menu:
                    frag = map_fragment.newInstance();
                    break;
                case R.id.control_bottom_menu:
                    frag = list_fragment.newInstance();
                    break;
                case R.id.settings_bottom_menu:
                    frag = settings_fragment.newInstance();
                    break;
            }
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentLayout, frag);
            fragmentTransaction.commit();
            return true;
        });

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragmentLayout, map_fragment.newInstance());
        fragmentTransaction.commit();

        botNavView.getMenu().getItem(1).setChecked(true);
    }
}