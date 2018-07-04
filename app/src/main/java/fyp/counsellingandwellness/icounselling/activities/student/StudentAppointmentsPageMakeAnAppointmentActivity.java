package fyp.counsellingandwellness.icounselling.activities.student;

import java.text.DateFormat;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fyp.counsellingandwellness.icounselling.AppointmentInfo;
import fyp.counsellingandwellness.icounselling.CounsellorAppointmentAdapter;
import fyp.counsellingandwellness.icounselling.R;
import fyp.counsellingandwellness.icounselling.DatePickerFragment;
import fyp.counsellingandwellness.icounselling.TimePickerFragment;
import fyp.counsellingandwellness.icounselling.UserInfo;

public class StudentAppointmentsPageMakeAnAppointmentActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private RadioButton radioVoice;
    private RadioButton radioVideo;
    private RadioButton radioFace;
    private Button bookAppointmentButton;
    private String apptDate = null;
    private String apptTime= null;
    private String apptType= null;
    private DatabaseReference appointmentRef;
    private FirebaseDatabase database;
    private String csId;
    private String userId;
    private String function;
    private TextView showTime;
    private TextView showDate;
    private String appointmentId;
    private Button cancelAppointmentButton;
    private String dateFormatShort;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_appointments_page_make_an_appointment);

        showTime = findViewById(R.id.showTime);
        showDate = findViewById(R.id.showDate);
        radioVoice = findViewById(R.id.radioVoice);
        radioVideo = findViewById(R.id.radioVideo);
        radioFace = findViewById(R.id.radioFace);
        cancelAppointmentButton = findViewById(R.id.cancelAppointment);


        function = getIntent().getStringExtra("function");
        userId = FirebaseAuth.getInstance().getUid();



        Button buttonDate = findViewById(R.id.buttonDate);
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        Button buttonTime = findViewById(R.id.buttonTime);
        buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        database = FirebaseDatabase.getInstance();
        DatabaseReference getCsidRef = database.getReference();
        getCsidRef = getCsidRef.child("User").child(userId);
        ValueEventListener getCsidUl = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                csId = userInfo.getCounsellor();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        };
        getCsidRef.addValueEventListener(getCsidUl);

        Firebase.setAndroidContext(this);
        appointmentRef = database.getReference("Appointments");
        bookAppointmentButton = findViewById(R.id.bookAppointment);

        if(function.equals("edit"))
        {
            cancelAppointmentButton.setVisibility(View.VISIBLE);
            bookAppointmentButton.setText("Edit");
            appointmentId = getIntent().getStringExtra("apptId");

            DatabaseReference fillapptref = database.getReference();
            fillapptref = fillapptref.child("Appointments").child(appointmentId);
            ValueEventListener fillapptUl = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    AppointmentInfo apptInfo = dataSnapshot.getValue(AppointmentInfo.class);
                    showTime.setText(apptInfo.getTime());
                    apptTime = apptInfo.getTime();
                    showDate.setText(apptInfo.getDate());
                    apptDate = apptInfo.getDate();
                    if(apptInfo.getAppointmentType().equals("Voice"))
                    {
                        radioVoice.setChecked(true);
                        apptType = "Voice";
                    }
                    else if(apptInfo.getAppointmentType().equals("Video"))
                    {
                        radioVideo.setChecked(true);
                        apptType = "Video";
                    }
                    else if(apptInfo.getAppointmentType().equals("Face-To-Face"))
                    {
                        radioFace.setChecked(true);
                        apptType = "Face-To-Face";
                    }
                }
                @Override
                public void onCancelled(DatabaseError error) {}
            };
            fillapptref.addListenerForSingleValueEvent(fillapptUl);

            bookAppointmentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DatabaseReference updateApptRef = database.getReference();
                    updateApptRef = updateApptRef.child("Appointments").child(appointmentId);

                    if( apptDate != null && apptTime != null && apptType != null) {
                        updateApptRef.child("date").setValue(apptDate);
                        updateApptRef.child("time").setValue(apptTime);
                        if(radioVoice.isChecked())
                        {
                            updateApptRef.child("appointmentType").setValue("Voice");
                        }
                        else if(radioVideo.isChecked())
                        {
                            updateApptRef.child("appointmentType").setValue("Video");
                        }
                        else if(radioFace.isChecked())
                        {
                            updateApptRef.child("appointmentType").setValue("Face-To-Face");
                        }

                        Intent intent = new Intent(StudentAppointmentsPageMakeAnAppointmentActivity.this, StudentNavBarActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(StudentAppointmentsPageMakeAnAppointmentActivity.this, "Please do not leave any field blank", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        else if(function.equals("make"))
        {
            cancelAppointmentButton.setVisibility(View.INVISIBLE);
            bookAppointmentButton.setText("MAKE AN APPOINTMENT");
            bookAppointmentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if( apptDate != null && apptTime != null && apptType != null)
                    {
                        DatabaseReference apptIdRef = appointmentRef.push();
                        String apptId = apptIdRef.getKey();
                        apptIdRef.setValue(new AppointmentInfo(apptId,csId, userId, apptDate, apptTime, apptType, "Pending",dateFormatShort));
                        finish();
                    }
                    else
                    {
                        Toast.makeText(StudentAppointmentsPageMakeAnAppointmentActivity.this, "Please do not leave any field blank", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        cancelAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(StudentAppointmentsPageMakeAnAppointmentActivity.this);
                alertDialog.setMessage("Sure you want to cancel your appointment?")
                        .setPositiveButton("yeap, cancel it", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(StudentAppointmentsPageMakeAnAppointmentActivity.this, "make a new appointment anytime", Toast.LENGTH_SHORT).show();
                                DatabaseReference removeApptRef = database.getReference();
                                removeApptRef = removeApptRef.child("Appointments");
                                removeApptRef.child(appointmentId).removeValue();
                                Intent intent = new Intent(StudentAppointmentsPageMakeAnAppointmentActivity.this, StudentNavBarActivity.class);
                               startActivity(intent);
                                finish();



                            }
                        })
                        .setNegativeButton("nope, keep it", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(StudentAppointmentsPageMakeAnAppointmentActivity.this, "look forward to it", Toast.LENGTH_SHORT).show();

                            }
                        });
                alertDialog.show();
            }
        });

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        //String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        apptDate =  DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        dateFormatShort = DateFormat.getDateInstance(DateFormat.SHORT).format(c.getTime());

        showDate.setText(apptDate);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {

        int hour = hourOfDay % 12;
        apptTime = String.format("%02d:%02d %s", hour == 0 ? 12 : hour, minute, hourOfDay < 12 ? "am" : "pm");
        showTime.setText(apptTime);
        //showTime.setText(String.format("%02d:%02d %s", hour == 0 ? 12 : hour, minute, hourOfDay < 12 ? "am" : "pm"));
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radioVoice:
                if (checked) {
                    radioVoice.setChecked(true);
                    apptType = "Voice";
                }
                break;
            case R.id.radioVideo:
                if (checked) {
                    radioVideo.setChecked(true);
                    apptType = "Video";
                }
                break;
            case R.id.radioFace:
                if (checked) {
                    radioFace.setChecked(true);
                    apptType = "Face-To-Face";
                }
                break;
        }
    }
}