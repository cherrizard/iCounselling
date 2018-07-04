package fyp.counsellingandwellness.icounselling.activities;

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
import fyp.counsellingandwellness.icounselling.activities.counsellor.CounsellorVideoCallOutgoingActivity;
import fyp.counsellingandwellness.icounselling.activities.counsellor.CounsellorVoiceCallOutgoingActivity;

public class SessionViewStudentProfileActivity extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDataRef;
    private UserInfo userInfo;

    private TextView nickname;
    private TextView course;
    private TextView gender;
    private TextView age;
    private TextView contact;
    private TextView address;

    private String userId;
    private String studentId;
    private ImageButton voiceCall;
    private ImageButton videoCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_view_student_profile);

        studentId = getIntent().getStringExtra("studentId");

        userId =  FirebaseAuth.getInstance().getUid();

        nickname = findViewById(R.id.nickname);
        course = findViewById(R.id.course);
        gender = findViewById(R.id.gender);
        age = findViewById(R.id.age);
        contact = findViewById(R.id.contact);
        address = findViewById(R.id.address);
        voiceCall = findViewById(R.id.voiceCall);
        videoCall = findViewById(R.id.videoCall);

        mDatabase = FirebaseDatabase.getInstance();
        mDataRef = mDatabase.getReference();
        mDataRef = mDataRef.child("User").child(studentId);

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInfo = dataSnapshot.getValue(UserInfo.class);

                nickname.setText(userInfo.getName());
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

        voiceCall.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {

                Intent intent = new Intent(getApplicationContext(), CounsellorVoiceCallOutgoingActivity.class);
              //  intent.putExtra("callerId", callerId);
                intent.putExtra("recipientId", studentId);
                startActivity(intent);

            }
        });

        videoCall.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {

                Intent intent = new Intent(getApplicationContext(), CounsellorVideoCallOutgoingActivity.class);
                intent.putExtra("recipientId", studentId);
                startActivity(intent);

            }
        });


    }
}
