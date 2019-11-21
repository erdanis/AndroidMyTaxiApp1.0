package com.example.mytaxiapp10;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST = 100;
    private static final String TAG = "EmailPassword";

    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;
    String useremail="";
    String uid="";
    // [START declare_auth]
    public FirebaseAuth mAuth;
    // [END declare_auth]


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Views
        mStatusTextView = findViewById(R.id.status);
        mDetailTextView = findViewById(R.id.detail);
        mEmailField = findViewById(R.id.fieldEmail);
        mPasswordField = findViewById(R.id.fieldPassword);
        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            finish();
        }
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission == PackageManager.PERMISSION_GRANTED) {
            if (uid.length()>1){
                startTrackerService();}
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                        PERMISSIONS_REQUEST);
            }
            else
            { ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);}
        }
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        // ...
                    }
                });
        // [END sign_in_with_email]
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {

        if (user != null) {
            useremail = user.getEmail();
            uid = user.getUid();
            mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,
                    useremail, user.isEmailVerified()));
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, uid));

            findViewById(R.id.emailSignInButton).setVisibility(View.GONE);
            findViewById(R.id.gotoappbutton).setVisibility(View.VISIBLE);
            findViewById(R.id.signOutbutton).setVisibility(View.VISIBLE);
            startTrackerService();
        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            findViewById(R.id.emailSignInButton).setVisibility(View.VISIBLE);
            findViewById(R.id.gotoappbutton).setVisibility(View.GONE);
            findViewById(R.id.signOutbutton).setVisibility(View.GONE);
            useremail="";
            uid="";
            Intent intent = new Intent(this,TrackingService.class);
            stopService(intent);

            Toast.makeText(this, "GPS tracking disabled", Toast.LENGTH_SHORT).show();
        }
    }

        public void signInButton (View v) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }

    public void signOut(View v) {
        mAuth.signOut();
        updateUI(null);
    }

    public void goToTaxiAppActivity(View v) {
        Intent intent = new Intent (this, TaxiMainActivity.class);
        intent.putExtra("user", useremail);
        intent.putExtra("userid", uid);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if(uid.length()>1){
            startTrackerService();}
        } else {
            Toast.makeText(this, "Please enable location services to allow GPS tracking", Toast.LENGTH_SHORT).show();
        }
    }
    private void startTrackerService() {
        if (useremail.length()>1){
        Intent intent = new Intent(this,TrackingService.class);
        //intent.putExtra("userid", uid);
        intent.putExtra("user",useremail);
        startService(intent);
            //Notify the user that tracking has been enabled//
            Toast.makeText(this, "GPS tracking enabled", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Debes iniciar sesion", Toast.LENGTH_SHORT).show();
        }

//Close MainActivity//
    }

}



