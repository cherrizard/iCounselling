package fyp.counsellingandwellness.icounselling.activities.counsellor;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import fyp.counsellingandwellness.icounselling.R;
import fyp.counsellingandwellness.icounselling.fragments.counsellor.CounsellorStudentsPageFragment;
import fyp.counsellingandwellness.icounselling.fragments.counsellor.CounsellorAppointmentsPageFragment;
import fyp.counsellingandwellness.icounselling.fragments.counsellor.CounsellorMorePageFragment;
import fyp.counsellingandwellness.icounselling.fragments.HomePageFragment;

public class CounsellorNavBarActivity extends AppCompatActivity {

    static String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counsellor_nav_bar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(new HomePageFragment());

        userId =  FirebaseAuth.getInstance().getUid();
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

                case R.id.navigation_students:
                    Bundle bundle_students = new Bundle();
                    CounsellorStudentsPageFragment fragment_students = new CounsellorStudentsPageFragment();
                    fragment_students.setArguments(bundle_students);
                    loadFragment(fragment_students);
                    return true;

                case R.id.navigation_appointments:
                    fragment = new CounsellorAppointmentsPageFragment();
                    loadFragment(fragment);
                    return true;

                case R.id.navigation_more:
                    CounsellorMorePageFragment fragment_more = new CounsellorMorePageFragment();
                    loadFragment(fragment_more);
                    return true;
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
}