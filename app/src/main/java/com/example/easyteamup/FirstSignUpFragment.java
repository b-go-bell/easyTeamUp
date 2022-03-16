package com.example.easyteamup;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

public class FirstSignUpFragment extends Fragment {

    private SignUpInterface mCallback;

    private EditText usernameText, passwordText;
    private ImageButton continueButton;

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
        continueButton = (ImageButton) v.findViewById(R.id.continue_button);

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
