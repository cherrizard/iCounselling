package fyp.counsellingandwellness.icounselling.fragments.counsellor;

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

import fyp.counsellingandwellness.icounselling.R;
import fyp.counsellingandwellness.icounselling.activities.counsellor.CounsellorMorePageMyProfile;
import fyp.counsellingandwellness.icounselling.activities.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class CounsellorMorePageFragment extends Fragment {

    private RelativeLayout myProfile;
    private RelativeLayout notifications;
    private RelativeLayout passcodeFingerprint;
    private RelativeLayout syncToCalendar;
    private RelativeLayout logout;

    private FirebaseAuth mAuth;
    private String userId;
    private Switch fingerPrintSwitch;
    private SharedPreferences.Editor editor;
    private SharedPreferences pref;



    public CounsellorMorePageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_counsellor_more_page,
                container, false);

        pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();


        userId = FirebaseAuth.getInstance().getUid();

        myProfile = (RelativeLayout) view.findViewById(R.id.myProfile);
        passcodeFingerprint = (RelativeLayout) view.findViewById(R.id.passcodeFingerprint);
        logout = (RelativeLayout) view.findViewById(R.id.logout);
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
                Intent intent = new Intent(getActivity(), CounsellorMorePageMyProfile.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setMessage("You are logging out..")
                        .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getActivity(), "Logged out", Toast.LENGTH_SHORT).show();
                                mAuth.signOut();
                                Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finishAffinity();
                            }
                        })
                        .setNegativeButton("Nope, I have butterfingers", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getActivity(), "You're welcome", Toast.LENGTH_SHORT).show();
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
