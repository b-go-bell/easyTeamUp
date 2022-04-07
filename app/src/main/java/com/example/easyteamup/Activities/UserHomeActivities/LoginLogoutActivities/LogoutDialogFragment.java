package com.example.easyteamup.Activities.UserHomeActivities.LoginLogoutActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

import com.example.easyteamup.Activities.UserHomeActivities.StartActivity;
import com.example.easyteamup.Backend.FirebaseOperations;
import com.example.easyteamup.R;

public class LogoutDialogFragment extends DialogFragment {

    private String uid;

    private Button cancel;
    private Button logout;
    private FirebaseOperations fops;

    public LogoutDialogFragment(){}

    public static LogoutDialogFragment newInstance(String uid) {
        LogoutDialogFragment frag = new LogoutDialogFragment();

        Bundle args = new Bundle();
        args.putString("uid", uid);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_logout_dialog, container);
        uid = getArguments().getString("uid");
        fops = new FirebaseOperations(getContext());

        cancel = (Button) v.findViewById(R.id.cancel);
        logout = (Button) v.findViewById(R.id.logout_button);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().cancel();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fops.logoutUser();
                Intent restart = new Intent(getActivity(), StartActivity.class);
                try{
                    Thread.sleep(500);
                } catch(InterruptedException e){ e.printStackTrace();}
                startActivity(restart);
            }
        });

        return v;
    }
}

