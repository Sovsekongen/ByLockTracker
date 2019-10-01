package p.vikpo.bylocktracker.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import p.vikpo.bylocktracker.R;
import p.vikpo.bylocktracker.fragments.FragmentList;
import p.vikpo.bylocktracker.fragments.FragmentMap;

public class MainActivity extends FragmentActivity
{


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView botNavView = findViewById(R.id.bottomNavigationMenu);

        botNavView.setOnNavigationItemSelectedListener(item ->
        {
            Fragment frag;
            switch (item.getItemId())
            {
                case R.id.map_bottom_menu:
                    frag = FragmentMap.newInstance();
                    break;
                case R.id.control_bottom_menu:
                    frag = FragmentList.newInstance();
                    break;
                default:
                    frag = FragmentMap.newInstance();
            }
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentLayout, frag);
            fragmentTransaction.commit();
            return true;
        });

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragmentLayout, FragmentMap.newInstance());
        fragmentTransaction.commit();

        botNavView.getMenu().getItem(1).setChecked(true);


    }
}
