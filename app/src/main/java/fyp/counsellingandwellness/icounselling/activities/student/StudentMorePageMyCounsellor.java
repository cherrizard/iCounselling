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

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fyp.counsellingandwellness.icounselling.R;
import fyp.counsellingandwellness.icounselling.UserInfo;

public class StudentMorePageMyCounsellor extends AppCompatActivity {

    private TextView name;
    private TextView major;
    private TextView about;
    private TextView email;
    private Button dismissCounsellor;
    private int writeonce = 0;

    private String userId;
    private String csId;
    FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_more_page_my_counsellor);

        dismissCounsellor = findViewById(R.id.dismissCounsellor);
        name = findViewById(R.id.name);
        major = findViewById(R.id.major);
        about = findViewById(R.id.about);
        email = findViewById(R.id.email);

        Bundle bundle = getIntent().getExtras();
        csId = String.valueOf(bundle.getString("counsellorId"));



        userId = FirebaseAuth.getInstance().getUid();

        DatabaseReference myRef1 = database.getReference();
        myRef1 = myRef1.child("User").child(csId);
        ValueEventListener userListener1 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                if(writeonce == 0) {
                    writeDetails(userInfo);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        };
        myRef1.addValueEventListener(userListener1);


        dismissCounsellor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(StudentMorePageMyCounsellor.this);
                alertDialog.setMessage("By selecting yes all communication history will be removed permanently")
                        .setPositiveButton("Yes, I'm ready", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(StudentMorePageMyCounsellor.this, "Feel free to come back any time :)", Toast.LENGTH_SHORT).show();

                                removeAppointments();
                                removeChats();

                            }
                        })
                        .setNegativeButton("Nope, just kidding", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(StudentMorePageMyCounsellor.this, "Thanks for staying with me", Toast.LENGTH_SHORT).show();
                            }
                        });
                alertDialog.show();

            }
        });
    }



    private void writeDetails(UserInfo userInfo){
            String nm = userInfo.getName();
            String mj = userInfo.getMajor();
            String desc = userInfo.getDescription();
            String em = userInfo.getEmail();
            name.setText(nm);
            major.setText(mj);
            about.setText(desc);
            email.setText(em);

    }

    private void removeAppointments()
    {
        DatabaseReference mdRef = database.getReference();
        mdRef.child("Appointments")
                .orderByChild("student")
                .equalTo(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            Firebase apptRef = new Firebase("https://icounselling-fyp.firebaseio.com/Appointments");
                            String clubkey = childSnapshot.getKey();
                            apptRef.child(clubkey).removeValue();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    private void removeChats(){

        DatabaseReference Ref2 = database.getReference();
        Ref2 = Ref2.child("User").child(userId);
        Ref2.child("counsellor").setValue("none");

        ValueEventListener userListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference Ref = database.getReference();
                Ref = Ref.child("chat").child("users");
                Ref.child(csId).child(userInfo.getName()).removeValue();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        };
        Ref2.addValueEventListener(userListener2);


        DatabaseReference Ref3 = database.getReference();
        Ref3 = Ref3.child("User").child(csId);
        ValueEventListener userListener3 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference Ref = database.getReference();
                Ref = Ref.child("chat").child("users");
                Ref.child(userId).child(userInfo.getName()).removeValue();

                Intent intent = new Intent(StudentMorePageMyCounsellor.this, StudentNavBarActivity.class);
                intent.putExtra("userid", userId);
                startActivity(intent);
                finish();
                writeonce = 1;
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        };
        Ref3.addValueEventListener(userListener3);

        Firebase reference1, reference2;
        Firebase.setAndroidContext(StudentMorePageMyCounsellor.this);
        reference1 = new Firebase("https://icounselling-fyp.firebaseio.com/chat/message/" + userId + "_" + csId);
        reference2 = new Firebase("https://icounselling-fyp.firebaseio.com/chat/message/" + csId + "_" + userId);
        reference1.removeValue();
        reference2.removeValue();

    }
}
