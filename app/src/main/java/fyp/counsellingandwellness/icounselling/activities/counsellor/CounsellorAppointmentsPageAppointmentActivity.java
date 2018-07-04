package fyp.counsellingandwellness.icounselling.activities.counsellor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fyp.counsellingandwellness.icounselling.AppointmentInfo;
import fyp.counsellingandwellness.icounselling.CounsellorAppointmentAdapter;
import fyp.counsellingandwellness.icounselling.R;
import fyp.counsellingandwellness.icounselling.UserInfo;
import fyp.counsellingandwellness.icounselling.activities.SessionActvity;
import fyp.counsellingandwellness.icounselling.activities.SessionViewStudentProfileActivity;

public class CounsellorAppointmentsPageAppointmentActivity extends AppCompatActivity {

    private String appointmentId;
    private TextView nickName;
    private TextView date;
    private TextView time;
    private TextView type;
    private Button viewProfile;
    private Button accept;
    private Button decline;
    private FirebaseDatabase database;
    private String studentId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counsellor_appointments_page_appointment);

        appointmentId = getIntent().getStringExtra("apptId");

        nickName = findViewById(R.id.nickname);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        type = findViewById(R.id.appointmentType);
        viewProfile = findViewById(R.id.viewProfile);
        accept = findViewById(R.id.buttonAccept);
        decline = findViewById(R.id.buttonDecline);

        database = FirebaseDatabase.getInstance();
        DatabaseReference fillapptref = database.getReference();
        fillapptref = fillapptref.child("Appointments").child(appointmentId);
        ValueEventListener fillapptUl = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                AppointmentInfo apptInfo = dataSnapshot.getValue(AppointmentInfo.class);
                time.setText(apptInfo.getTime());
                date.setText(apptInfo.getDate());
                type.setText(apptInfo.getAppointmentType());
                studentId = apptInfo.getStudent();

                DatabaseReference getStudentNameRef = database.getReference();
                getStudentNameRef = getStudentNameRef.child("User").child(studentId);
                ValueEventListener getStudentNameRefUl = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                        nickName.setText(userInfo.getName());
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {}
                };
                getStudentNameRef.addListenerForSingleValueEvent(getStudentNameRefUl);

            }
            @Override
            public void onCancelled(DatabaseError error) {}
        };
        fillapptref.addListenerForSingleValueEvent(fillapptUl);


        accept.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                DatabaseReference getStudentNameRef = database.getReference();
                getStudentNameRef = getStudentNameRef.child("Appointments").child(appointmentId);
                getStudentNameRef.child("appointmentStatus").setValue("accepted");
                Intent intent = new Intent(CounsellorAppointmentsPageAppointmentActivity.this, CounsellorNavBarActivity.class);
               startActivity(intent);
                finish();
            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DatabaseReference getStudentNameRef = database.getReference();
                getStudentNameRef = getStudentNameRef.child("Appointments").child(appointmentId);
                getStudentNameRef.removeValue();
               Intent intent = new Intent(CounsellorAppointmentsPageAppointmentActivity.this, CounsellorNavBarActivity.class);
               startActivity(intent);
                finish();


            }
        });

        viewProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(CounsellorAppointmentsPageAppointmentActivity.this, SessionViewStudentProfileActivity.class);
                intent.putExtra("studentId",studentId);
                startActivity(intent);

            }
        });



    }
}
