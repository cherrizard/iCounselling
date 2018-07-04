package fyp.counsellingandwellness.icounselling.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinch.android.rtc.SinchError;

import fyp.counsellingandwellness.icounselling.R;
import fyp.counsellingandwellness.icounselling.UserInfo;
import fyp.counsellingandwellness.icounselling.BaseActivity;
import fyp.counsellingandwellness.icounselling.SinchService;
import fyp.counsellingandwellness.icounselling.activities.counsellor.CounsellorNavBarActivity;
import fyp.counsellingandwellness.icounselling.activities.student.StudentNavBarActivity;


public class LoginActivity extends BaseActivity implements SinchService.StartFailedListener{
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private boolean isConnected;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mUserRef;
    private FirebaseDatabase mDatabase;
    private UserInfo userInfo;
    private String userId;
    private static String fpAccess = "neutral";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (ContextCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(LoginActivity.this,
                    new String[]{android.Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE,  Manifest.permission.CAMERA, Manifest.permission.ACCESS_NETWORK_STATE},
                    1);
        }
/*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_PHONE_STATE},100);
        }
*/
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();

/*      getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
       */

        editTextEmail = findViewById(R.id.editText_email);
        editTextPassword = findViewById(R.id.edittext_password);
        buttonLogin = findViewById(R.id.button_login);
        mAuth = FirebaseAuth.getInstance();




        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d("TAG", "Signed in" + user.getUid());
                } else {
                    Log.d("TAG", "Signed out");
                }
            }
        };

        Runnable checkInternet = new Runnable() {
            @Override
            public void run() {
                onCheckInternetConnection(LoginActivity.this);
            }
        };
        new Thread(checkInternet).start();


        mDatabase = FirebaseDatabase.getInstance();
        mUserRef = mDatabase.getReference();
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isConnected = false;
                if (!isConnected) {
                    onCheckInternetConnection(LoginActivity.this);
//                    Runnable checkInternet = new Runnable() {
//                        @Override
//                        public void run() {
//
//                        }
//                    };
//                    new Thread(checkInternet).start();
                }
                login();

            }
        });

        FirebaseAuth auth = FirebaseAuth.getInstance();

        String fingerprint = pref.getString("fingerprint", "off");

        if(fingerprint.equals("on"))
        {
            if(auth.getCurrentUser() != null){
                if(fpAccess.equals("neutral"))
                {
                    fpAccess = "denied";
                    Intent intent = new Intent(LoginActivity.this,FingerprintActivity.class);
                    startActivity(intent);
                }
                else{
                    fpAccess = getIntent().getStringExtra("fpAccess");
                    if(fpAccess.equals("granted"))
                    {
                        fpAccess = "neutral";
                        startNewActivity();
                    }
                }
            }
        }
        else{
            if(auth.getCurrentUser() != null){
                startNewActivity();
            }
        }
    }

    @Override
    public void onStarted() {
    }

    @Override
    public void onStartFailed(SinchError error) {

    }

    @Override
    protected void onServiceConnected() {
        getSinchServiceInterface().setStartListener(this);
    }


    public void onCheckInternetConnection(Context context) {
        isConnected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if ((networkInfo != null) && networkInfo.isConnected()) {
            isConnected = true;
        }
    }

    public void login() {
        Log.d("TAG", "Login!");
        isPasswordNull();
        isEmailValid();
        if (isPasswordNull() || !isEmailValid() || !isConnected) {
            if (isPasswordNull() || !isEmailValid()) {
                onFailedLogin();
            }
            if (!isConnected) {
                notifyNoConnection();
            }
        } else {

            buttonLogin.setEnabled(false);

            final ProgressDialog progDialog = new ProgressDialog(LoginActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progDialog.setIndeterminate(true);
            progDialog.setMessage("Signing in");
            progDialog.show();

            final String emailInput = editTextEmail.getText().toString().trim();
            final String passwordInput = editTextPassword.getText().toString();

            Runnable loginTask = new Runnable() {
                @Override
                public void run() {
                    mAuth.signInWithEmailAndPassword(emailInput, passwordInput).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {


                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                onSuccessfulLogin();
                                progDialog.dismiss();
                            } else {
                                onFailedLogin();
                                progDialog.dismiss();
                            }
                        }
                    });
                }
            };

            new Thread(loginTask).start();

        }

    }

    private boolean isEmailValid() {

        boolean isEmail = false;

        String input = editTextEmail.getText().toString().trim();

        if (input.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
            editTextEmail.setError("Please enter a valid email address.");
        } else {
            editTextEmail.setError(null);
            isEmail = true;
        }

        return isEmail;
    }

    private boolean isPasswordNull() {
        boolean isPasswordNull = true;

        String pass = editTextPassword.getText().toString().trim();

        if (pass.isEmpty()) {
            editTextPassword.setError("Please enter your password.");
        } else {
            editTextPassword.setError(null);
            isPasswordNull = false;
        }
        return isPasswordNull;
    }

    public void onFailedLogin() {
        Toast.makeText(LoginActivity.this, "Login failed. Please try again.", Toast.LENGTH_LONG).show();
        buttonLogin.setEnabled(true);
    }

    public void notifyNoConnection() {
        Toast.makeText(LoginActivity.this, "No internet connection.", Toast.LENGTH_LONG).show();
        buttonLogin.setEnabled(true);
    }

    public void onSuccessfulLogin() {
        Toast.makeText(LoginActivity.this, "Signed in.", Toast.LENGTH_LONG).show();
        buttonLogin.setEnabled(true);
        startNewActivity();

    }

    public void startNewActivity() {

        userId = mAuth.getCurrentUser().getUid();
        String test = userId;
        mUserRef = mUserRef.child("User").child(userId);
        final ProgressDialog progDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progDialog.setIndeterminate(true);
        progDialog.setMessage("Loading user data...");
        progDialog.show();
        progDialog.dismiss();


        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInfo = dataSnapshot.getValue(UserInfo.class);
                progDialog.dismiss();
                checkUserType();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mUserRef.addValueEventListener(userListener);
    }

    private void checkUserType()
    {
        if(userInfo!= null)
        {
            String role = userInfo.getRole();
            String test = role;

            if(role.equals("counsellor"))
            {
                if (!getSinchServiceInterface().isStarted()) {
                    getSinchServiceInterface().startClient(userId);
                }
                Intent intent = new Intent(LoginActivity.this, CounsellorNavBarActivity.class);
                startActivity(intent);
                finish();
            }
            else if(role.equals("student"))
            {
                if (!getSinchServiceInterface().isStarted()) {
                    getSinchServiceInterface().startClient(userId);
                }
                Intent intent = new Intent(LoginActivity.this, StudentNavBarActivity.class);
                startActivity(intent);
                finish();
            }
        }

    }
}



