package fyp.counsellingandwellness.icounselling.fragments.student;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fyp.counsellingandwellness.icounselling.R;
import fyp.counsellingandwellness.icounselling.UserInfo;
import fyp.counsellingandwellness.icounselling.activities.LoginActivity;
import fyp.counsellingandwellness.icounselling.activities.student.StudentMorePageMyCounsellor;
import fyp.counsellingandwellness.icounselling.activities.student.StudentMorePageMyProfile;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentMorePageFragment extends Fragment {

    private RelativeLayout myProfile;
    private RelativeLayout myCounsellor;
    private RelativeLayout notifications;
    private RelativeLayout passcodeFingerprint;
    private RelativeLayout syncToCalendar;
    private RelativeLayout logout;
    private String csId;

    private Switch fingerPrintSwitch;
    private SharedPreferences.Editor editor;
    private SharedPreferences pref;

    private FirebaseAuth mAuth;
    private String userId;

    public StudentMorePageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_more_page,
                container, false);

        pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();

        userId = FirebaseAuth.getInstance().getUid();

        myProfile = view.findViewById(R.id.myProfile);
        myCounsellor = view.findViewById(R.id.myCounsellor);
        passcodeFingerprint =  view.findViewById(R.id.passcodeFingerprint);
        logout = view.findViewById(R.id.logout);
        fingerPrintSwitch = view.findViewById(R.id.switchFingerprint);
        mAuth = FirebaseAuth.getInstance();


        String fingerprint = pref.getString("fingerprint", "off");
        if(fingerprint.equals("on"))
        {
            fingerPrintSwitch.setChecked(true);
        }
        else
        {
            fingerPrintSwitch.setChecked(false);
        }

        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StudentMorePageMyProfile.class);
                startActivity(intent);
            }
        });

        myCounsellor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();
                myRef = myRef.child("User").child(userId);
                ValueEventListener userListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                        csId = userInfo.getCounsellor();
                        Intent intent = new Intent(getActivity(), StudentMorePageMyCounsellor.class);
                        intent.putExtra("counsellorId",csId);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                    }
                };
                myRef.addValueEventListener(userListener);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setMessage("Sure you want to log out?")
                        .setPositiveButton("yeap, log me out", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getActivity(), "see you next time", Toast.LENGTH_SHORT).show();
                                mAuth.signOut();
                                Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finishAffinity();
                            }
                        })
                        .setNegativeButton("nope, decided to stay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getActivity(), "glad your staying", Toast.LENGTH_SHORT).show();
                            }
                        });
                alertDialog.show();
            }
        });


        fingerPrintSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    editor.putString("fingerprint", "on");
                    editor.commit();
                }
                else
                {
                    editor.putString("fingerprint", "off");
                    editor.commit();
                }
            }
        });
        return view;
    }
}
