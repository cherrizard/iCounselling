package fyp.counsellingandwellness.icounselling.activities.student;

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

public class StudentMorePageMyProfile extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDataRef;
    private UserInfo userInfo;

    private ImageButton editProfile;

    private TextView nickname;
    private TextView email;
    private TextView course;
    private TextView gender;
    private TextView age;
    private TextView contact;
    private TextView address;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_more_page_my_profile);

        editProfile = findViewById(R.id.editProfile);

        userId =  FirebaseAuth.getInstance().getUid();

        nickname = findViewById(R.id.nickname);
        email = findViewById(R.id.email);
        course = findViewById(R.id.course);
        gender = findViewById(R.id.gender);
        age = findViewById(R.id.age);
        contact = findViewById(R.id.contact);
        address = findViewById(R.id.address);

        mDatabase = FirebaseDatabase.getInstance();
        mDataRef = mDatabase.getReference();
        mDataRef = mDataRef.child("User").child(userId);

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInfo = dataSnapshot.getValue(UserInfo.class);

                nickname.setText(userInfo.getName());
                email.setText(userInfo.getEmail());
                course.setText(userInfo.getCourse());
                gender.setText(userInfo.getGender());
                age.setText(userInfo.getAge());
                contact.setText(userInfo.getContact());
                address.setText(userInfo.getAddress());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mDataRef.addValueEventListener(userListener);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentMorePageMyProfile.this, StudentMorePageMyProfileEditProfile.class);
                intent.putExtra("oldName",nickname.getText().toString() );
                startActivity(intent);
            }
        });
    }
}
