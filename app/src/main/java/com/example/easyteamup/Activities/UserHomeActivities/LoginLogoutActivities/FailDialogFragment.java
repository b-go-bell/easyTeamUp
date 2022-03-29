package com.example.easyteamup.Activities.UserHomeActivities.LoginLogoutActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.easyteamup.Activities.UserHomeActivities.ViewProfileActivity;
import com.example.easyteamup.R;

public class FailDialogFragment extends DialogFragment {

    private String uid;
    private String where;

    private Button cancel;
    private Button home;
    private TextView text;

    public FailDialogFragment(){}

    public static FailDialogFragment newInstance(String uid, String where) {
        FailDialogFragment frag = new FailDialogFragment();

        Bundle args = new Bundle();
        args.putString("uid", uid);
        args.putString("where", where);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_leave_create_event_dialog, container);
        uid = getArguments().getString("uid");
        where = getArguments().getString("uid");

        text = (TextView) v.findViewById(R.id.dialog_message);
        cancel = (Button) v.findViewById(R.id.cancel);
        home = (Button) v.findViewById(R.id.home);

        if(where.equals("fail")){
            text.setText("Oh no! There was an issue performing that action...");
            cancel.setText("Review and try again");
            home.setText("Go back home");
        }


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().cancel();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewProfile = new Intent(getActivity(), ViewProfileActivity.class);
                viewProfile.putExtra("uid", uid);
                startActivity(viewProfile);
            }
        });

        return v;
    }
}
