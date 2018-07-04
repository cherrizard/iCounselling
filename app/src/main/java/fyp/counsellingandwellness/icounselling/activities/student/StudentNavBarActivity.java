package fyp.counsellingandwellness.icounselling.activities.student;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fyp.counsellingandwellness.icounselling.R;
import fyp.counsellingandwellness.icounselling.UserInfo;
import fyp.counsellingandwellness.icounselling.BaseActivity;
import fyp.counsellingandwellness.icounselling.fragments.HomePageFragment;
import fyp.counsellingandwellness.icounselling.fragments.student.StudentSessionPageFragment;
import fyp.counsellingandwellness.icounselling.fragments.student.StudentAppointmentsPageFragment;
import fyp.counsellingandwellness.icounselling.fragments.student.StudentMorePageFragment;

public class StudentNavBarActivity extends BaseActivity{

    private String userId;
    boolean hasCounsellor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_nav_bar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        userId = FirebaseAuth.getInstance().getUid();

        HomePageFragment myFragment = new HomePageFragment();
        //THEN NOW SHOW OUR FRAGMENT
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, myFragment).commitAllowingStateLoss();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef = myRef.child("User").child(userId);
        ValueEventListener userListener3 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                hasCounsellor = checkStudentCounsellor(userInfo);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        };
        myRef.addValueEventListener(userListener3);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomePageFragment();
                    loadFragment(fragment);
                    return true;

                case R.id.navigation_session:
                        if(hasCounsellor == false)
                        {
                            selectCounsellor();
                        }
                        else if(hasCounsellor == true){
                            StudentSessionPageFragment fragment_session = new StudentSessionPageFragment();
                            //fragment = new StudentSessionPageFragment();
                            loadFragment(fragment_session);
                            return true;
                        }

                case R.id.navigation_appointments:

                    if(hasCounsellor == false)
                    {
                        selectCounsellor();
                    }
                    else if(hasCounsellor == true) {
                        fragment = new StudentAppointmentsPageFragment();
                        loadFragment(fragment);
                        return true;
                    }

                case R.id.navigation_more:

                    if(hasCounsellor == false)
                    {
                        selectCounsellor();
                    }
                    else if(hasCounsellor == true) {
                        fragment = new StudentMorePageFragment();
                        loadFragment(fragment);
                        return true;
                    }

            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private boolean checkStudentCounsellor(UserInfo userInfo) {
            String counsellor = userInfo.getCounsellor();
            if ("none".equals(counsellor)) {
                //THEN NOW SHOW OUR FRAGMENT
                return false;
            }
            else{
                return true;
            }
    }

    private void selectCounsellor()
    {
        Intent intent = new Intent(StudentNavBarActivity.this, StudentPickCounsellorActivity.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(StudentNavBarActivity.this, StudentNavBarActivity.class);
        startActivity(intent);
    }
}