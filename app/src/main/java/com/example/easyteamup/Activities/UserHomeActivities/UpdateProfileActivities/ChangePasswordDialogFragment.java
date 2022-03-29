package com.example.easyteamup.Activities.UserHomeActivities.UpdateProfileActivities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.easyteamup.Backend.FirebaseOperations;
import com.example.easyteamup.R;

public class ChangePasswordDialogFragment extends DialogFragment {

    private String uid;
    private String where;

    private Button cancel;
    private Button change;
    private EditText oldPass, newPass;
    private TextView error;
    private FirebaseOperations fops;

    public ChangePasswordDialogFragment(){}

    public static ChangePasswordDialogFragment newInstance() {
        ChangePasswordDialogFragment frag = new ChangePasswordDialogFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_change_password_dialog, container);

        oldPass = (EditText) v.findViewById(R.id.old_pass);
        newPass = (EditText) v.findViewById(R.id.new_pass);
        cancel = (Button) v.findViewById(R.id.cancel);
        change = (Button) v.findViewById(R.id.home);
        error = (TextView) v.findViewById(R.id.error);

        fops = new FirebaseOperations(getContext());


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().cancel();
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldps = oldPass.getText().toString();
                String newps = oldPass.getText().toString();
                fops.changePassword(oldps, newps, bc ->{
                    if(bc){
                        getDialog().cancel();
                    }
                    else {
                        error.setText("There was an issue with updating your password.");
                    }
                });
            }
        });

        return v;
    }
}

