package com.example.easyteamup.Activities.SignUpActivityFolder;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.easyteamup.R;

public class SecondSignUpFragment extends Fragment {

    public SecondSignUpFragment() {
    }

    private SignUpInterface mCallback;
    private EditText firstNameText, lastNameText, phoneText;
    private ImageButton backButton, continueButton;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (SignUpInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+ " must implement SignUpInterface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signup_second, container, false);


        firstNameText = (EditText) v.findViewById(R.id.first_name_text);
        lastNameText = (EditText) v.findViewById(R.id.last_name_text);
        phoneText = (EditText) v.findViewById(R.id.phone_text);
        continueButton = (ImageButton) v.findViewById(R.id.continue_button);
        backButton = (ImageButton) v.findViewById(R.id.back_button);

        continueButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String firstName = firstNameText.getText().toString();
                String lastName = lastNameText.getText().toString();
                long phone;
                try {
                    phone = Long.parseLong(phoneText.getText().toString()) ;
                }
                catch (NumberFormatException nfe){
                    phone = 0;
                }

                mCallback.onSecondContinue(false, firstName, lastName, phone);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCallback.onSecondContinue(true, null, null, 0);
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