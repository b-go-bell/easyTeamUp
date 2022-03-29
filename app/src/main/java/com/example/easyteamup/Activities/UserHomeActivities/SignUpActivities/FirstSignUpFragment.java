package com.example.easyteamup.Activities.UserHomeActivities.SignUpActivities;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.example.easyteamup.R;

public class FirstSignUpFragment extends Fragment {

    private SignUpInterface mCallback;

    private EditText usernameText, passwordText;
    private ImageButton continueButton, transContinueButton;
    private boolean usr, psd;

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
        View v = inflater.inflate(R.layout.fragment_signup_first, container, false);

        usernameText = (EditText) v.findViewById(R.id.username_text);
        passwordText = (EditText) v.findViewById(R.id.password_text);
        transContinueButton = (ImageButton) v.findViewById(R.id.continue_button_transparent);
        continueButton = (ImageButton) v.findViewById(R.id.continue_button);
        transContinueButton.setEnabled(false);
        continueButton.setEnabled(false);
        continueButton.setVisibility(View.INVISIBLE);

        usernameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    continueButton.setEnabled(false);
                    continueButton.setVisibility(View.INVISIBLE);
                    usr = false;

                } else {
                    usr = true;
                }

                if(usr && psd){
                    continueButton.setEnabled(true);
                    continueButton.setVisibility(View.VISIBLE);
                    transContinueButton.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        passwordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    continueButton.setEnabled(false);
                    continueButton.setVisibility(View.INVISIBLE);
                    psd = false;

                } else {
                    psd = true;
                }

                if(usr && psd){
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
                String username = usernameText.getText().toString();
                String password = passwordText.getText().toString();

                mCallback.onFirstContinue(username, password);
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
