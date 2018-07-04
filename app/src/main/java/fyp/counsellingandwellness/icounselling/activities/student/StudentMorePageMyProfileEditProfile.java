package fyp.counsellingandwellness.icounselling.activities.student;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fyp.counsellingandwellness.icounselling.R;
import fyp.counsellingandwellness.icounselling.UserDetails;
import fyp.counsellingandwellness.icounselling.UserInfo;

public class StudentMorePageMyProfileEditProfile extends AppCompatActivity {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDataRef;
    private UserInfo userInfo;
    private String userId;
    private String oldName;

    private TextView email;
    private EditText nickname;
    private EditText course;
    private RadioButton radioFemale;
    private RadioButton radioMale;
    private RadioButton radioNotsay;
    private EditText age;
    private EditText contact;
    private EditText address;
    private String csid;
    private Firebase reference1,reference2;

    private Button saveDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_more_page_my_profile_edit_profile);

        userId = FirebaseAuth.getInstance().getUid();

        Firebase.setAndroidContext(this);


        Bundle extras = getIntent().getExtras();
        oldName = extras.getString("oldName");

        email = findViewById(R.id.email);
        nickname = findViewById(R.id.nickname);
        course = findViewById(R.id.course);
        radioFemale = findViewById(R.id.radioFemale);
        radioMale = findViewById(R.id.radioMale);
        radioNotsay = findViewById(R.id.radioNotsay);
        age = findViewById(R.id.age);
        contact = findViewById(R.id.contact);
        address = findViewById(R.id.address);
        saveDetails = findViewById(R.id.saveDetails);

        mDatabase = FirebaseDatabase.getInstance();
        mDataRef = mDatabase.getReference();
        mDataRef = mDataRef.child("User").child(userId);


        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInfo = dataSnapshot.getValue(UserInfo.class);
                email.setText(userInfo.getEmail());
                nickname.setText(userInfo.getName());
                course.setText(userInfo.getCourse());
                if("Male".equals(userInfo.getGender()))
                {
                    radioMale.setChecked(true);
                    radioFemale.setChecked(false);
                    radioNotsay.setChecked(false);
                }
                else if ("Female".equals(userInfo.getGender()))
                {
                    radioMale.setChecked(false);
                    radioFemale.setChecked(true);
                    radioNotsay.setChecked(false);
                }
                else if ("I'd rather not say".equals(userInfo.getGender()))
                {
                    radioMale.setChecked(false);
                    radioFemale.setChecked(false);
                    radioNotsay.setChecked(true);
                }
                age.setText(userInfo.getAge());
                contact.setText(userInfo.getContact());
                address.setText(userInfo.getAddress());
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
            String test = nickname.getText().toString();
            String test1 = course.getText().toString();

            if (!"".equals(nickname.getText().toString())) {
                mDataRef.child("name").setValue(nickname.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("yay","running");

                        editChatName();
                        Intent intent = new Intent(StudentMorePageMyProfileEditProfile.this, StudentMorePageMyProfile.class);
                        startActivity(intent);
                        finish();
                    }
                });
            } else {
                mDataRef.child("name").setValue("Student").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("yay","running");
                        editChatName();
                        Intent intent = new Intent(StudentMorePageMyProfileEditProfile.this, StudentMorePageMyProfile.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }


            if (!"".equals(course.getText().toString())) {
                mDataRef.child("course").setValue(course.getText().toString());
            } else {
                mDataRef.child("course").setValue("I'd rather not say");
            }

            if (radioFemale.isChecked()) {
                mDataRef.child("gender").setValue("Female");
            } else if (radioMale.isChecked()) {
                mDataRef.child("gender").setValue("Male");
            } else if (radioNotsay.isChecked()) {
                mDataRef.child("gender").setValue("I'd rather not say");
            }

            if (!"".equals(age.getText().toString())) {
                mDataRef.child("age").setValue(age.getText().toString());
            } else {
                mDataRef.child("age").setValue("I'd rather not say");
            }

            if (!"".equals(contact.getText().toString())) {
                mDataRef.child("contact").setValue(contact.getText().toString());
            } else {
                mDataRef.child("contact").setValue("I'd rather not say");
            }

            if (!"".equals(address.getText().toString())) {
                mDataRef.child("address").setValue(address.getText().toString());
            } else {
                mDataRef.child("address").setValue("I'd rather not say");
            }
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radioFemale:
                if (checked) {
                    radioFemale.setChecked(true);
                    //mDataRef.child("gender").setValue("Female");
                }
                break;
            case R.id.radioMale:
                if (checked) {
                    radioMale.setChecked(true);
                    // mDataRef.child("gender").setValue("Male");
                }
                break;
            case R.id.radioNotsay:
                if (checked) {
                    radioNotsay.setChecked(true);
                    // mDataRef.child("gender").setValue("I'd rather not say");
                }
                break;
        }
    }

    public void editChatName()
    {
        mDataRef = mDatabase.getReference();
        mDataRef = mDataRef.child("User").child(userId);

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInfo = dataSnapshot.getValue(UserInfo.class);

                csid = userInfo.getCounsellor();
                DatabaseReference mDataRef2 =  mDatabase.getReference();
                mDataRef2.child("chat").child("users").child(csid).child(oldName).setValue(null);
                if (!"".equals(nickname.getText().toString())) {
                    mDataRef2.child("chat").child("users").child(csid).child(nickname.getText().toString()).child("UID").setValue(userId);
                    //UserDetails.username = nickname.getText().toString();
                    reference1 = new Firebase("https://icounselling-fyp.firebaseio.com/chat/message/" + userId + "_" + csid);
                    reference2 = new Firebase("https://icounselling-fyp.firebaseio.com/chat/message/" + csid+ "_" +userId);

                    DatabaseReference mdRef = mDatabase.getReference();

                    mdRef.child("chat")
                            .child("message")
                            .child(userId + "_" + csid)
                            .orderByChild("user")
                            .equalTo(oldName)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                        String clubkey = childSnapshot.getKey();
                                        reference1.child(clubkey).child("user").setValue(nickname.getText().toString());
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                    mdRef.child("chat")
                            .child("message")
                            .child(csid+ "_" + userId)
                            .orderByChild("user")
                            .equalTo(oldName)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                        String clubkey = childSnapshot.getKey();
                                        reference2.child(clubkey).child("user").setValue(nickname.getText().toString());
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                }
                else
                {
                    mDataRef2.child("chat").child("users").child(csid).child("Student").child("UID").setValue(userId);
                    //UserDetails.username = "Student";
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        };
        mDataRef.addValueEventListener(userListener);

    }
}
