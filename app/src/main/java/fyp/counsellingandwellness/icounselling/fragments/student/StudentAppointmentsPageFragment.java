package fyp.counsellingandwellness.icounselling.fragments.student;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fyp.counsellingandwellness.icounselling.StudentAppointmentAdapter;
import fyp.counsellingandwellness.icounselling.AppointmentCard;
import fyp.counsellingandwellness.icounselling.AppointmentInfo;
import fyp.counsellingandwellness.icounselling.R;
import fyp.counsellingandwellness.icounselling.UserInfo;
import fyp.counsellingandwellness.icounselling.activities.student.StudentAppointmentsPageMakeAnAppointmentActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentAppointmentsPageFragment extends Fragment {

    private Button makeAnAppointment;
    private String userId;
    private String csId;
    private RecyclerView recyclerView;
    private StudentAppointmentAdapter adapter;
    private List<AppointmentCard> appointmentList;


    public StudentAppointmentsPageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_appointments_page,
                container, false);
        userId = FirebaseAuth.getInstance().getUid();
        makeAnAppointment = view.findViewById(R.id.makeAnAppointment);


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        appointmentList = new ArrayList<>();
        adapter = new StudentAppointmentAdapter(getActivity().getApplicationContext(),appointmentList);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
       // recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareAppointments();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef = myRef.child("User").child(userId);
        ValueEventListener userListener3 = new ValueEventListener() {
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
        myRef.addValueEventListener(userListener3);

        makeAnAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StudentAppointmentsPageMakeAnAppointmentActivity.class);
                intent.putExtra("csId", csId);
                intent.putExtra("function", "make");
                startActivityForResult(intent,1);
            }
        });
        return view;
    }


    private void prepareAppointments() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Appointments");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                AppointmentInfo appointment = dataSnapshot.getValue(AppointmentInfo.class);

                int apptHour;
                int apptMin;
                int apptDay;
                int apptMonth;
                int apptYear;
                int currentHour;
                int currentMin;
                int currentDay;
                int currentMonth;
                int currentYear;
                boolean apptExpire = false;

                if(appointment.getStudent().equals(userId)){
                    String id = appointment.getAppointmentID();
                    String date = appointment.getDate();
                    String time = appointment.getTime();
                    String type = appointment.getAppointmentStatus();
                    String status = appointment.getAppointmentType();
                    String student = appointment.getStudent();
                    String dateFormatShort = appointment.getDateFormatShort();

                    String[] separatedDate = dateFormatShort.split("/");
                    apptMonth = Integer.parseInt(separatedDate[0]);
                    apptDay = Integer.parseInt(separatedDate[1]);
                    apptYear = Integer.parseInt(separatedDate[2]);

                    String[] separatedTime1 = time.split(" ");


                    if(separatedTime1[1].equals("pm"))
                    {
                        String[] separatedTime2 = separatedTime1[0].split(":");
                        apptHour = Integer.parseInt(separatedTime2[0]) + 12;
                        apptMin = Integer.parseInt(separatedTime2[1]);
                    }
                    else
                    {
                        String[] separatedTime2 = separatedTime1[0].split(":");
                        apptHour = Integer.parseInt(separatedTime2[0]);
                        apptMin = Integer.parseInt(separatedTime2[1]);
                    }
                    Date currentTime = Calendar.getInstance().getTime();
                    DateFormat df = new SimpleDateFormat("MM/dd/yy/HH/mm/ss");
                    String reportDate = df.format(currentTime);
                    String[] separatedCurrentDateTime = reportDate.split("/");
                    currentMonth = Integer.parseInt(separatedCurrentDateTime[0]);
                    currentDay = Integer.parseInt(separatedCurrentDateTime[1]);
                    currentYear = Integer.parseInt(separatedCurrentDateTime[2]);
                    currentHour = Integer.parseInt(separatedCurrentDateTime[3]);
                    currentMin = Integer.parseInt(separatedCurrentDateTime[4]);

                    if(apptYear < currentYear)
                    {
                        apptExpire = true;
                    }
                    else if(apptYear == currentYear && apptMonth < currentMonth)
                    {
                        apptExpire = true;
                    }
                    else if (apptYear == currentYear && apptMonth == currentMonth && apptDay < currentDay)
                    {
                        apptExpire = true;
                    }
                    else if ( apptYear == currentYear && apptMonth == currentMonth && apptDay == currentDay && apptHour < currentHour)
                    {
                        apptExpire = true;
                    }
                    else if ( apptYear == currentYear && apptMonth == currentMonth && apptDay == currentDay && apptHour == currentHour && apptMin < currentMin)
                    {
                        apptExpire = true;
                    }
                    else
                    {
                        apptExpire = false;
                    }
                    if(apptExpire == true)
                    {
                        Firebase.setAndroidContext(getActivity());
                        Firebase ref = new Firebase("https://icounselling-fyp.firebaseio.com/Appointments");
                        ref.child(id).removeValue();
                    }
                    else
                    {
                        AppointmentCard appointmentDetails = new AppointmentCard(id,date,time,status,type,student);
                        appointmentList.add(appointmentDetails);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        adapter.notifyDataSetChanged();
    }

}
