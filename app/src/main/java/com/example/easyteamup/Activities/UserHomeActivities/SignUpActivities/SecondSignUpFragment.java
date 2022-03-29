package com.example.easyteamup.Activities.UserHomeActivities.SignUpActivities;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
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
    private ImageButton backButton, continueButton, transContinueButton;
    private boolean first, last;

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
        transContinueButton = (ImageButton) v.findViewById(R.id.continue_button_transparent);
        continueButton = (ImageButton) v.findViewById(R.id.continue_button);
        backButton = (ImageButton) v.findViewById(R.id.back_button);
        transContinueButton.setEnabled(false);
        continueButton.setEnabled(false);
        continueButton.setVisibility(View.INVISIBLE);

        firstNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    continueButton.setEnabled(false);
                    continueButton.setVisibility(View.INVISIBLE);
                    first = false;

                } else {
                    first = true;
                }

                if(first && last){
                    continueButton.setEnabled(true);
                    continueButton.setVisibility(View.VISIBLE);
                    transContinueButton.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        lastNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    continueButton.setEnabled(false);
                    continueButton.setVisibility(View.INVISIBLE);
                    last = false;

                } else {
                    last = true;
                }

                if(first && last){
                    continueButton.setEnabled(true);
                    continueButton.setVisibility(View.VISIBLE);
                    transContinueButton.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

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