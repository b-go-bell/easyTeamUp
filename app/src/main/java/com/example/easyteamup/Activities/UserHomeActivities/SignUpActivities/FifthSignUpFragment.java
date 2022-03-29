package com.example.easyteamup.Activities.UserHomeActivities.SignUpActivities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.example.easyteamup.R;

public class FifthSignUpFragment extends Fragment {

    public FifthSignUpFragment() {
        // Required empty public constructor
    }

    private SignUpInterface mCallback;
    private ImageView profilePhoto;
    private ImageButton backButton;
    private Button signupButton;
    private Uri imageUri;

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
        View v = inflater.inflate(R.layout.fragment_signup_fifth, container, false);

        profilePhoto = (ImageView) v.findViewById(R.id.profile_photo);
        signupButton = (Button) v.findViewById(R.id.submit_button);
        backButton = (ImageButton) v.findViewById(R.id.back_button);

        ActivityResultLauncher<String> imageGallery = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        imageUri = uri;
                        profilePhoto.setImageURI(null);
                        profilePhoto.setImageURI(imageUri);
                    }
                });

        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageGallery.launch("image/*");
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                mCallback.onFifthSubmit(false, imageUri);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCallback.onFifthSubmit(true, null);
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
