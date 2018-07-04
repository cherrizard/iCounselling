package fyp.counsellingandwellness.icounselling.activities.counsellor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinch.android.rtc.calling.Call;

import fyp.counsellingandwellness.icounselling.R;
import fyp.counsellingandwellness.icounselling.UserInfo;
import fyp.counsellingandwellness.icounselling.BaseActivity;
import fyp.counsellingandwellness.icounselling.SinchService;
import fyp.counsellingandwellness.icounselling.activities.SessionVideoCallScreenActivity;

public class CounsellorVideoCallOutgoingActivity extends BaseActivity {

    private Button mCallButton;
    private String sId;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDataRef;
    private UserInfo userInfo;
    private String chatWithName;
    private TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counsellor_video_call_outgoing);

        Intent intent = getIntent();
        sId = intent.getStringExtra("recipientId");

        //initializing UI elements
        mCallButton = (Button) findViewById(R.id.button_call);
        userName = (TextView) findViewById(R.id.loggedInName);
        mCallButton.setEnabled(false);
        mCallButton.setOnClickListener(buttonClickListener);

        mDatabase = FirebaseDatabase.getInstance();
        mDataRef = mDatabase.getReference();
        mDataRef = mDataRef.child("User").child(sId);

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInfo = dataSnapshot.getValue(UserInfo.class);
                chatWithName = userInfo.getName();
                userName.setText(chatWithName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mDataRef.addValueEventListener(userListener);
    }

    // invoked when the connection with SinchServer is established
    @Override
    protected void onServiceConnected() {
        mCallButton.setEnabled(true);
    }

    @Override
    public void onDestroy() {
        if (getSinchServiceInterface() != null) {
            getSinchServiceInterface().stopClient();
        }
        super.onDestroy();
    }

    private void callButtonClicked() {
        String userName = sId;
        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter a user to call", Toast.LENGTH_LONG).show();
            return;
        }


        Call call = getSinchServiceInterface().callUserVideo(userName);
        String callId = call.getCallId();

        Intent callScreen = new Intent(this, SessionVideoCallScreenActivity.class);
        callScreen.putExtra(SinchService.CALL_ID, callId);
        callScreen.putExtra("chatWithName", chatWithName);
        callScreen.putExtra("chatWithId", sId);
        callScreen.putExtra("role", "counsellor");
        startActivity(callScreen);
    }


    private OnClickListener buttonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_call:
                    callButtonClicked();
                    break;
            }
        }
    };
}
