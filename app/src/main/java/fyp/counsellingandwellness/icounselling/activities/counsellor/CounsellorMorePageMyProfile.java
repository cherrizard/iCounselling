package fyp.counsellingandwellness.icounselling.activities.counsellor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fyp.counsellingandwellness.icounselling.R;
import fyp.counsellingandwellness.icounselling.UserInfo;

public class CounsellorMorePageMyProfile extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDataRef;
    private UserInfo userInfo;

    private ImageButton editProfile;
    private TextView name;
    private TextView major;
    private TextView about;
    private TextView email;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counsellor_more_page_my_profile);

        editProfile = findViewById(R.id.editProfile);

        userId =  FirebaseAuth.getInstance().getUid();

        name = findViewById(R.id.name);
        major = findViewById(R.id.major);
        about = findViewById(R.id.about);
        email = findViewById(R.id.email);

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
                email.setText(userInfo.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mDataRef.addValueEventListener(userListener);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CounsellorMorePageMyProfile.this, CounsellorMorePageMyProfileEditProfile.class);
                startActivity(intent);
            }
        });
    }
}
