package fyp.counsellingandwellness.icounselling.activities.student;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import fyp.counsellingandwellness.icounselling.R;
import fyp.counsellingandwellness.icounselling.UserInfo;

public class StudentPickCounsellorProfileActivity extends AppCompatActivity {

    TextView name;
    TextView major;
    TextView about;
    TextView email;
    Button choose;

    ArrayList<String> counsellorId = new ArrayList<String>();
    public String Name;
    public String userId;
    public String csId;
    public int writeIterator = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_pick_counsellor_profile);

        name = findViewById(R.id.name);
        major = findViewById(R.id.major);
        about = findViewById(R.id.about);
        email = findViewById(R.id.email);
        choose = findViewById(R.id.choose);

        counsellorId.add("RCMihGnFs7NbttzuDvejrrqaWmB2");
        counsellorId.add("VNGwz2DklyYUocAJgEdVIoLKKqt1");
        counsellorId.add("YVygUOIcyFdgcXmr2ltKTzkBD9i2");
        counsellorId.add("bNI09PsgimbcIEvlItWx4NEcx8m1");
        counsellorId.add("lq7w43yfVFch4XgCzGTr9XiANyx1");
        counsellorId.add("oxVuQ4jTLPPx1s5OoE4sj0zfBVs2");
        counsellorId.add("qLJrGAoAR9gdPxzikGg0s1giaTt2");
        counsellorId.add("qLJrGAoAR9gdPxzikGg0s1giaTt2");

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            Name = String.valueOf(bundle.getString("counsellorName"));
            name.setText(Name);
        }
        userId = FirebaseAuth.getInstance().getUid();

        for (int x=0; x<counsellorId.size(); x++) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference();
            String csidlist = counsellorId.get(x);
            myRef = myRef.child("User").child(csidlist);


            ValueEventListener userListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                    writeDetails(userInfo);
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                }
            };
            myRef.addValueEventListener(userListener);
        }

        choose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(StudentPickCounsellorProfileActivity.this);
                alertDialog.setMessage("Are you sure?")
                        .setPositiveButton("Yeap, absolutely", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Toast.makeText(StudentPickCounsellorProfileActivity.this, "Time to say hello to your counsellor", Toast.LENGTH_SHORT).show();

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference Ref1 = database.getReference();
                                Ref1.child("User").child(userId).child("counsellor").setValue(csId).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent intent = new Intent(StudentPickCounsellorProfileActivity.this, StudentNavBarActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                                DatabaseReference Ref2 = database.getReference();
                                Ref2 = Ref2.child("chat").child("users").child(userId);
                                Ref2.child(name.getText().toString()).child("UID").setValue(csId);

                                DatabaseReference dbref = database.getReference();
                                dbref = dbref.child("User").child(userId);

                                ValueEventListener userListener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference Ref3 = database.getReference();
                                        Ref3 = Ref3.child("chat").child("users").child(csId);
                                        UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                                        Ref3.child(userInfo.getName()).child("UID").setValue(userId);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError error) {
                                        // Failed to read value
                                    }
                                };
                                dbref.addValueEventListener(userListener);
                                Intent intent = new Intent(StudentPickCounsellorProfileActivity.this, StudentNavBarActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        })
                        .setNegativeButton("Nope, changed my mind", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Toast.makeText(StudentPickCounsellorProfileActivity.this, "No worries, take your time :)", Toast.LENGTH_SHORT).show();
                            }
                        });
                alertDialog.show();
            }
        });
    }

    private void writeDetails(UserInfo userInfo){
        String testn = Name;
        String testn2 = userInfo.getName();
        if(testn.equals(testn2))
        {
            String testm = userInfo.getMajor();
            String testd = userInfo.getDescription();
            String teste = userInfo.getEmail();

            major.setText(testm);
            about.setText(testd);
            email.setText(teste);
            csId = counsellorId.get(writeIterator);
        }
        writeIterator++;
    }
}