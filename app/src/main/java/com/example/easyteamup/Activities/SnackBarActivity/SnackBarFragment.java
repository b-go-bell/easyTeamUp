package com.example.easyteamup.Activities.SnackBarActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.example.easyteamup.R;

public class SnackBarFragment extends Fragment {

    private SnackBarInterface mCallback;
    private ImageButton viewMap, viewList, createEvent, viewProfile, viewHistory;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (SnackBarInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+ " must implement SignUpInterface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_snackbar, container, false);

        viewMap = (ImageButton) v.findViewById(R.id.public_map);
        viewList = (ImageButton) v.findViewById(R.id.public_list);
        createEvent = (ImageButton) v.findViewById(R.id.create_event);
        viewProfile = (ImageButton) v.findViewById(R.id.user_profile);
        viewHistory = (ImageButton) v.findViewById(R.id.user_history);

        viewMap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCallback.viewPublicMapEvents();
            }
        });

        viewList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCallback.viewPublicListEvents();
            }
        });

        createEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCallback.createEvent();
            }
        });

        viewProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCallback.viewUserProfile();
            }
        });

        viewHistory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCallback.viewUserHistory();
            }
        });

        return v;
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }
}

