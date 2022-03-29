package com.example.easyteamup.Activities.SnackBarActivities;

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
    private ImageButton viewPublic, viewInvitations, createEvent, viewProfile, viewHistory;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (SnackBarInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+ " must implement SnackBarInterface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_snackbar, container, false);

        viewPublic = (ImageButton) v.findViewById(R.id.public_events);
        viewInvitations = (ImageButton) v.findViewById(R.id.invitations);
        createEvent = (ImageButton) v.findViewById(R.id.create_event);
        viewProfile = (ImageButton) v.findViewById(R.id.user_profile);
        viewHistory = (ImageButton) v.findViewById(R.id.user_history);

        viewPublic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCallback.viewPublicEvents();
            }
        });

        viewInvitations.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCallback.viewInvitations();
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

