package com.example.easyteamup.Activities.SignUpActivityFolder;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.easyteamup.Activities.SignUpActivityFolder.SignUpInterface;
import com.example.easyteamup.R;

public class ThirdSignUpFragment extends Fragment {


    public ThirdSignUpFragment() {
        // Required empty public constructor
    }

    private SignUpInterface mCallback;
    private EditText majorText, yearText;
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
        View v = inflater.inflate(R.layout.fragment_signup_third, container, false);


        majorText = (EditText) v.findViewById(R.id.major_text);
        yearText = (EditText) v.findViewById(R.id.year_text);
        continueButton = (ImageButton) v.findViewById(R.id.continue_button);
        backButton = (ImageButton) v.findViewById(R.id.back_button);

        continueButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String major = majorText.getText().toString();
                int year;
                try {
                    year = Integer.parseInt(yearText.getText().toString()) ;
                }
                catch (NumberFormatException nfe){
                    year = 0;
                }
                mCallback.onThirdContinue(false, major, year);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCallback.onThirdContinue(true, null, 0);
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