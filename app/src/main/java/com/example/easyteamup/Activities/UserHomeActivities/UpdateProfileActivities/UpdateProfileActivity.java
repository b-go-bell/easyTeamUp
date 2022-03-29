package com.example.easyteamup.Activities.UserHomeActivities.UpdateProfileActivities;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.easyteamup.Activities.SnackBarActivities.SnackBarFragment;
import com.example.easyteamup.Activities.SnackBarActivities.SnackBarInterface;
import com.example.easyteamup.Activities.UserHomeActivities.ViewProfileActivity;
import com.example.easyteamup.Backend.FirebaseOperations;
import com.example.easyteamup.Backend.User;
import com.example.easyteamup.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class UpdateProfileActivity extends AppCompatActivity implements SnackBarInterface {

    private Intent getIntent;
    private String uid;
    private FirebaseOperations fops;
    private FragmentManager fragmentManager;

    private EditText email, password, firstName, lastName, phone, major, year, bio;
    private ImageView photo;
    private TextView error;
    private Button changePassword, updateProfile, cancelUpdate;

    private Uri oldImageUri;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        getSupportActionBar().hide();

        fops = new FirebaseOperations(this);

        fragmentManager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.snackbar_container, SnackBarFragment.class, bundle)
                .commit();

        getIntent = getIntent();
        uid = getIntent.getStringExtra("uid");

        //setting up views
        changePassword = (Button) findViewById(R.id.change_password);
        email = (EditText) findViewById(R.id.email);
        firstName = (EditText) findViewById(R.id.first);
        lastName = (EditText) findViewById(R.id.last);
        phone = (EditText) findViewById(R.id.phone);
        major = (EditText) findViewById(R.id.major);
        year = (EditText) findViewById(R.id.year);
        bio = (EditText) findViewById(R.id.bio);

        photo = (ImageView) findViewById(R.id.photo);

        error = (TextView) findViewById(R.id.error);

        updateProfile = (Button) findViewById(R.id.submit_event);
        cancelUpdate = (Button) findViewById(R.id.cancel_event);

        fops.getProfilePhoto(uid, uriObject -> {
            oldImageUri = (Uri) uriObject;
            Picasso.get().load(oldImageUri).into(photo);
        });

        fops.getUserByUid(uid, userObject -> {
            User user = (User) userObject;

            email.setText(user.getEmail());
            firstName.setText(user.getFirstName());
            lastName.setText(user.getLastName());
            String phnString = String.valueOf(user.getPhone());
            if(!phnString.equals("0"))
                phone.setText(phnString);
            if(!user.getMajor().equals(""))
                major.setText(user.getMajor());
            String yrString = String.valueOf(user.getGraduationYear());
            if(!yrString.equals("0"))
                year.setText(yrString);
            if(!(user.getBio().equals("")))
                bio.setText(user.getBio());
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePasswordDialogFragment leave = ChangePasswordDialogFragment.newInstance();
                leave.show(fragmentManager, "fragment_change_password");
            }
        });


        ActivityResultLauncher<String> imageGallery = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        imageUri = uri;
                        photo.setImageURI(imageUri);
                        fops.uploadProfilePhoto(uid, imageUri, bc -> {
                            if(!bc){
                                error.setText("There was an issue setting your profile picture.");
                            }
                        });
                    }
                });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageGallery.launch("image/*");
            }
        });


        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String e = email.getText().toString();
                String psd = password.getText().toString();
                String fn = firstName.getText().toString();
                String ln = lastName.getText().toString();
                String ph = phone.getText().toString();
                String m = major.getText().toString();
                String y = year.getText().toString();
                String b = bio.getText().toString();


                fops.getUserByUid(uid, userObject -> {
                    User usr = (User) userObject;

                    if(!e.equals("")){
                        usr.setEmail(e);
                    }

                    if(!fn.equals("")){
                        usr.setFirstName(fn);
                    }

                    if(!ln.equals("")){
                        usr.setLastName(ln);
                    }

                    if(!ph.equals("")){
                        long phn;
                        try {
                            phn = Long.parseLong(ph) ;
                        }
                        catch (NumberFormatException nfe){
                            phn = 0;
                        }
                        usr.setPhone(phn);
                    }

                    if(!m.equals("")){
                        usr.setMajor(m);
                    }

                    if(!y.equals("")){
                        int yr;
                        try {
                            yr = Integer.parseInt(y) ;
                        }
                        catch (NumberFormatException nfe){
                            yr = 0;
                        }
                        usr.setGraduationYear(yr);
                    }

                    if(!b.equals("")) {
                        usr.setBio(b);
                    }

                    fops.setUser(usr, uid, bc -> {
                        if(bc){
                            Intent viewUserProfile = new Intent(UpdateProfileActivity.this, ViewProfileActivity.class);
                            viewUserProfile.putExtra("uid", uid);
                            startActivity(viewUserProfile);
                        }
                        else {
                            error.setText("There was an issue updating your profile. Please check all fields are valid.");
                            LeaveUpdateProfileDialogFragment leave = LeaveUpdateProfileDialogFragment.newInstance(uid, "fail");
                            leave.show(fragmentManager, "fragment_leave_create_event");
                        }
                    });
                });
            }
        });

        cancelUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LeaveUpdateProfileDialogFragment leave = LeaveUpdateProfileDialogFragment.newInstance(uid, "home");
                leave.show(fragmentManager, "fragment_leave_create_event");
            }
        });
    }

    public void viewPublicEvents(){
        LeaveUpdateProfileDialogFragment leave = LeaveUpdateProfileDialogFragment.newInstance(uid, "public");
        leave.show(fragmentManager, "fragment_leave_create_event");
    }

    public void viewInvitations(){
        LeaveUpdateProfileDialogFragment leave = LeaveUpdateProfileDialogFragment.newInstance(uid, "invite");
        leave.show(fragmentManager, "fragment_leave_create_event");
    }

    public void createEvent(){
        LeaveUpdateProfileDialogFragment leave = LeaveUpdateProfileDialogFragment.newInstance(uid, "create");
        leave.show(fragmentManager, "fragment_leave_create_event");
    }

    public void viewUserProfile(){
        LeaveUpdateProfileDialogFragment leave = LeaveUpdateProfileDialogFragment.newInstance(uid, "home");
        leave.show(fragmentManager, "fragment_leave_create_event");
    }

    public void viewUserHistory(){
        LeaveUpdateProfileDialogFragment leave = LeaveUpdateProfileDialogFragment.newInstance(uid, "history");
        leave.show(fragmentManager, "fragment_leave_create_event");
    }
}