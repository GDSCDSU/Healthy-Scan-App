package com.healthyscan;
import static android.app.PendingIntent.getActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.healthyscan.Dashboard;
import com.healthyscan.R;


public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Find the ImageView and set an OnClickListener
        ImageView imageViewMenu = view.findViewById(R.id.imageViewMenu);
        imageViewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer();
            }
        });

        return view;
    }

    // Open Navigation Drawer directly from HomeFragment
    private void openDrawer() {
        // Get the activity and open the Navigation Drawer
        if (getActivity() instanceof Dashboard) {
            ((Dashboard) getActivity()).openDrawer();
        }
    }
}
