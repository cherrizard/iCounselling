package fyp.counsellingandwellness.icounselling.fragments.counsellor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import fyp.counsellingandwellness.icounselling.AppointmentCard;
import fyp.counsellingandwellness.icounselling.AppointmentInfo;
import fyp.counsellingandwellness.icounselling.CounsellorAppointmentAdapter;
import fyp.counsellingandwellness.icounselling.R;
import fyp.counsellingandwellness.icounselling.StudentAppointmentAdapter;
import fyp.counsellingandwellness.icounselling.UserInfo;

public class CounsellorAppointmentsPageFragment extends Fragment {

    private String userId;
    private RecyclerView recyclerView;
    private List<AppointmentCard> appointmentList;
    private CounsellorAppointmentAdapter adapter;
    private String studentName;
    private String id;
    private String date;
    private String time;
    private String type;
    private String status;
    private FirebaseDatabase database;
    private String studentId;
    private int count = 0;
    private DatabaseReference getStudentNameRef;
    private TextView noAppointmentText; //noUsersText
    private String dateFormatShort;



    public CounsellorAppointmentsPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_counsellor_appointments_page,
                container, false);

        userId = FirebaseAuth.getInstance().getUid();
        database = FirebaseDatabase.getInstance();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        noAppointmentText = (TextView) view.findViewById(R.id.noUsersText);
        recyclerView.setHasFixedSize(true);

        appointmentList = new ArrayList<>();

        adapter = new CounsellorAppointmentAdapter(getActivity().getApplicationContext(),appointmentList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareAppointments();

        if(appointmentList.isEmpty())
        {
            int a = appointmentList.size();
            noAppointmentText.setVisibility(View.VISIBLE);
        }

        return view;

    }

    private void prepareAppointments() {

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

                if(appointment.getCounsellor().equals(userId))
                {
                    id = appointment.getAppointmentID();
                    date = appointment.getDate();
                    time = appointment.getTime();
                    type = appointment.getAppointmentStatus();
                    status = appointment.getAppointmentType();
                    studentId = appointment.getStudent();
                    dateFormatShort = appointment.getDateFormatShort();

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
                        AppointmentCard appointmentDetails = new AppointmentCard(id,date,time,status,type,studentId);
                        appointmentList.add(appointmentDetails);

                        if(!appointmentList.isEmpty())
                        {
                            appointmentList.size();
                            noAppointmentText.setVisibility(View.INVISIBLE);
                        }
                        getStudentNameRef = database.getReference();
                        getStudentNameRef = getStudentNameRef.child("User").child(studentId);
                        ValueEventListener getStudentNameRefUl = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                                appointmentList.get(count).setStudent(userInfo.getName());
                                count++;
                                adapter.notifyDataSetChanged();

                            }
                            @Override
                            public void onCancelled(DatabaseError error) {}

                        };
                        getStudentNameRef.addListenerForSingleValueEvent(getStudentNameRefUl);
                    }
                        //end of if
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



    }


