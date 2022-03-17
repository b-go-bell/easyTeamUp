package com.example.easyteamup.Activities.UserAuthActivities.SignUpActivityFolder;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.easyteamup.R;

public class FourthSignUpFragment extends Fragment {


    public FourthSignUpFragment() {
        // Required empty public constructor
    }

    private SignUpInterface mCallback;
    private EditText bioText;
    private ImageButton backButton, continueButton;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (SignUpInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement SignUpInterface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signup_fourth, container, false);

        bioText = (EditText) v.findViewById(R.id.bio_text);
        continueButton = (ImageButton) v.findViewById(R.id.continue_button);
        backButton = (ImageButton) v.findViewById(R.id.back_button);

        continueButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String bio = bioText.getText().toString();
                mCallback.onFourthContinue(false, bio);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCallback.onFourthContinue(true, null);
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

