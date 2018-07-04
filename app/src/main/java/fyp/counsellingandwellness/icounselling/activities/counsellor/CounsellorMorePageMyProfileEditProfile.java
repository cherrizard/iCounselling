package fyp.counsellingandwellness.icounselling.activities.counsellor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fyp.counsellingandwellness.icounselling.R;
import fyp.counsellingandwellness.icounselling.UserInfo;

public class CounsellorMorePageMyProfileEditProfile extends AppCompatActivity {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDataRef;
    private UserInfo userInfo;
    private String userId;

    private TextView name;
    private EditText major;
    private EditText about;

    private Button saveDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counsellor_more_page_my_profile_edit_profile);

        userId =  FirebaseAuth.getInstance().getUid();

        name = findViewById(R.id.name);
        major = findViewById(R.id.major);
        about = findViewById(R.id.about);
        saveDetails = findViewById(R.id.saveDetails);

        mDatabase = FirebaseDatabase.getInstance();
        mDataRef = mDatabase.getReference();
        mDataRef = mDataRef.child("User").child(userId);

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInfo = dataSnapshot.getValue(UserInfo.class);
                name.setText(userInfo.getName());
                major.setText(userInfo.getMajor());
                about.setText(userInfo.getDescription());
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        };
        mDataRef.addValueEventListener(userListener);

        saveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String test = major.getText().toString();
            String test1 = about.getText().toString();

            if (!"".equals(major.getText().toString())) {
                mDataRef.child("major").setValue(major.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("yay","running");
                        Intent intent = new Intent(CounsellorMorePageMyProfileEditProfile.this, CounsellorMorePageMyProfile.class);
                        startActivity(intent);
                    }
                });
            }

            if (!"".equals(about.getText().toString())) {
                mDataRef.child("course").setValue(about.getText().toString());
            }
            }
        });
    }
}
